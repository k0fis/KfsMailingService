package kfs.mailingservice.dao.jpa;

import java.util.List;
import kfs.mailingservice.dao.MailFooterDao;
import kfs.mailingservice.domain.MailFooter;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pavedrim
 */
@Repository
public class MailFooterDaoJpa extends BaseDaoJpa<MailFooter, Long > implements MailFooterDao{

    @Override
    protected Class<MailFooter> getDataClass() {
        return MailFooter.class;
    }

    @Override
    protected Long getId(MailFooter data) {
        return data.getId();
    }

    @Override
    public List<MailFooter> load() {
        return em.createQuery("SELECT a FROM MailFooter a ORDER BY a.name")
                .getResultList();
    }

}
