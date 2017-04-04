package cz.cvut.kbss.reporting.service.factory;

import cz.cvut.kbss.reporting.exception.WebServiceIntegrationException;
import cz.cvut.kbss.reporting.model.InitialReport;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.util.ConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TextAnalyzingOccurrenceReportFactory extends DefaultOccurrenceReportFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TextAnalyzingOccurrenceReportFactory.class);

    private final RestTemplate restTemplate;

    private final Environment environment;

    @Autowired
    public TextAnalyzingOccurrenceReportFactory(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    @Override
    public OccurrenceReport createFromInitialReport(InitialReport initialReport) {
        final OccurrenceReport report = super.createFromInitialReport(initialReport);
        analyze(report);
        return report;
    }

    private void analyze(OccurrenceReport report) {
        final TextAnalysisInput taInput = new TextAnalysisInput(report.getInitialReport().getDescription());
        final String serviceUrl = environment.getProperty(ConfigParam.TEXT_ANALYSIS_SERVICE_URL.toString(), "");
        if (serviceUrl.isEmpty()) {
            return;
        }
        try {
            restTemplate.exchange(serviceUrl, HttpMethod.POST, new HttpEntity<>(taInput), String.class).getBody();
        } catch (RestClientException e) {
            LOG.error("Error during analysis of the initial report.", e);
            throw new WebServiceIntegrationException("Unable to analyze initial report content.", e);
        }
    }

    static class TextAnalysisInput {
        private final String text;
        private List<String> vocabularies;

        TextAnalysisInput(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public List<String> getVocabularies() {
            return vocabularies;
        }

        public void setVocabularies(List<String> vocabularies) {
            this.vocabularies = vocabularies;
        }
    }
}
