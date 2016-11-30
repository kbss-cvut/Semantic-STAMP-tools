package cz.cvut.kbss.inbas.reporting.config;

import cz.cvut.kbss.inbas.reporting.service.arms.ArmsService;
import cz.cvut.kbss.inbas.reporting.service.arms.ArmsServiceImpl;
import cz.cvut.kbss.inbas.reporting.service.data.mail.EccairsReportImporter;
import cz.cvut.kbss.inbas.reporting.service.data.mail.EmailSourceService;
import cz.cvut.kbss.inbas.reporting.service.data.mail.ReportImporter;
import cz.cvut.kbss.inbas.reporting.service.data.mail.ReportImportingConfig;
import cz.cvut.kbss.inbas.reporting.service.data.mail.SafaImportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(ReportImportingConfig.class)
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.reporting.service")
@EnableScheduling
public class ServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ArmsService armsService() {
        return new ArmsServiceImpl();
    }

    @Bean
    public EmailSourceService emailSourceService() {
        return new EmailSourceService();
    }

    @Bean
    public ReportImporter reportImporter() {
        return new EccairsReportImporter();
    }
    
    @Bean
    public SafaImportService safaImportService() {
        return new SafaImportService();
    }
}
