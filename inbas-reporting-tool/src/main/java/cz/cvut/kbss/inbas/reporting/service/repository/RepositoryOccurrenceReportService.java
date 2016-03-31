package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RepositoryOccurrenceReportService extends BaseRepositoryService<OccurrenceReport>
        implements OccurrenceReportService {

    @Autowired
    private OccurrenceReportDao reportDao;

    @Override
    protected GenericDao<OccurrenceReport> getPrimaryDao() {
        return reportDao;
    }

    @Override
    public OccurrenceReport createNewRevision(Long fileNumber) {
        // TODO
        return null;
    }

    @Override
    public OccurrenceReport findLatestRevision(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        return reportDao.findLatestRevision(fileNumber);
    }

    @Override
    public OccurrenceReport findRevision(Long fileNumber, Integer revision) {
        Objects.requireNonNull(fileNumber);
        Objects.requireNonNull(revision);
        return reportDao.findRevision(fileNumber, revision);
    }
}
