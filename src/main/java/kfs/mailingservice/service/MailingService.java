package kfs.mailingservice.service;

import java.util.List;
import kfs.kfscrm.domain.KfsContact;
import kfs.mailingservice.domain.MailAddress;
import kfs.mailingservice.domain.MailAttach;
import kfs.mailingservice.domain.MailFooter;
import kfs.mailingservice.domain.MailFooterAttach;
import kfs.mailingservice.domain.MailForSent;
import kfs.mailingservice.domain.MailForSent.MailStatus;
import kfs.mailingservice.domain.MailIncoming;
import kfs.mailingservice.domain.MailIncomingPart;
import kfs.mailingservice.domain.MailTemplate;
import kfs.mailingservice.domain.MailTemplateAttach;


/**
 *
 * @author pavedrim
 */
public interface MailingService {
   
    MailAddress mailAddressFind(String mail);
    void mailAddressSave(MailAddress mail);
    
    void mailAttachSave(MailAttach mailAttach);
    List<MailAttach>mailAttachLoad(MailTemplate template);

    void mailForSentSave(MailForSent mailForSend);
    List<MailForSent> mailForSentLoad(KfsContact contact);
    List<MailForSent> mailForSentLoad(int limit);
    List<MailForSent> mailForSentLoad(MailStatus status, int limit);
    
    void mailTemplateSave(MailTemplate mailtemplate);
    void mailTemplateRemove(MailTemplate mailtemplate);
    MailTemplate mailTemplateInit();
    MailTemplate mailTemplateReload(MailTemplate mt);
    MailTemplate mailTemplateFindByName(String name);
    MailTemplate mailTemplateCreate(String name);
    List<MailTemplate> mailTemplateLoad();


    void mailTemplateAttachSave(MailTemplateAttach ma);
    void mailTemplateAttachDelete(MailTemplateAttach ma);
    List<MailTemplateAttach>mailTemplateAttachLoad(MailTemplate template);
    
    void sendMailDirect(MailTemplate mailTemplate, MailAddress mailTo);
    void sendMail(MailForSent mailForSend);
  
    List<MailIncomingPart> partLoad(MailIncoming mailIncoming);
    
    List<MailIncoming> incomingLoad(KfsContact contact);
    void procesIncoming(String statusName);
    void procesIncomingDelete(String statusName);

    void mailFooterSave(MailFooter ma, List<MailFooterAttach> attach);
    void mailFooterDelete(MailFooter mf);
    List<MailFooter> mailFooterLoad();
    List<MailFooterAttach> mailFooterAttachLoad(MailFooter footer);

    boolean isValidEmailAddress(String line);
    String filterEmailAddress(String line);

}
