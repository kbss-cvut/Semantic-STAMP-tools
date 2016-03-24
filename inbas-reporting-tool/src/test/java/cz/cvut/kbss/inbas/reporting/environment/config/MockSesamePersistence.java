package cz.cvut.kbss.inbas.reporting.environment.config;

import cz.cvut.kbss.inbas.reporting.persistence.sesame.DataDao;
import org.openrdf.repository.Repository;
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
    public Repository repository() {
        return mock(Repository.class);
    }
}
