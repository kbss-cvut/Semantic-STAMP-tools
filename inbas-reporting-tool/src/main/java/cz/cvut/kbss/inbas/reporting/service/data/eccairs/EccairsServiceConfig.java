package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import cz.cvut.kbss.eccairs.webapi.EwaIWebServer;
import cz.cvut.kbss.eccairs.webapi.EwaResult;
import cz.cvut.kbss.eccairs.webapi.EwaWebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @author Petr Křemen
 */
@Configuration
@PropertySource("classpath:eccairs-config.properties")
public class EccairsServiceConfig {

    private static final Logger LOG = LoggerFactory.getLogger(EccairsServiceConfig.class);

    @Autowired
    protected Environment env;

    @PostConstruct
    protected void init() {
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();

        final String pRepositoryId=env.getProperty("eccairs.repository.id");
        final String pLanguage=env.getProperty("eccairs.repository.language");
        final String pUsername=env.getProperty("eccairs.repository.username");
        final String pPassword=env.getProperty("eccairs.repository.password");
        EwaIWebServer service = new EwaWebServer().getBasicHttpBindingEwaIWebServer();

        LOG.info("ECCAIRS Service Connected: ",service.about().getData().getValue());

        EwaResult login=service.login(
                pRepositoryId,
                pUsername,
                pPassword,
                pLanguage
        );

        LOG.info("ECCAIRS Service Login Successful: ",login.getData().getValue());
        String token = login.getUserToken().getValue();

        EwaResult result = service.executeQueryAsTable(token, "CTU Library", "ALL", 0);
        LOG.info("ECCAIRS Service Login Successful: ",login.getData().getValue());
        System.out.println(result.getData().getValue());
    }

}
