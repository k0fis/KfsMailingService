package kfs.mailingservice.dao.jpa;

import java.util.List;
import kfs.mailingservice.dao.MailAddressDao;
import kfs.mailingservice.domain.MailAddress;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pavedrim
 */
@Repository
public class JpaMailAddressDao extends BaseDaoJpa<MailAddress, Long> implements MailAddressDao {

    @Override
    protected Class<MailAddress> getDataClass() {
        return MailAddress.class;
    }

    @Override
    protected Long getId(MailAddress data) {
        return data.getId();
    }

    @Override
    public MailAddress findByAddress(String address) {
        List<MailAddress> ret
                = em.createQuery("SELECT ma FROM MailAddress ma where ma.address = :address")
                .setParameter("address", address)
                .setMaxResults(1)
                .getResultList();
        if ((ret == null) || (ret.size() < 1)) {
            return null;
        }
        return ret.get(0);
    }

}
