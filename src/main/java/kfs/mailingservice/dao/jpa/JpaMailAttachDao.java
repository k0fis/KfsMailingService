package kfs.mailingservice.dao.jpa;

import java.util.List;
import kfs.mailingservice.dao.MailAttachDao;
import kfs.mailingservice.domain.MailAttach;
import kfs.mailingservice.domain.MailTemplate;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pavedrim
 */
@Repository
public class JpaMailAttachDao extends BaseDaoJpa<MailAttach, Long> implements MailAttachDao {

    @Override
    protected Class<MailAttach> getDataClass() {
        return MailAttach.class;
    }

    @Override
    protected Long getId(MailAttach data) {
        return data.getId();
    }

    @Override
    public List<MailAttach> load(MailTemplate template) {
        return em.createQuery("select ma from MailAttach ma where ma in ( SELECT a FROM MailTemplateAttach a WHERE a.template = :template)")
                .setParameter("template", template)
                .getResultList();
    }

}
