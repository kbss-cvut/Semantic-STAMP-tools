package cz.cvut.kbss.inbas.reporting.service.formgen;

import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.formgen.FormGenDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.formgen.OccurrenceReportFormGenDao;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.data.DataLoader;
import cz.cvut.kbss.inbas.reporting.util.ConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class FormGenServiceImpl implements FormGenService {

    private static final Logger LOG = LoggerFactory.getLogger(FormGenServiceImpl.class);

    static final String REPOSITORY_URL_PARAM = "repositoryUrl";
    static final String CONTEXT_URI_PARAM = "context";

    @Autowired
    private OccurrenceReportFormGenDao occurrenceReportFormGenDao;

    @Autowired
    @Qualifier("remoteDataLoader")
    private DataLoader dataLoader;

    @Autowired
    private Environment environment;

    private final Map<Class<?>, FormGenDao<?>> daoMap = new HashMap<>(2);

    @PostConstruct
    private void registerDaos() {
        daoMap.put(OccurrenceReport.class, occurrenceReportFormGenDao);
    }

    @Override
    public <T> RawJson generateForm(T data) {
        final FormGenDao<T> dao = resolveDao(data);
        final URI context = dao.persist(data);
        return loadFormStructure(context);
    }

    private <T> FormGenDao<T> resolveDao(Object data) {
        if (!daoMap.containsKey(data.getClass())) {
            throw new IllegalArgumentException("Unsupported data type for form generation.");
        }
        return (FormGenDao<T>) daoMap.get(data.getClass());
    }

    private RawJson loadFormStructure(URI context) {
        final String serviceUrl = environment.getProperty(ConfigParam.FORM_GEN_SERVICE_URL.toString(), "");
        final String repoUrl = environment.getProperty(ConfigParam.FORM_GEN_REPOSITORY_URL.toString(), "");
        if (serviceUrl.isEmpty() || repoUrl.isEmpty()) {
            LOG.error("Form generator service URL or repository URL is missing. Service URL: {}, repository URL: {}.",
                    serviceUrl, repoUrl);
            return new RawJson("");
        }
        final Map<String, String> params = new HashMap<>();
        params.put(CONTEXT_URI_PARAM, context.toString());
        params.put(REPOSITORY_URL_PARAM, repoUrl);
        return new RawJson(dataLoader.loadData(serviceUrl, params));
    }
}
