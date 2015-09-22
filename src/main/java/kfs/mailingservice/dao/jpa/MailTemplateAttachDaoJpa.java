package kfs.mailingservice.dao.jpa;

import java.util.List;
import kfs.mailingservice.dao.MailTemplateAttachDao;
import kfs.mailingservice.domain.MailTemplate;
import kfs.mailingservice.domain.MailTemplateAttach;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pavedrim
 */
@Repository
public class MailTemplateAttachDaoJpa extends BaseDaoJpa<MailTemplateAttach, Long > implements MailTemplateAttachDao{

    @Override
    protected Class<MailTemplateAttach> getDataClass() {
        return MailTemplateAttach.class;
    }

    @Override
    protected Long getId(MailTemplateAttach data) {
        return data.getId();
    }

    @Override
    public List<MailTemplateAttach> load(MailTemplate template) {
        return em.createQuery("SELECT a FROM MailTemplateAttach a WHERE a.template = :template")
                .setParameter("template", template)
                .getResultList();
    }

}
