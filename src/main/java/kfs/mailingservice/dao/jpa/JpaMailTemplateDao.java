package kfs.mailingservice.dao.jpa;

import java.util.List;
import javax.persistence.NoResultException;
import kfs.mailingservice.dao.MailTemplateDao;
import kfs.mailingservice.domain.MailTemplate;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pavedrim
 */
@Repository
public class JpaMailTemplateDao extends BaseDaoJpa<MailTemplate, Long> implements MailTemplateDao {

    @Override
    protected Class<MailTemplate> getDataClass() {
        return MailTemplate.class;
    }

    @Override
    protected Long getId(MailTemplate data) {
        return data.getId();
    }

    @Override
    public MailTemplate findFull(MailTemplate mt) {
        MailTemplate ret = find(mt);
        ret.getCcList().size();
        return ret;
    }

    @Override
    public MailTemplate findByName(String name) {
        try {
            return (MailTemplate) em.createQuery("select mt from MailTemplate mt where mt.name = :name")
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<MailTemplate> load() {
        return em.createQuery("select mt from MailTemplate mt ORDER BY mt.name")
                .getResultList();
    }

}
