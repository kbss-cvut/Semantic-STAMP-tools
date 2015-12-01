package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class OccurrenceReportDao extends BaseDao<OccurrenceReport>
        implements GenericDao<OccurrenceReport>, SupportsOwlKey<OccurrenceReport> {

    public OccurrenceReportDao() {
        super(OccurrenceReport.class);
    }

    @Override
    public void persist(OccurrenceReport entity) {
        throw new UnsupportedOperationException("Persist is not supported for OccurrenceReports.");
    }

    @Override
    public void persist(Collection<OccurrenceReport> entities) {
        throw new UnsupportedOperationException("Persist is not supported for OccurrenceReports.");
    }

    @Override
    public void update(OccurrenceReport entity) {
        throw new UnsupportedOperationException("Update is not supported for OccurrenceReports.");
    }

    @Override
    public void remove(OccurrenceReport entity) {
        throw new UnsupportedOperationException("Remove is not supported for OccurrenceReports.");
    }

    @Override
    public void remove(Collection<OccurrenceReport> entities) {
        throw new UnsupportedOperationException("Remove is not supported for OccurrenceReports.");
    }
}
