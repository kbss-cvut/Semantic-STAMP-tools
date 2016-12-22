package cz.cvut.kbss.reporting.service.data.mail;

import cz.cvut.kbss.datatools.mail.imap.idle.IDLEMailMessageReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.MessagingException;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class EmailSourceService {

    @Autowired
    protected IDLEMailMessageReader idleImapReader;

    /**
     * WARNING: DO NOT CALL THIS BY HAND IN SPRING with configuration 
     * cz.cvut.kbss.inbas.reporting.config.AppConfig. In this settings it is called
     * automatically. This method mya be called in a script outside.
     */
    @PostConstruct
    public void start() {
        // FIXME : do not create a thread by hand find a better solution!!!
//        Thread daemon = new Thread(() -> idleImapReader.waitForEmails());
//        daemon.start();
    }

    @PreDestroy
    public void stop() throws MessagingException {
        idleImapReader.close();
    }
}
