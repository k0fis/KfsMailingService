package kfs.mailingservice.domain;

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
public class MailTemplateAttach {

    @KfsTablePos({
        @Pos(value = 1, name = "MailAttachList", genName = "id0")})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private MailTemplate template;

    @KfsTablePos({
        @Pos(value = 20, name = "MailAttachList", genName = "name0"),
    })
    @OneToOne(optional = false)
    private MailAttach attach;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(MailTemplate template) {
        this.template = template;
    }

    public MailAttach getAttach() {
        return attach;
    }

    public void setAttach(MailAttach attach) {
        this.attach = attach;
    }
}
