package kfs.mailingservice.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import kfs.kfsvaalib.kfsTable.KfsTablePos;
import kfs.kfsvaalib.kfsTable.Pos;

/**
 *
 * @author pavedrim
 */
@Entity
public class MailFooterAttach {

    @KfsTablePos({
        @Pos(value = 10, name = "MailAttachList", genName = "id0"),})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private MailFooter footer;

    @KfsTablePos({
        @Pos(value = 20, name = "MailAttachList", genName = "name"),})
    @OneToOne(optional = false)
    private MailAttach attach;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.footer);
        hash = 37 * hash + Objects.hashCode(this.attach);
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
        final MailFooterAttach other = (MailFooterAttach) obj;
        if (!Objects.equals(this.footer, other.footer)) {
            return false;
        }
        if (!Objects.equals(this.attach, other.attach)) {
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

    public MailAttach getAttach() {
        return attach;
    }

    public void setAttach(MailAttach attach) {
        this.attach = attach;
    }

    public MailFooter getFooter() {
        return footer;
    }

    public void setFooter(MailFooter footer) {
        this.footer = footer;
    }
}
