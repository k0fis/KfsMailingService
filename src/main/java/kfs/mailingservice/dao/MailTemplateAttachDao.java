package kfs.mailingservice.dao;

import java.util.List;
import kfs.mailingservice.domain.MailTemplate;
import kfs.mailingservice.domain.MailTemplateAttach;
import kfs.springutils.BaseDao;

/**
 *
 * @author pavedrim
 */
public interface MailTemplateAttachDao extends BaseDao<MailTemplateAttach, Long> {

    List<MailTemplateAttach> load(MailTemplate template);

}
