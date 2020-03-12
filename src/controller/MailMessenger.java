/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author Jake
 */
public class MailMessenger extends Authenticator {

    PasswordAuthentication authentication = null;

    public MailMessenger(String username, String password) {
        authentication = new PasswordAuthentication(username, password);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return authentication;
    }

   
}
