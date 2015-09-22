package kfs.mailingservice.domain;

import com.vaadin.ui.RichTextArea;
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
import org.hibernate.annotations.Type;

/**
 *
 * @author pavedrim
 */
@Entity
public class MailFooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @KfsTablePos({
        @Pos(value = 1, name = "MailFooterList", genName = "id0"),
        @Pos(value = 1, name = "MailFooterSelect", genName = "id0"),})    
    private Long id;

    @KfsMField({
        @KfsField(name = "MailFooterDlg", pos = 10, isRequired = true),})
    @KfsTablePos({
        @Pos(value = 10, name = "MailFooterList"),})    
    @Column(unique = true)
    private String name = "";

    @KfsMField({
        @KfsField(name = "MailFooterDlg", pos = 20, fieldClass = RichTextArea.class),})
    @Lob
    @Type(type="org.hibernate.type.StringClobType")
    private String footerText = "";

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.name);
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
        final MailFooter other = (MailFooter) obj;
        if (!Objects.equals(this.name, other.name)) {
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

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
