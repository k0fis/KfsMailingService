package kfs.mailingservice.utils;

/**
 *
 * @author pavedrim
 */
public class MailingServiceException extends RuntimeException {
    public MailingServiceException(String msg) {
        super(msg);
    }
    public MailingServiceException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
