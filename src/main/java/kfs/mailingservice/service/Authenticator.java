package kfs.mailingservice.service;

import javax.mail.PasswordAuthentication;

/**
 *
 * @author pavedrim
 */
public class Authenticator extends javax.mail.Authenticator {

    private String user;
    private String pass;
    
    
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pass);
    }    

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
