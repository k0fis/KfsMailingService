package kfs.mailingservice.dao.jpa;

import java.util.Arrays;
import java.util.List;
import kfs.mailingservice.dao.MailFooterAttachDao;
import kfs.mailingservice.domain.MailFooter;
import kfs.mailingservice.domain.MailFooterAttach;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pavedrim
 */
@Repository
public class MailFooterAttachDaoJpa extends BaseDaoJpa<MailFooterAttach, Long>
        implements MailFooterAttachDao {

    @Override
    protected Class<MailFooterAttach> getDataClass() {
        return MailFooterAttach.class;
    }

    @Override
    protected Long getId(MailFooterAttach data) {
        return data.getId();
    }

    @Override
    public List<MailFooterAttach> load(MailFooter footer) {
        if (footer.getId() == null) {
            return Arrays.asList();
        }
        return em.createQuery("SELECT a FROM MailFooterAttach a WHERE a.footer = :footer")
                .setParameter("footer", footer)
                .getResultList();
    }

}
