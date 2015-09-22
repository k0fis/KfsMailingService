package kfs.mailingservice.dao.jpa;

import java.util.List;
import kfs.kfscrm.domain.KfsContact;
import kfs.mailingservice.dao.MailIncomingDao;
import kfs.mailingservice.domain.MailIncoming;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pavedrim
 */
@Repository
public class JpaMailIncomingDao extends BaseDaoJpa<MailIncoming, Long> implements MailIncomingDao {

    @Override
    protected Class<MailIncoming> getDataClass() {
        return MailIncoming.class;
    }

    @Override
    protected Long getId(MailIncoming data) {
        return data.getId();
    }

    @Override
    public List<MailIncoming> load(KfsContact contact) {
        return em.createQuery("SELECT a FROM MailIncoming a WHERE a.contact = :contact ORDER BY a.incomingTime")
                .setParameter("contact", contact)
                .getResultList();
    }

}
