package kfs.mailingservice.dao.jpa;

import kfs.mailingservice.dao.MailAddressDao;
import kfs.mailingservice.domain.MailAddress;
import kfs.springutils.BaseDaoJpa;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pavedrim
 */
@Repository
public class JpaMailAddressDao extends BaseDaoJpa<MailAddress, String> implements MailAddressDao{


    @Override
    protected Class<MailAddress> getDataClass() {
        return MailAddress.class;
    }


    @Override
    protected String getId(MailAddress data) {
        return data.getAddress();
    }
    

}
