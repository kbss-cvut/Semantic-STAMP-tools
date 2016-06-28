package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.commons.utils.PropertyUtils;
import cz.cvut.kbss.eccairs.schema.dao.SingeltonEccairsAccessFactory;
import cz.cvut.kbss.eccairs.schema.dao.cfg.EccairsAccessConfiguration;
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
@PropertySource("classpath:ib-caa-email-config.properties")
public class ReportImportingConfig {

    @Autowired
    protected Environment env;

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
                PropertyUtils.loadProperties("/eccairs-tools-config.properties"));
        eaf = new SingeltonEccairsAccessFactory(eac);
    }

    @Bean
    public SingeltonEccairsAccessFactory getEccairsAccessFactory() {
        return eaf;
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
