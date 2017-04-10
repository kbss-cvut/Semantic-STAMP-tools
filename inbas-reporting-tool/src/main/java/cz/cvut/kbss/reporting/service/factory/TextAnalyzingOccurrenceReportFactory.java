package cz.cvut.kbss.reporting.service.factory;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.kbss.reporting.exception.WebServiceIntegrationException;
import cz.cvut.kbss.reporting.model.InitialReport;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.textanalysis.ExtractedItem;
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

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Creates an {@link OccurrenceReport} based on analysis of the text of the specified initial report.
     *
     * @param initialReport The initial report to analyze
     * @return New occurrence report
     */
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
            final TextAnalysisResultWrapper result = restTemplate
                    .exchange(serviceUrl, HttpMethod.POST, new HttpEntity<>(taInput), TextAnalysisResultWrapper.class)
                    .getBody();
            attachTextAnalysisResultsToInitialReport(report.getInitialReport(), result);
            if (LOG.isTraceEnabled()) {
                LOG.trace("TextAnalysis result: {}.", result);
            }
        } catch (RestClientException e) {
            LOG.error("Error during analysis of the initial report.", e);
            throw new WebServiceIntegrationException("Unable to analyze initial report content.", e);
        }
    }

    private void attachTextAnalysisResultsToInitialReport(InitialReport report, TextAnalysisResultWrapper result) {
        final Double confidence = Double.parseDouble(result.confidence);
        report.setExtractedItems(
                result.getResults().stream().map(r -> new ExtractedItem(confidence, r.entityLabel, r.entityResource))
                      .collect(Collectors.toSet()));
    }

    static class TextAnalysisInput {
        private final String text;
        private List<String> vocabulary;

        TextAnalysisInput(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public List<String> getVocabulary() {
            return vocabulary;
        }

        public void setVocabulary(List<String> vocabulary) {
            this.vocabulary = vocabulary;
        }
    }

    static class TextAnalysisResultWrapper {

        private String confidence;
        @JsonProperty("stanbol")
        private List<TextAnalysisResult> results;

        public String getConfidence() {
            return confidence;
        }

        void setConfidence(String confidence) {
            this.confidence = confidence;
        }

        public List<TextAnalysisResult> getResults() {
            return results;
        }

        void setResults(List<TextAnalysisResult> results) {
            this.results = results;
        }

        @Override
        public String toString() {
            return "TextAnalysisResultWrapper{" +
                    "confidence='" + confidence + '\'' +
                    ", results=" + results +
                    '}';
        }
    }

    static class TextAnalysisResult {
        private String entityLabel;
        private URI entityResource;

        public String getEntityLabel() {
            return entityLabel;
        }

        void setEntityLabel(String entityLabel) {
            this.entityLabel = entityLabel;
        }

        public URI getEntityResource() {
            return entityResource;
        }

        void setEntityResource(URI entityResource) {
            this.entityResource = entityResource;
        }

        @Override
        public String toString() {
            return "{" +
                    "entityLabel='" + entityLabel + '\'' +
                    ", entityResource='" + entityResource + '\'' +
                    '}';
        }
    }
}
