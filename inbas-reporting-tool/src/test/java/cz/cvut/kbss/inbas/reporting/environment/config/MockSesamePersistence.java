package cz.cvut.kbss.inbas.reporting.environment.config;

import cz.cvut.kbss.inbas.reporting.persistence.sesame.DataDao;
import cz.cvut.kbss.inbas.reporting.persistence.sesame.SesamePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class MockSesamePersistence {

    @Bean
    public DataDao dataDao() {
        return mock(DataDao.class);
    }

    @Bean
    public SesamePersistenceProvider sesamePersistenceProvider() {
        return null;
    }
}
