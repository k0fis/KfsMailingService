package kfs.mailingservice.dao;

import java.util.List;
import kfs.mailingservice.domain.MailAttach;
import kfs.mailingservice.domain.MailTemplate;
import kfs.springutils.BaseDao;


/**
 *
 * @author pavedrim
 */
public interface MailAttachDao extends BaseDao<MailAttach, Long>{

    List<MailAttach> load(MailTemplate template);

}
