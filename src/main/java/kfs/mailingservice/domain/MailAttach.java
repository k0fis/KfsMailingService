package kfs.mailingservice.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import kfs.kfsvaalib.kfsForm.KfsField;
import kfs.kfsvaalib.kfsForm.KfsMField;
import kfs.kfsvaalib.kfsTable.KfsTablePos;
import kfs.kfsvaalib.kfsTable.Pos;

/**
 *
 * @author pavedrim
 */
@Entity
public class MailAttach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @KfsTablePos({
        @Pos(value = 10, name = "MailAddressDlg", genName = "id0"),})
    private Long id;
    
    @KfsMField({
        @KfsField(name = "MailAttachDlg", pos = 20, isRequired = true),})
    @KfsTablePos({
        @Pos(value = 20, name = "MailAddressDlg"),})
    @Column(nullable = false)
    private String attachName = "";
    
    @KfsMField({
        @KfsField(name = "MailAttachDlg", pos = 30),})
    @KfsTablePos({
        @Pos(value = 30, name = "MailAddressDlg"),})
    private String contentId = "";
    
    @KfsMField({
        @KfsField(name = "MailAttachDlg", pos = 40, readOnly = true),})
    @KfsTablePos({
        @Pos(value = 40, name = "MailAddressDlg"),})
    @Column(nullable = false)
    private String contentType = "";
    
    @Lob
    private byte[] image;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final MailAttach other = (MailAttach) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    
}
