package cz.cvut.kbss.reporting.service.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorFactory {

    @Bean
    public ReportValidator reportValidator() {
        return new ReportValidator();
    }

    @Bean
    public AircraftValidator aircraftValidator() {
        return new AircraftValidator();
    }

    @Bean
    public OccurrenceValidator occurrenceValidator(AircraftValidator aircraftValidator) {
        return new OccurrenceValidator(aircraftValidator);
    }

    @Bean
    public OccurrenceReportValidator occurrenceReportValidator(ReportValidator reportValidator,
                                                               OccurrenceValidator occurrenceValidator) {
        return new OccurrenceReportValidator(reportValidator, occurrenceValidator);
    }

    @Bean
    public AuditReportValidator auditReportValidator(ReportValidator reportValidator) {
        return new AuditReportValidator(reportValidator);
    }
}
