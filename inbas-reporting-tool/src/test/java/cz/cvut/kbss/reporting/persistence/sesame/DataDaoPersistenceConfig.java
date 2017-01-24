package cz.cvut.kbss.reporting.persistence.sesame;

import cz.cvut.kbss.reporting.persistence.TestEccairsReportImportPersistenceFactory;
import cz.cvut.kbss.reporting.persistence.TestFormGenPersistenceFactory;
import cz.cvut.kbss.reporting.persistence.TestPersistenceFactory;
import cz.cvut.kbss.reporting.persistence.dao.formgen.OccurrenceReportFormGenDao;
import org.springframework.context.annotation.*;

import static org.mockito.Mockito.spy;

@Configuration
@ComponentScan(basePackages = {"cz.cvut.kbss.reporting.persistence.dao"})
@Import({TestPersistenceFactory.class,
        TestFormGenPersistenceFactory.class,
        TestEccairsReportImportPersistenceFactory.class})
public class DataDaoPersistenceConfig {

    @Bean
    @Primary
    public SesamePersistenceProvider sesamePersistenceProvider() {
        return new TestSesamePersistenceProvider();
    }

    @Bean
    public DataDao dataDao() {
        return new DataDao();
    }

    @Bean
    public OccurrenceReportFormGenDao occurrenceReportFormGenDao() {
        return spy(new OccurrenceReportFormGenDao());
    }
}
