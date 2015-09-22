package kfs.mailingservice.dao.jpa;

import java.util.List;
import kfs.mailingservice.dao.MailIncomingPartDao;
import kfs.mailingservice.domain.MailIncoming;
import kfs.mailingservice.domain.MailIncomingPart;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pavedrim
 */
@Repository
public class JpaMailIncomingPartDao extends BaseDaoJpa<MailIncomingPart, Long> implements MailIncomingPartDao {

    @Override
    protected Class<MailIncomingPart> getDataClass() {
        return MailIncomingPart.class;
    }

    @Override
    protected Long getId(MailIncomingPart data) {
        return data.getId();
    }

    @Override
    public List<MailIncomingPart> load(MailIncoming mailIncoming) {
        return em.createQuery("SELECT a FROM MailIncomingPart a WHERE a.mailIncoming = :mailIncoming ORDER BY a.contentType, a.filename")
                .setParameter("mailIncoming", mailIncoming)
                .getResultList();
    }

}
