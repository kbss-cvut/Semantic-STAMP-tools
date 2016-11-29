package cz.cvut.kbss.inbas.reporting.environment.config;

import cz.cvut.kbss.inbas.reporting.service.OccurrenceReportService;
import cz.cvut.kbss.inbas.reporting.service.arms.ArmsService;
import cz.cvut.kbss.inbas.reporting.service.data.eccairs.EccairsService;
import cz.cvut.kbss.inbas.reporting.service.data.mail.EmailSourceService;
import cz.cvut.kbss.inbas.reporting.service.data.mail.ReportImporter;
import cz.cvut.kbss.inbas.reporting.service.repository.RepositoryOccurrenceReportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.reporting.service")
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
    public ArmsService armsService() {
        return mock(ArmsService.class);
    }

    @Bean
    public EmailSourceService emailSourceService() {
        return mock(EmailSourceService.class);
    }

    @Bean
    @Primary
    public ReportImporter reportImporter() {
        return mock(ReportImporter.class);
    }

    @Bean
    @Primary
    public EccairsService eccairsService() {
        return mock(EccairsService.class);
    }
}
