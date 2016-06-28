package cz.cvut.kbss.inbas.reporting.environment.config;

import cz.cvut.kbss.inbas.reporting.persistence.TestEccairsReportImportPersistenceFactory;
import cz.cvut.kbss.inbas.reporting.persistence.TestFormGenPersistenceFactory;
import cz.cvut.kbss.inbas.reporting.persistence.TestPersistenceFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"cz.cvut.kbss.inbas.reporting.persistence.dao",
        "cz.cvut.kbss.inbas.reporting.persistence.sesame"})
@Import({TestPersistenceFactory.class,
        TestFormGenPersistenceFactory.class,
        TestEccairsReportImportPersistenceFactory.class})
public class TestPersistenceConfig {
}
