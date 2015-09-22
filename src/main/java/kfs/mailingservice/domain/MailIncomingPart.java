package kfs.mailingservice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import kfs.kfsvaalib.kfsTable.KfsTablePos;
import kfs.kfsvaalib.kfsTable.Pos;

/**
 *
 * @author pavedrim
 */
@Entity
public class MailIncomingPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @KfsTablePos({
        @Pos(value = 1, name = "MailIncomingPartList", genName = "id0"),})        
    private Long id;

    @OneToOne
    private MailIncoming mailIncoming;

    @KfsTablePos({
        @Pos(value = 10, name = "MailIncomingPartList"),})        
    private String filename;

    @KfsTablePos({
        @Pos(value = 20, name = "MailIncomingPartList"),})        
    private String contentType;
    
    @Lob
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailIncoming getMailIncoming() {
        return mailIncoming;
    }

    public void setMailIncoming(MailIncoming mailIncoming) {
        this.mailIncoming = mailIncoming;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
