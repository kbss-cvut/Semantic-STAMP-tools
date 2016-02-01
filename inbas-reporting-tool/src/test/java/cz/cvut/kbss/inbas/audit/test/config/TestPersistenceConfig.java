package cz.cvut.kbss.inbas.audit.test.config;

import cz.cvut.kbss.inbas.audit.persistence.TestPersistenceFactory;
import cz.cvut.kbss.inbas.audit.persistence.sesame.DataDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit.persistence.dao")
@Import({TestPersistenceFactory.class})
public class TestPersistenceConfig {

    @Bean
    public DataDao dataDao() {
        return mock(DataDao.class);
    }
}
