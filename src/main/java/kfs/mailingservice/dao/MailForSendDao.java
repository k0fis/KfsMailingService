package kfs.mailingservice.dao;

import java.util.List;
import kfs.kfscrm.domain.KfsContact;
import kfs.mailingservice.domain.MailForSent;
import kfs.springutils.BaseDao;


/**
 *
 * @author pavedrim
 */
public interface MailForSendDao extends BaseDao<MailForSent, Long> {
    
    void updateForceTransaction(MailForSent mailForSent);

    List<MailForSent> load(KfsContact contact);
    
    List<MailForSent> load(int limit);

    List<MailForSent> load(MailForSent.MailStatus status, int limit);
}
