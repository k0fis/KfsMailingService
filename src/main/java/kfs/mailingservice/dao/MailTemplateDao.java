package kfs.mailingservice.dao;

import java.util.List;
import kfs.mailingservice.domain.MailTemplate;
import kfs.springutils.BaseDao;


/**
 *
 * @author pavedrim
 */
public interface MailTemplateDao extends BaseDao<MailTemplate, Long> {
    
    MailTemplate findByName(String name);
    MailTemplate findFull(MailTemplate mt);

    List<MailTemplate> load();
}
