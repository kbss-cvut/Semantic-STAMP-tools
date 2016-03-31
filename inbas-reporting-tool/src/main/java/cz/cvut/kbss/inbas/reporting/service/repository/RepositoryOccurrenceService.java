package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.model_new.Occurrence;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OwlKeySupportingDao;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
public class RepositoryOccurrenceService extends KeySupportingRepositoryService<Occurrence>
        implements OccurrenceService {

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private OccurrenceReportDao reportDao;

    @Override
    protected OwlKeySupportingDao<Occurrence> getPrimaryDao() {
        return occurrenceDao;
    }

    @Override
    public Collection<OccurrenceReport> getReports(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);
        return reportDao.findByOccurrence(occurrence);
    }
}
