package cz.cvut.kbss.reporting.environment.config;

import cz.cvut.kbss.reporting.service.OccurrenceReportService;
import cz.cvut.kbss.reporting.service.data.export.ReportExporter;
import cz.cvut.kbss.reporting.service.jmx.AppAdminBean;
import cz.cvut.kbss.reporting.service.repository.RepositoryOccurrenceReportService;
import cz.cvut.kbss.reporting.service.security.LoginTracker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.reporting.service")
public class TestServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OccurrenceReportService occurrenceReportService() {
        return spy(new RepositoryOccurrenceReportService());
    }

    @Bean
    public ReportExporter reportExporter() {
        return mock(ReportExporter.class);
    }

    @Bean
    public LoginTracker loginTracker() {
        return mock(LoginTracker.class);
    }

    @Bean
    public AppAdminBean appAdminBean() {
        return mock(AppAdminBean.class);
    }
}
