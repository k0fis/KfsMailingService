package kfs.mailingservice.dao;

import java.util.List;
import kfs.kfscrm.domain.KfsContact;
import kfs.mailingservice.domain.MailIncoming;
import kfs.springutils.BaseDao;

/**
 *
 * @author pavedrim
 */
public interface MailIncomingDao extends BaseDao<MailIncoming, Long>{

    //List<MailIncoming> load(int limit);

    List<MailIncoming> load(KfsContact contact);

}
