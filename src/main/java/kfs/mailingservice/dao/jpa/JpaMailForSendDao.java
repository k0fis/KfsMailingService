package kfs.mailingservice.dao.jpa;

import java.util.List;
import javax.persistence.EntityTransaction;
import kfs.mailingservice.dao.MailForSendDao;
import kfs.mailingservice.domain.MailForSent;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pavedrim
 */
@Repository
public class JpaMailForSendDao extends BaseDaoJpa<MailForSent, Long> implements MailForSendDao {

    @Override
    protected Class<MailForSent> getDataClass() {
        return MailForSent.class;
    }

    @Override
    protected Long getId(MailForSent data) {
        return data.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateForceTransaction(MailForSent mailForSent) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        if (mailForSent.getId() == null) {
            em.persist(mailForSent);
        } else {
            em.merge(mailForSent);
        }
        et.commit();
    }

    @Override
    public List<MailForSent> load(int limit) {
        return em.createQuery("SELECT a FROM MailForSent a ORDER BY a.created DESC")
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<MailForSent> load(MailForSent.MailStatus status, int limit) {
        return em.createQuery("SELECT a FROM MailForSent a WHERE a.mailStatus = :status ORDER BY a.created DESC")
                .setParameter("status", status)
                .setMaxResults(limit)
                .getResultList();
    }
}
