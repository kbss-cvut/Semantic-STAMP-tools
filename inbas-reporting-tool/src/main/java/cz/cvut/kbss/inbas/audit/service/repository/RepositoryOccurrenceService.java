package cz.cvut.kbss.inbas.audit.service.repository;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.inbas.audit.service.OccurrenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RepositoryOccurrenceService extends BaseRepositoryService<Occurrence> implements OccurrenceService {

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private OccurrenceReportDao reportDao;

    @Override
    protected GenericDao<Occurrence> getPrimaryDao() {
        return occurrenceDao;
    }

    @Override
    public Occurrence findByKey(String key) {
        return occurrenceDao.findByKey(key);
    }

    @Override
    public Collection<OccurrenceReport> getReports(Occurrence occurrence) {
        return reportDao.findByOccurrence(occurrence);
    }

    @Override
    public Collection<OccurrenceReport> getReports(Occurrence occurrence, ReportingPhase phase) {
        Objects.requireNonNull(occurrence);
        Objects.requireNonNull(phase);

        final Collection<OccurrenceReport> reports = getReports(occurrence);
        return reports.stream().filter(occurrenceReport -> occurrenceReport.getPhase() == phase)
                      .collect(Collectors.toList());
    }
}
