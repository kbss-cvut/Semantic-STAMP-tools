package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.datatools.mail.imap.idle.IDLEMailMessageReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class EmailSourceService {

    @Autowired
    protected IDLEMailMessageReader idleImapReader;


    @PostConstruct
    protected void init() {
        start();
    }

    public void start() {
        Thread daemon = new Thread(() -> idleImapReader.waitForEmails());
        daemon.start();
    }

    public void stop() throws MessagingException {
        idleImapReader.close();
    }
}
