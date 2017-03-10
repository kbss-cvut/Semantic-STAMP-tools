package cz.cvut.kbss.reporting.service.data.mail;

import cz.cvut.kbss.datatools.mail.imap.idle.IDLEMailMessageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.MessagingException;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class EmailSourceService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSourceService.class);

    private static final String THREAD_NAME = "IMAPReaderThread";

    private final IDLEMailMessageReader idleImapReader;

    private Thread imapReaderThread;

    @Autowired
    public EmailSourceService(IDLEMailMessageReader idleImapReader) {
        this.idleImapReader = idleImapReader;
    }

    @PostConstruct
    public void start() {
        LOG.debug("Starting IMAP reader.");
        this.imapReaderThread = new Thread(idleImapReader::waitForEmails, THREAD_NAME);
        imapReaderThread.start();
    }

    @PreDestroy
    public void stop() throws MessagingException {
        LOG.debug("Closing IMAP reader.");
        idleImapReader.close();
        imapReaderThread.interrupt();
    }
}
