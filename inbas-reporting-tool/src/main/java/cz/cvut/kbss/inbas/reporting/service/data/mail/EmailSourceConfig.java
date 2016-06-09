package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.datatools.mail.CompuondProcessor;
import cz.cvut.kbss.datatools.mail.caa.e5xml.E5XMLProcessor;
import cz.cvut.kbss.datatools.mail.imap.idle.IDLEMailMessageReader;
import cz.cvut.kbss.datatools.mail.model.javaapi.MessageWrapperJavaAPI;
import cz.cvut.kbss.inbas.reporting.model.com.EMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.Properties;
import java.util.stream.Stream;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Configuration
@PropertySource("classpath:ib-caa-email-config.properties")
public class EmailSourceConfig {

    protected static final String MAIL_SERVER = "mail.server";
    protected static final String MAIL_PROTOCOL = "mail.store.protocol";
    protected static final String MAIL_SOCKET_FACTORY_CLASS = "mail.imap.socketFactory.class";
    protected static final String MAIL_SOCKET_FACTORY_FALLBACK = "mail.imap.socketFactory.fallback";
    protected static final String MAIL_SOCKET_FACTORY_PORT = "mail.imap.socketFactory.port";
    protected static final String MAIL_USER = "mail.user";
    protected static final String MAIL_PASSWORD = "mail.password";
    protected static final String MAIL_FOLDER = "mail.folder";

    @Autowired
    protected Environment env;

    @Autowired
    protected EMailService emailService;

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
                return emailService.getMailById(id) != null;
            }

            @Override
            protected boolean setProcessed(String id) {
                EMail email = emailService.getMailById(id);
                if (email == null) {
                    email = new EMail();
                    email.setId(id);
                    emailService.persist(email);
                }
                return false;
            }

//            @Override
//            public void processOldMessages(Folder f) throws MessagingException { // FOR DEBUG
//                Message[] ms = new Message[]{};
//                if (f.isOpen()) {
//                    ms = f.getMessages();
//                } else {
//                    f.open(Folder.READ_ONLY);
//                    ms = f.getMessages();
//                    f.close(false);
//                }
//
//                for (Message m : ms) {
//                    MessageWrapperJavaAPI mw = new MessageWrapperJavaAPI(m);
//                    String id = mw.getId();
//                    if (!isProcessed(id)) {
//                        processMessageImpl(m, mw);
//                    }
//                    break;// DEBUG
//                }
//            }

            @Override
            protected Object processMessage(MessageWrapperJavaAPI m) throws MessagingException {
                Object o = super.processMessage(m); //To change body of generated methods, choose Tools | Templates.=
                if (o != null && o instanceof Stream) {
                    Stream s = (Stream) o;
                    s.forEach(importer::processDelegate);
                } else {
                    importer.processDelegate(o);
                }
                return o;
            }
        };
        CompuondProcessor processor = new CompuondProcessor();
        processor.registerMessageProcessor(new E5XMLProcessor());
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
