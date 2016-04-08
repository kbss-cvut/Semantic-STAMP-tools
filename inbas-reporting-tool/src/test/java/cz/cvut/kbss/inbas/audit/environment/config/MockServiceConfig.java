package cz.cvut.kbss.inbas.audit.environment.config;

import cz.cvut.kbss.inbas.audit.service.*;
import cz.cvut.kbss.inbas.audit.service.data.FileDataLoader;
import cz.cvut.kbss.inbas.audit.service.data.RemoteDataLoader;
import cz.cvut.kbss.inbas.audit.service.security.PortalSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = {"cz.cvut.kbss.inbas.audit.service.options"})
public class MockServiceConfig {

    @Bean
    public InvestigationReportService investigationReportService() {
        return mock(InvestigationReportService.class);
    }

    @Bean
    public OccurrenceService occurrenceService() {
        return mock(OccurrenceService.class);
    }

    @Bean
    public OrganizationService organizationService() {
        return mock(OrganizationService.class);
    }

    @Bean
    public PersonService personService() {
        return mock(PersonService.class);
    }

    @Bean
    public PreliminaryReportService preliminaryReportService() {
        return mock(PreliminaryReportService.class);
    }

    @Bean
    public ReportService reportService() {
        return mock(ReportService.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return mock(UserDetailsService.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ConfigReader configReader() {
        return new ConfigReader();
    }

    @Bean(name = "localDataLoader")
    public FileDataLoader fileDataLoader() {
        return new FileDataLoader();
    }

    @Bean(name = "remoteDataLoader")
    public RemoteDataLoader remoteDataLoader() {
        return mock(RemoteDataLoader.class);
    }

    @Bean
    public StatisticsService statisticsService() {
        return mock(StatisticsService.class);
    }

    @Bean
    public PortalSessionManager portalSessionManager() {
        return mock(PortalSessionManager.class);
    }
}
