package kfs.mailingservice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import kfs.kfscrm.domain.KfsContact;
import kfs.kfscrm.service.CrmService;
import kfs.mailingservice.dao.*;
import kfs.mailingservice.domain.*;
import kfs.mailingservice.utils.MailingServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pavedrim
 */
@Transactional
public class MailingServiceImpl implements MailingService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailAddressDao mailAddressDao;

    @Autowired
    private MailAttachDao mailAttachDao;

    @Autowired
    private MailForSendDao mailForSendDao;

    @Autowired
    private MailTemplateDao mailTemplateDao;

    @Autowired
    private MailTemplateAttachDao mailTemplateAttachDao;

    @Autowired
    private MailIncomingDao mailIncomingDao;

    @Autowired
    private MailIncomingPartDao mailIncomingPartDao;

    @Autowired
    private MailFooterDao mailFooterDao;

    @Autowired
    private MailFooterAttachDao mailFooterAttachDao;

    @Value("${mail.imap.folder}")
    private String mailInboxFolder;

    @Value("${mail.imap.moveFolder}")
    private String mailMoveFolder;

    private Properties mailsProperties;

    @Autowired
    private CrmService crmService;

    @Autowired
    private Authenticator mailAuthenticator;

    private String mailUserName = "MailService";

    private static final String ctPlainText = "text/plain";
    private static final String ctMultipart = "multipart/*";
    private static final String ctMessage = "message/rfc822";

    @Override
    public void procesIncoming() {
        Session session = Session.getInstance(mailsProperties, mailAuthenticator);
        Store store;
        try {
            store = session.getStore();
        } catch (NoSuchProviderException ex) {
            throw new MailingServiceException("Cannot init store", ex);
        }
        try {
            store.connect();
        } catch (MessagingException ex) {
            throw new MailingServiceException("Cannot open mail", ex);
        }

        Folder folder2;
        try {
            folder2 = store.getFolder(mailMoveFolder);
        } catch (MessagingException ex) {
            throw new MailingServiceException("Cannot open folder " + mailMoveFolder, ex);
        }
        try {
            if (!folder2.exists()) {
                folder2.create(Folder.HOLDS_MESSAGES);
            }
        } catch (MessagingException ex) {
            throw new MailingServiceException("Cannot create " + mailMoveFolder, ex);
        }

        Folder folder;
        try {
            folder = store.getFolder(mailInboxFolder);
        } catch (MessagingException ex) {
            throw new MailingServiceException("Cannot open " + mailInboxFolder, ex);
        }

        if (!folder.isOpen()) {
            try {
                folder.open(Folder.READ_WRITE);
            } catch (MessagingException ex) {
                throw new MailingServiceException("Cannot open " + mailInboxFolder, ex);
            }
        }
        Message msgs[];
        try {
            msgs = folder.getMessages();
        } catch (MessagingException ex) {
            throw new MailingServiceException("Cannot get mails", ex);
        }
        if ((msgs != null) && (msgs.length > 0)) {
            for (Message m : msgs) {
                processIncoming(m);
                try {
                    folder2.appendMessages(new Message[]{m});
                } catch (MessagingException ex) {
                    throw new MailingServiceException("Cannot move mail into " + mailMoveFolder, ex);
                }
            }
        }
        try {
            folder2.close(true);
        } catch (MessagingException ex) {
            throw new MailingServiceException("Cannot close " + mailMoveFolder, ex);
        }
        try {
            folder.close(true);
        } catch (MessagingException ex) {
            throw new MailingServiceException("Cannot close " + mailInboxFolder, ex);
        }
        try {
            store.close();
        } catch (MessagingException ex) {
            throw new MailingServiceException("Cannot close store", ex);
        }
    }

    public void processIncoming(Message message) {
        Logger log = Logger.getLogger(getClass().getName());
        MailIncoming mi = new MailIncoming();
        try {
            mi.setSubject(message.getSubject());
        } catch (MessagingException ex) {
            log.log(Level.SEVERE, "Cannot read subject", ex);
            mi.setSubject("");
        }
        try {
            mi.setSentTime(new Timestamp(message.getSentDate().getTime()));
        } catch (MessagingException ex) {
            log.log(Level.SEVERE, "Cannot read SentDate", ex);
            mi.setSentTime(null);
        }
        try {
            mi.setIncomingTime(new Timestamp(message.getReceivedDate().getTime()));
        } catch (MessagingException ex) {
            log.log(Level.SEVERE, "Cannot read RecivedDate", ex);
            mi.setIncomingTime(new Timestamp(new Date().getTime()));
        }
        // sender
        Address[] addrss;
        try {
            addrss = message.getFrom();
        } catch (MessagingException ex) {
            throw new MailingServiceException("Cannot obtain FROM", ex);
        }
        for (Address adrs : addrss) {
            InternetAddress ia = (InternetAddress) adrs;
            MailAddress ret = mailAddressDao.findById(ia.getAddress());
            if (ret == null) {
                ret = new MailAddress();
                ret.setAddress(ia.getAddress());
                ret.setName(ia.getPersonal());
                mailAddressDao.insert(ret);
            } else {
                if ((ia.getPersonal() != null) && (ia.getPersonal().length() > 0)) {
                    ret.setName(ia.getPersonal());
                    mailAddressDao.update(ret);
                }
            }
            mi.setSender(ret);
            break;
        }

        ArrayDeque<Part> parts = new ArrayDeque<>();
        parts.add(message);

        while (!parts.isEmpty()) {
            Part p = parts.pop();
            String cct = null;
            try {
                cct = p.getContentType();
            } catch (MessagingException ex) {
                log.log(Level.SEVERE, "Cannot read contentType", ex);
            }
            if (cct != null) {
                MailIncomingPart mip;
                switch (cct) {
                    case ctPlainText:
                        mip = new MailIncomingPart();
                        mip.setMailIncoming(mi);
                        mip.setContentType(cct);
                        try {
                            mip.setFilename(p.getFileName());
                        } catch (MessagingException ex) {
                            log.log(Level.SEVERE, "Cannot read fileName", ex);
                        }
                        try {
                            mip.setData(((String) p.getContent()).getBytes());
                        } catch (IOException | MessagingException ex) {
                            log.log(Level.SEVERE, "Cannot read content", ex);
                        }
                        if (mi.getMailText() == null) {
                            mi.setMailText(new String(mip.getData()));
                        }
                        if (mip.getData() != null) {
                            mailIncomingPartDao.insert(mip);
                        }
                        break;
                    case ctMultipart:
                        Multipart mp = null;
                        try {
                            mp = (Multipart) p.getContent();
                        } catch (IOException | MessagingException ex) {
                            log.log(Level.SEVERE, "Cannot read content", ex);
                        }
                        if (mp != null) {
                            int count = 0;
                            try {
                                count = mp.getCount();
                            } catch (MessagingException ex) {
                                log.log(Level.SEVERE, "Cannot read Count", ex);
                            }
                            for (int i = 0; i < count; i++) {
                                BodyPart bp = null;
                                try {
                                    bp = mp.getBodyPart(i);
                                } catch (MessagingException ex) {
                                    log.log(Level.SEVERE, "Cannot read part", ex);
                                }
                                if (bp != null) {
                                    parts.add(bp);
                                }
                            }
                        }
                        break;
                    case ctMessage:
                        Part pp = null;
                        try {
                            pp = (Part) p.getContent();
                        } catch (IOException | MessagingException ex) {
                            log.log(Level.SEVERE, "Cannot read content", ex);
                        }
                        if (pp != null) {
                            parts.add(pp);
                        }
                        break;
                    default:
                        Object o = null;
                        try {
                            o = p.getContent();
                        } catch (IOException | MessagingException ex) {
                            log.log(Level.SEVERE, "Cannot read content", ex);
                        }
                        if (o != null) {
                            mip = new MailIncomingPart();
                            mip.setMailIncoming(mi);
                            mip.setContentType(cct);
                            try {
                                mip.setFilename(p.getFileName());
                            } catch (MessagingException ex) {
                                log.log(Level.SEVERE, "Cannot read filename", ex);
                            }
                            if (o instanceof String) {
                                mip.setData(((String) o).getBytes());
                                if (mi.getMailText() == null) {
                                    mi.setMailText(new String(mip.getData()));
                                }
                            } else if (o instanceof InputStream) {
                                mip.setData(readInputBinaryFile((InputStream) o));
                            } else {
                                mip.setData(o.toString().getBytes());
                                if (mi.getMailText() == null) {
                                    mi.setMailText(new String(mip.getData()));
                                }
                            }
                            mailIncomingPartDao.insert(mip);
                        }
                        break;
                }
            }
        }
        saveMailIncoming(mi);
        try {
            message.setFlag(Flags.Flag.DELETED, true);
        } catch (MessagingException ex) {
            log.log(Level.SEVERE, "Cannot delete message", ex);
        }
    }

    private void saveMailIncoming(MailIncoming mi) {
        String ma = mi.getSender().getAddress();
        KfsContact contact = crmService.contactFindByMail(ma, mailUserName);
        mi.setContact(contact);
        mailIncomingDao.insert(mi);
    }

    @Override
    public List<MailIncoming> incomingLoad(KfsContact contact) {
        return mailIncomingDao.load(contact);
    }

    @Override
    public MailAddress mailAddressFind(String mail) {
        if (!isValidEmailAddress(mail)) {
            return null;
        }
        MailAddress ret = mailAddressDao.findById(mail);
        if (ret == null) {
            ret = new MailAddress();
            ret.setAddress(mail);
            ret.setName(mail);
            mailAddressDao.insert(ret);
        }
        return ret;
    }

    @Override
    public void mailAddressSave(MailAddress mail) {
        MailAddress ret = mailAddressDao.findById(mail.getAddress());
        if (ret == null) {
            mailAddressDao.insert(mail);
        } else if (!ret.getName().equals(mail.getName())) {
            mailAddressDao.update(mail);
        }
    }

    @Override
    public List<MailTemplateAttach> mailTemplateAttachLoad(MailTemplate template) {
        try {
            return mailTemplateAttachDao.load(template);
        } catch (RuntimeException ex) {
            return Arrays.asList();
        }
    }

    @Override
    public List<MailAttach> mailAttachLoad(MailTemplate template) {
        try {
            return mailAttachDao.load(template);
        } catch (RuntimeException ex) {
            return Arrays.asList();
        }

    }

    @Override
    public void mailTemplateAttachDelete(MailTemplateAttach ma) {
        MailAttach mat = ma.getAttach();
        mailTemplateAttachDao.delete(ma);
        mailAttachDao.delete(mat);
    }

    @Override
    public void mailAttachSave(MailAttach mailAttach) {
        if (mailAttach.getId() == null) {
            mailAttachDao.insert(mailAttach);
        } else {
            mailAttachDao.update(mailAttach);
        }
    }

    @Override
    public void mailForSentSave(MailForSent mailForSend) {
        if (mailForSend.getId() == null) {
            mailForSend.setCreated(new Timestamp(new Date().getTime()));
            mailForSend.statusCreated();
            mailForSendDao.insert(mailForSend);
        } else {
            mailForSendDao.update(mailForSend);
        }
    }

    @Override
    public List<MailForSent> mailForSentLoad(int limit) {
        return mailForSendDao.load(limit);
    }

    @Override
    public List<MailForSent> mailForSentLoad(MailForSent.MailStatus status, int limit) {
        return mailForSendDao.load(status, limit);
    }

    @Override
    public void mailTemplateRemove(MailTemplate mt) {
        for (MailTemplateAttach mta : mailTemplateAttachLoad(mt)) {
            mailTemplateAttachDelete(mta);
        }
        mailTemplateDao.delete(mt);
    }

    @Override
    public void mailTemplateSave(MailTemplate mt) {
        if (mt.getId() == null) {
            mailAddressSave(mt.getFromTemplate());

            if (mt.getCcList() != null) {
                for (MailAddress ma : mt.getCcList()) {
                    mailAddressSave(ma);
                }
            }

            mailTemplateDao.insert(mt);
        } else {
            mailTemplateDao.update(mt);
        }
    }

    @Override
    public MailTemplate mailTemplateInit() {
        MailTemplate mt = new MailTemplate();
        mt.setFromTemplate(new MailAddress());
        mt.getFromTemplate().setAddress("");
        mt.getFromTemplate().setName("");
        mt.setCcList(new ArrayList());
        mt.setName("");
        mt.setNote("");
        mt.setMailSubject("");
        mt.setMailText("");
        return mt;
    }

    @Override
    public MailTemplate mailTemplateCreate(String name) {
        MailTemplate mt = mailTemplateDao.findByName(name);
        if (mt != null) {
            return mt;
        }
        mt = mailTemplateInit();
        mt.setName(name);
        mt.setMailSubject(name);
        mailTemplateDao.insert(mt);
        return mt;
    }
    
    @Override
    public MailTemplate mailTemplateReload(MailTemplate mt) {
        return mailTemplateDao.findFull(mt);
    }

    @Override
    public MailTemplate mailTemplateFindByName(String name) {
        return mailTemplateDao.findByName(name);
    }

    @Override
    public List<MailTemplate> mailTemplateLoad() {
        return mailTemplateDao.load();
    }

    InternetAddress getInternetAddress(MailAddress ma) throws UnsupportedEncodingException {
        return new InternetAddress(ma.getAddress(), ma.getName(), "UTF-8");
    }

    @Override
    public void sendMailDirect(MailTemplate mailTemplate, MailAddress mailTo) {
        MailForSent mfs = new MailForSent();
        mfs.setContact(crmService.contactFindByMail(mailTo.getAddress(), mailUserName));
        mfs.setCreated(new Timestamp(new Date().getTime()));
        mfs.setFromAddress(mailTemplate.getFromTemplate());
        mfs.statusCreated();
        mfs.setToAddress(mailTo);
        sendMail(mfs);
    }

    @Override
    public void sendMail(MailForSent mailForSend) {
        MailTemplate mailTemplate = mailTemplateDao.findFull(mailForSend.getMailTemplate());
        mailForSend.statusSending();
        mailForSendDao.updateForceTransaction(mailForSend);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject(mailTemplate.getMailSubject(), "UTF-8");
            message.setFrom(getInternetAddress(mailForSend.getFromAddress()));
            message.addRecipient(Message.RecipientType.TO, getInternetAddress(mailForSend.getToAddress()));
            if (mailTemplate.getCcList() != null) {
                for (MailAddress ma : mailTemplate.getCcList()) {
                    message.addRecipient(Message.RecipientType.CC, getInternetAddress(ma));
                }
            }
            Multipart multipart = new MimeMultipart();

            BodyPart mbp = new MimeBodyPart();
            mbp.setContent(mailTemplate.getMailText(), "text/html; charset=utf-8");
            multipart.addBodyPart(mbp);

            for (MailAttach ma : mailAttachLoad(mailTemplate)) {
                mbp = new MimeBodyPart();
                mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(ma.getImage(), ma.getContentType())));
                if ((ma.getContentId() != null) && (ma.getContentId().length() > 0)) {
                    mbp.setHeader("Content-ID", "<" + ma.getContentId() + ">");
                }
                multipart.addBodyPart(mbp);
            }
            message.setContent(multipart);
            mailSender.send(message);
            mailForSend.statusSended();
            mailForSendDao.updateForceTransaction(mailForSend);
        } catch (MessagingException | UnsupportedEncodingException | MailException ex) {
            mailForSend.statusError();
            mailForSendDao.updateForceTransaction(mailForSend);
            throw new MailingServiceException("Cannot sent email", ex);
        }
    }

    private final String mailValidExpression = "^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$";
    private final Pattern pattern = Pattern.compile(mailValidExpression, Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValidEmailAddress(String s) {
        if (s == null) {
            return false;
        }
        if (s.length() < 1) {
            return false;
        }
        boolean isValid = false;
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public String filterEmailAddress(String line) {
        if (line == null) {
            return null;
        }
        line = line//
                .replaceAll("\"", "")//
                .replace(",", ".")//
                .replace("\\w", "")//
                .replace(":", "")
                .replace(" AT ", "@")
                .replace(";", "")
                .trim();
        if (line.endsWith(".c")) {
            line += "z";
        }
        if (line.endsWith(".")) {
            line = line.substring(0, line.length() - 1);
        }
        if (line.contains("<") && line.contains(">")) {
            int b = line.indexOf("<");
            int e = line.indexOf(">");
            line = line.substring(b + 1, e);
        }
        if (line.contains("[") && line.contains("]")) {
            int b = line.indexOf("[");
            int e = line.indexOf("]");
            line = line.substring(b + 1, e);
        }
        if (line.contains("<")) {
            line = line.substring(line.indexOf("<") + 1);
        }
        if (line.contains(" ") && !line.contains("@")) {
            line = line.replaceFirst("\\ ", "@");
        }
        if (line.contains(" ")) {
            line = line.replaceAll("\\ ", "");
        }
        return line;
    }

    public static byte[] readInputBinaryFile(InputStream imgIS) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        try {
            while (-1 != (n = imgIS.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (IOException ex) {
            throw new MailingServiceException("Error in copy inputstream", ex);
        }
        return output.toByteArray();
    }

    public void setMailAuthenticator(Authenticator mailAuthenticator) {
        this.mailAuthenticator = mailAuthenticator;
    }

    public void setMailsProperties(Properties mailsProperties) {
        this.mailsProperties = mailsProperties;
    }

    public void setMailMoveFolder(String mailMoveFolder) {
        this.mailMoveFolder = mailMoveFolder;
    }

    public void setMailInboxFolder(String mailInboxFolder) {
        this.mailInboxFolder = mailInboxFolder;
    }

    @Override
    public List<MailIncomingPart> partLoad(MailIncoming mailIncoming) {
        return mailIncomingPartDao.load(mailIncoming);
    }

    @Override
    public void mailTemplateAttachSave(MailTemplateAttach ma) {
        mailAttachSave(ma.getAttach());
        if (ma.getId() == null) {
            mailTemplateAttachDao.insert(ma);
        } else {
            mailTemplateAttachDao.update(ma);
        }
        mailTemplateSave(ma.getTemplate());
    }

    @Override
    public void mailFooterSave(MailFooter ma, List<MailFooterAttach> attach) {
        for (MailFooterAttach att : mailFooterAttachDao.load(ma)) {
            if (!attach.contains(att)) {
                mailAttachDao.delete(att.getAttach());
                mailFooterAttachDao.delete(att);
            }
        }
        if (ma.getId() == null) {
            mailFooterDao.insert(ma);
        } else {
            mailFooterDao.update(ma);
        }
        for (MailFooterAttach att : attach) {
            mailAttachSave(att.getAttach());
            att.setFooter(ma);
            if (ma.getId() == null) {
                mailFooterAttachDao.insert(att);
            } else {
                mailFooterAttachDao.update(att);
            }
        }
    }

    @Override
    public List<MailFooter> mailFooterLoad() {
        return mailFooterDao.load();
    }

    @Override
    public List<MailFooterAttach> mailFooterAttachLoad(MailFooter footer) {
        return mailFooterAttachDao.load(footer);
    }

    @Override
    public void mailFooterDelete(MailFooter mf) {
        for (MailFooterAttach mfa : mailFooterAttachLoad(mf)) {
            mailAttachDao.delete(mfa.getAttach());
            mailFooterAttachDao.delete(mfa);
        }
        mailFooterDao.delete(mf);
    }

    public String getMailUser() {
        return mailUserName;
    }

    public void setMailUser(String mailUser) {
        this.mailUserName = mailUser;
    }
}
