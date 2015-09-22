package kfs.mailingservice.domain;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import kfs.kfscrm.api.KfsCrmDetail;
import kfs.kfscrm.domain.KfsContact;
import kfs.kfsvaalib.convertors.KfsEnumI18n;
import kfs.kfsvaalib.fields.ComboField;
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
public class MailForSent implements KfsCrmDetail {

    public KfsContact getContact() {
        return contact;
    }

    public void setContact(KfsContact contact) {
        this.contact = contact;
    }
    
    public enum MailStatus {
        created, sending, sended, error, cancelSending
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @KfsTablePos({
        @Pos(value = 10, name = "MailForSentList", genName = "id0"),})    
    private Long id;

    @KfsMField({
        @KfsField(name = "MailForSentList", pos = 5, readOnly = true, fieldClass = KfsEditorField.class),})
    @OneToOne(optional = false)
    private KfsContact contact;    
    
    @KfsTablePos({
        @Pos(value = 20, name = "MailForSentList"),})    
    @KfsMField({
        @KfsField(name = "MailForSentDlg", pos = 10, isRequired = true, fieldClass = KfsEditorField.class),})
    @OneToOne(optional = false)
    private MailTemplate mailTemplate;
    
    @KfsTablePos({
        @Pos(value = 30, name = "MailForSentList"),})    
    @KfsMField({
        @KfsField(name = "MailForSentDlg", pos = 20, isRequired = true, fieldClass = KfsEditorField.class),})
    @OneToOne(optional = false)
    private MailAddress toAddress;

    @KfsTablePos({
        @Pos(value = 40, name = "MailForSentList"),})        
    @KfsMField({
        @KfsField(name = "MailForSentDlg", pos = 30, isRequired = true, fieldClass = KfsEditorField.class),})    
    @OneToOne(optional = false)
    private MailAddress fromAddress;
    
    @KfsTablePos({
        @Pos(value = 60, name = "MailForSentList"),})    
    @KfsMField({
        @KfsField(name = "MailForSentDlg", pos = 50, readOnly = true),})
    private Timestamp created;
    
    @KfsTablePos({
        @Pos(value = 70, name = "MailForSentList"),})    
    @KfsMField({
        @KfsField(name = "MailForSentDlg", pos = 60, readOnly = true),})
    private Timestamp sended;
    
    @KfsTablePos({
        @Pos(value = 50, name = "MailForSentList", converter = KfsEnumI18n.class),})    
    @KfsMField({
        @KfsField(name = "MailForSentDlg", pos = 40, fieldClass = ComboField.class),})
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailStatus mailStatus;

    public MailStatus getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(MailStatus mailStatus) {
        this.mailStatus = mailStatus;
    }
    
    public void statusCreated() {
        mailStatus = MailStatus.created;
    }
    
    public void statusSending() {
        mailStatus = MailStatus.sending;
    }
    
    public void statusSended() {
        mailStatus = MailStatus.sended;
    }
    
    public void statusError() {
        mailStatus = MailStatus.error;
    }

    public MailAddress getToAddress() {
        return toAddress;
    }

    public void setToAddress(MailAddress toAddress) {
        this.toAddress = toAddress;
    }

    public MailAddress getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(MailAddress fromAddress) {
        this.fromAddress = fromAddress;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailTemplate getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(MailTemplate mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getSended() {
        return sended;
    }

    public void setSended(Timestamp sended) {
        this.sended = sended;
    }
    
    @Override
    public Timestamp getDetailDate() {
        return created;
    }

    public static String crmDetailName = "E-Mail O";

    @Override
    public String getDetailName() {
        return crmDetailName;
    }

    @Override
    public String getDetailText() {
        return mailTemplate.getName()+" " + mailStatus.name();
    }    
}
