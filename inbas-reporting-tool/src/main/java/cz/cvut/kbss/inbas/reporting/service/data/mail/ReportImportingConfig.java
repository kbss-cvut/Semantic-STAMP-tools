package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.commons.utils.PropertyUtils;
import cz.cvut.kbss.eccairs.schema.dao.SingeltonEccairsAccessFactory;
import cz.cvut.kbss.eccairs.schema.dao.cfg.EccairsAccessConfiguration;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.service.PersonService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Configuration
@Import(EmailSourceConfig.class)
@PropertySource("classpath:email-config.properties")
public class ReportImportingConfig {

    private static final String IMPORTER_USERNAME = "e5xml-data-importer-0001";
    private static final String IMPORTER_URI = "http://onto.fel.cvut.cz/ointologies/tools/e5xml-data-importer-0001";
    
    @Autowired
    protected Environment env;
    
    @Autowired
    protected PersonService personService;

    @PostConstruct
    protected void init() {
        constructSesameUpdater();
        constructEccairsAccessFactory();
//        constructE5XMLLoader();
    }

    // Eccairs Access Factory
    protected SingeltonEccairsAccessFactory eaf;

    protected void constructEccairsAccessFactory() {
        EccairsAccessConfiguration eac = new EccairsAccessConfiguration(
                PropertyUtils.loadProperties("/eccairs-e5xml-parser.properties"));
        eaf = new SingeltonEccairsAccessFactory(eac);
    }

    @Bean
    public SingeltonEccairsAccessFactory getEccairsAccessFactory() {
        return eaf;
    }
    
    
    @Bean(name = "importer")
    public Person getImporter(){
        Person importer = personService.findByUsername(IMPORTER_USERNAME);
        if (importer == null) {
            importer = new Person();
            importer.setUri(URI.create(IMPORTER_URI));
            importer.setUsername(IMPORTER_USERNAME);
            importer.setFirstName("importer");
            importer.setLastName("0001");
            importer.setPassword("Importer0001");
            personService.persist(importer);
        }
        return importer;
    }

//    // Eccairs E5XML loader
//    protected E5XMLLoader loader;
//
//    protected void constructE5XMLLoader() {
//        loader = new E5XMLLoader();
//        E5XXMLParser parser = new E5XXMLParser(getEccairsAccessFactory());
//        loader.setE5xParser(parser);
//    }

//    @Bean
//    public E5XMLLoader getE5XMLLoader() {
//        return loader;
//    }

    // Sesame updater
    protected SesameUpdater updater;

    private void constructSesameUpdater() {
        updater = new SesameUpdater();
    }

    @Bean
    public SesameUpdater getSesameUpdater() {
        return updater;
    }
}
