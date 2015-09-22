package kfs.mailingservice.domain;

import com.vaadin.ui.RichTextArea;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.fields.KfsSelectField;
import kfs.kfsvaalib.kfsForm.KfsField;
import kfs.kfsvaalib.kfsForm.KfsMField;
import kfs.kfsvaalib.kfsTable.KfsTablePos;
import kfs.kfsvaalib.kfsTable.Pos;
import org.hibernate.annotations.Type;

/**
 *
 * @author pavedrim
 */
@Entity
public class MailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @KfsTablePos({
        @Pos(value = 1, name = "MailTemplateList", genName = "id0"),})
    private Long id;

    @KfsMField({
        @KfsField(name = "MailTemplateDlg", pos = 10, isRequired = true),})
    @Column(unique = true)
    @KfsTablePos({
        @Pos(value = 10, name = "MailTemplateList"),})
    private String name;

    @KfsMField({
        @KfsField(name = "MailTemplateDlg", pos = 20),})
    @KfsTablePos({
        @Pos(value = 20, name = "MailTemplateList"),})
    private String note;

    @KfsMField({
        @KfsField(name = "MailTemplateDlg", pos = 30, isRequired = true, fieldClass = KfsEditorField.class),})
    @OneToOne
    private MailAddress fromTemplate;

    @KfsMField({
        @KfsField(name = "MailTemplateDlg", pos = 50, isRequired = true),})
    @Size(min = 3, max = 255)
    @Column(nullable = false)
    private String mailSubject;

    @KfsMField({
        @KfsField(name = "MailTemplateDlg", pos = 40, fieldClass = KfsEditorField.class),})
    @ManyToMany
    private List<MailAddress> ccList;

    @KfsMField({
        @KfsField(name = "MailTemplateDlg", pos = 70, fieldClass = KfsSelectField.class),})
    @OneToOne
    private MailFooter footer;

    @KfsMField({
        @KfsField(name = "MailTemplateDlg", pos = 60, isRequired = true, fieldClass = RichTextArea.class),})
    @Lob
    @Type(type="org.hibernate.type.StringClobType")
    @NotNull
    private String mailText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailAddress getFromTemplate() {
        return fromTemplate;
    }

    public void setFromTemplate(MailAddress fromTemplate) {
        this.fromTemplate = fromTemplate;
    }

    public String getMailText() {
        return mailText;
    }

    public void setMailText(String mailText) {
        this.mailText = mailText;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public List<MailAddress> getCcList() {
        return ccList;
    }

    public void setCcList(List<MailAddress> ccList) {
        this.ccList = ccList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MailFooter getFooter() {
        return footer;
    }

    public void setFooter(MailFooter footer) {
        this.footer = footer;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MailTemplate other = (MailTemplate) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
}
