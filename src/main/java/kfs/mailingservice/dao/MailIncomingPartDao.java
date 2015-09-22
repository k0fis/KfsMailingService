package kfs.mailingservice.dao;

import java.util.List;
import kfs.mailingservice.domain.MailIncoming;
import kfs.mailingservice.domain.MailIncomingPart;
import kfs.springutils.BaseDao;

/**
 *
 * @author pavedrim
 */
public interface MailIncomingPartDao extends BaseDao<MailIncomingPart, Long>{

    List<MailIncomingPart> load(MailIncoming mailIncoming);

}
