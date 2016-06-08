/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.datatools.mail.imap.idle.IDLEMailMessageReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Service
public class EmailSourceService {
    
    @Autowired
    protected IDLEMailMessageReader idleImapReader;
  
    
    @PostConstruct
    protected void init(){
        start();
    }
    
    public void start(){
        Thread daemon = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    idleImapReader.waitForEmails();
                }
            }
        );
       daemon.start();
    }
    
    public void stop() throws MessagingException{
        idleImapReader.close();
    }
}
