package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.datatools.mail.CompoundProcessor;
import cz.cvut.kbss.datatools.mail.caa.e5xml.E5XMLLocator;
import cz.cvut.kbss.datatools.mail.imap.idle.IDLEMailMessageReader;
import cz.cvut.kbss.datatools.mail.model.Message;
import static cz.cvut.kbss.datatools.mail.model.javaapi.MailConfigParameters.MAIL_FOLDER;
import static cz.cvut.kbss.datatools.mail.model.javaapi.MailConfigParameters.MAIL_PASSWORD;
import static cz.cvut.kbss.datatools.mail.model.javaapi.MailConfigParameters.MAIL_PROTOCOL;
import static cz.cvut.kbss.datatools.mail.model.javaapi.MailConfigParameters.MAIL_SERVER;
import static cz.cvut.kbss.datatools.mail.model.javaapi.MailConfigParameters.MAIL_SOCKET_FACTORY_CLASS;
import static cz.cvut.kbss.datatools.mail.model.javaapi.MailConfigParameters.MAIL_SOCKET_FACTORY_FALLBACK;
import static cz.cvut.kbss.datatools.mail.model.javaapi.MailConfigParameters.MAIL_SOCKET_FACTORY_PORT;
import static cz.cvut.kbss.datatools.mail.model.javaapi.MailConfigParameters.MAIL_USER;
import cz.cvut.kbss.datatools.mail.model.javaapi.MessageWrapperJavaAPI;
import cz.cvut.kbss.inbas.reporting.persistence.dao.EmailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.Properties;
import java.util.stream.Stream;
import javax.mail.Folder;
import org.slf4j.LoggerFactory;

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

    @Autowired
    protected ReportImporter importer1;

    protected IDLEMailMessageReader idleImapReader;

    @Bean
    public IDLEMailMessageReader getIdleImapReader() {
        return idleImapReader;
    }

    @PostConstruct
    protected void init() {
        
        idleImapReader = new IDLEMailMessageReader() {

            protected ReportImporter importer = importer1;

            @Override
            protected boolean isProcessed(String id) {
                return emailDao.findByMailId(id) != null;
            }

            @Override
            protected boolean setProcessed(String id) {
                return false;
//                EMail email = emailService.getMailById(id);
//                if (email == null) {
//                    email = new EMail();
//                    email.setId(id);
//                    emailService.persist(email);
//                }
//                return false;
            }

            @Override
            public void processOldMessages(Folder f) throws MessagingException { // FOR DEBUG
                javax.mail.Message[] ms = ms = f.getMessages(1 , 20);

                for (javax.mail.Message m : ms) {
                    MessageWrapperJavaAPI mw = new MessageWrapperJavaAPI(m);
                    String id = mw.getId();
                    if (!isProcessed(id)) {
                        processMessageImpl(m, mw);
                    }
                    break;// DEBUG
                }
            }

            @Override
            protected Object processMessage(Message m) throws MessagingException {
                return reportImporter.process(m);
            }
        };
        
        CompoundProcessor processor = new CompoundProcessor();
        processor.registerMessageProcessor(new E5XMLLocator());
//        processor.registerMessageProcessor(new CSAEmailProcessor());
//        processor.registerMessageProcessor(new TISEmailProcessor());
//        processor.registerMessageProcessor(new UZPLNEmailProcessor());
//        processor.registerMessageProcessor(new UZPLNParaEmailProcessor());
        idleImapReader.setProcessor(processor);
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
