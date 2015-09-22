package kfs.mailingservice.dao;

import java.util.List;
import kfs.mailingservice.domain.MailFooter;
import kfs.mailingservice.domain.MailFooterAttach;
import kfs.springutils.BaseDao;

/**
 *
 * @author pavedrim
 */
public interface MailFooterAttachDao extends BaseDao<MailFooterAttach, Long>{

    List<MailFooterAttach> load(MailFooter footer);
    
}
