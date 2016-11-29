package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.datatools.mail.CompoundProcessor;
import cz.cvut.kbss.datatools.mail.MessageProcessor;
import cz.cvut.kbss.datatools.mail.caa.e5xml.E5XMLLocator;
import cz.cvut.kbss.datatools.mail.imap.idle.IDLEMailMessageReader;
import cz.cvut.kbss.datatools.mail.model.Message;
import cz.cvut.kbss.inbas.reporting.persistence.dao.EmailDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.Properties;
import java.util.stream.Stream;

import static cz.cvut.kbss.datatools.mail.model.javaapi.MailConfigParameters.*;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Configuration
@PropertySource("classpath:ib-caa-email-config.properties")
public class EmailSourceConfig {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(EmailSourceConfig.class);

//    protected static final String MAIL_SERVER = "mail.server";
//    protected static final String MAIL_PROTOCOL = "mail.store.protocol";
//    protected static final String MAIL_SOCKET_FACTORY_CLASS = "mail.imap.socketFactory.class";
//    protected static final String MAIL_SOCKET_FACTORY_FALLBACK = "mail.imap.socketFactory.fallback";
//    protected static final String MAIL_SOCKET_FACTORY_PORT = "mail.imap.socketFactory.port";
//    protected static final String MAIL_USER = "mail.user";
//    protected static final String MAIL_PASSWORD = "mail.password";
//    protected static final String MAIL_FOLDER = "mail.folder";

    @Autowired
    protected Environment env;

    @Autowired
    protected EmailDao emailDao;
//    protected EMailService emailService;
  
    @Autowired
    protected ReportImporter reportImporter;

//    @Autowired
//    protected ReportImporter importer1;

    protected IDLEMailMessageReader idleImapReader;

    @Bean
    public IDLEMailMessageReader getIdleImapReader() {
        return idleImapReader;
    }

    @PostConstruct
    protected void init() {
        
        idleImapReader = new IDLEMailMessageReader() {

//            protected ReportImporter importer = importer1;

            @Override
            protected boolean isProcessed(String id) {
                return emailDao.findByMailId(id) != null;
            }

            @Override
            protected boolean setProcessed(String id) {
                return false;
            }


            @Override
            protected Object processMessage(Message m) throws MessagingException {
                return reportImporter.process(m);
            }
        };
        
        // This is not used in the idleImapReader
//        CompoundProcessor processor = new CompoundProcessor();
//        idleImapReader.setProcessor(processor);
        Properties cfg = new Properties();
        Stream.of(MAIL_PASSWORD,
                MAIL_PROTOCOL,
                MAIL_SERVER,
                MAIL_SOCKET_FACTORY_CLASS,
                MAIL_SOCKET_FACTORY_FALLBACK,
                MAIL_SOCKET_FACTORY_PORT,
                MAIL_USER,
                MAIL_FOLDER).
                filter(x -> env.getProperty(x) != null).
                forEach(x -> cfg.put(x, env.getProperty(x)));

        idleImapReader.setConfig(cfg);
    }

}
