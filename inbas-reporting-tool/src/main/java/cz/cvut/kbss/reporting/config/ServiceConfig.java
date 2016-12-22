package cz.cvut.kbss.reporting.config;

import cz.cvut.kbss.reporting.service.arms.ArmsService;
import cz.cvut.kbss.reporting.service.arms.ArmsServiceImpl;
import cz.cvut.kbss.reporting.service.data.eccairs.EccairsReportSynchronizationService;
import cz.cvut.kbss.reporting.service.data.mail.*;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:eccairs-config.properties")
@Import(ReportImportingConfig.class)
@ComponentScan(basePackages = "cz.cvut.kbss.reporting.service")
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

    @Bean
    public EccairsReportSynchronizationService eccairsReportSynchronizationService() {
        return new EccairsReportSynchronizationService();
    }
}
