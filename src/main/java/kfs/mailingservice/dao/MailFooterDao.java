package kfs.mailingservice.dao;

import java.util.List;
import kfs.mailingservice.domain.MailFooter;
import kfs.springutils.BaseDao;

/**
 *
 * @author pavedrim
 */
public interface MailFooterDao extends BaseDao<MailFooter, Long>{
    
    List<MailFooter> load();
}
