package kfs.mailingservice.domain;

import com.vaadin.ui.TextArea;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import kfs.kfscrm.api.KfsCrmDetail;
import kfs.kfscrm.domain.KfsContact;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.kfsForm.KfsField;
import kfs.kfsvaalib.kfsForm.KfsMField;
import kfs.kfsvaalib.kfsTable.KfsTablePos;
import kfs.kfsvaalib.kfsTable.Pos;

/**
 *
 * @author pavedrim
 */
@Entity
public class MailIncoming implements KfsCrmDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @KfsTablePos({
        @Pos(value = 1, name = "MailIncomingList", genName = "id0"),})    
    private Long id;

    @KfsMField({
        @KfsField(name = "MailIncomingDlg", pos = 5, readOnly = true, fieldClass = KfsEditorField.class),})
    @OneToOne(optional = false)
    private KfsContact contact;
    
    @KfsTablePos({
        @Pos(value = 10, name = "MailIncomingList"),})    
    @KfsMField({
        @KfsField(name = "MailIncomingDlg", pos = 10, readOnly = true),})
    @OneToOne
    private MailAddress sender;
    
    @KfsTablePos({
        @Pos(value = 20, name = "MailIncomingList"),})        
    @KfsMField({
        @KfsField(name = "MailIncomingDlg", pos = 20, readOnly = true),})
    private String subject;

    
    @KfsTablePos({
        @Pos(value = 30, name = "MailIncomingList"),})    
    @KfsMField({
        @KfsField(name = "MailIncomingDlg", pos = 30, readOnly = true),})    
    private Timestamp sentTime;
    
    
    @KfsTablePos({
        @Pos(value = 40, name = "MailIncomingList"),})    
    @KfsMField({
        @KfsField(name = "MailIncomingDlg", pos = 40, readOnly = true),})    
    private Timestamp incomingTime;
    
    @KfsMField({
        @KfsField(name = "MailIncomingDlg", pos = 50, readOnly = true, fieldClass = TextArea.class),})
    @Lob
    private String mailText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public MailAddress getSender() {
        return sender;
    }

    public void setSender(MailAddress sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Timestamp getIncomingTime() {
        return incomingTime;
    }

    public void setIncomingTime(Timestamp incomingTime) {
        this.incomingTime = incomingTime;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    public String getMailText() {
        return mailText;
    }

    public void setMailText(String mailText) {
        this.mailText = mailText;
    }

    public KfsContact getContact() {
        return contact;
    }

    public void setContact(KfsContact contact) {
        this.contact = contact;
    }

    @Override
    public Timestamp getDetailDate() {
        return incomingTime;
    }

    public static String crmDetailName = "E-Mail I";

    @Override
    public String getDetailName() {
        return crmDetailName;
    }

    @Override
    public String getDetailText() {
        return subject;
    }
    
}
