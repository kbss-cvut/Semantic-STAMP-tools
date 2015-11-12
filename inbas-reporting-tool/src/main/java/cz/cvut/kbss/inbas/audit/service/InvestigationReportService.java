package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.reports.CorrectiveMeasure;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.InvestigationReportDao;
import cz.cvut.kbss.inbas.audit.service.security.SecurityUtils;
import cz.cvut.kbss.inbas.audit.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InvestigationReportService extends BaseService<InvestigationReport> {

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private Validator<PreliminaryReport> preliminaryReportValidator;

    @Autowired
    private InvestigationReportDao investigationReportDao;

    @Override
    protected GenericDao<InvestigationReport> getPrimaryDao() {
        return investigationReportDao;
    }

    @Override
    public void persist(InvestigationReport instance) {
        throw new UnsupportedOperationException(
                "Cannot persist investigation report. Need to create one from a preliminary report.");
    }

    @Override
    public void persist(Collection<InvestigationReport> instances) {
        throw new UnsupportedOperationException(
                "Cannot persist investigation report. Need to create one from a preliminary report.");
    }

    // Add update

    /**
     * Creates and persists a new investigation report from the specified preliminary report.
     *
     * @param preliminaryReport Base for the new report
     * @return The created report
     */
    public InvestigationReport createFromPreliminaryReport(PreliminaryReport preliminaryReport) {
        preliminaryReportValidator.validate(preliminaryReport);
        final InvestigationReport investigation = new InvestigationReport(preliminaryReport);
        initBasicInfo(investigation);
        copyInitialReports(preliminaryReport, investigation);
        copyCorrectiveMeasures(preliminaryReport, investigation);
        // TODO
        investigationReportDao.persist(investigation);
        return investigation;
    }

    private void initBasicInfo(InvestigationReport investigation) {
        investigation.setAuthor(securityUtils.getCurrentUser());
        investigation.setCreated(new Date());
    }

    private void copyInitialReports(PreliminaryReport source, InvestigationReport target) {
        if (source.getInitialReports().isEmpty()) {
            return;
        }
        target.setInitialReports(copy(source.getInitialReports(), InitialReport::new));
    }

    private void copyCorrectiveMeasures(PreliminaryReport source, InvestigationReport target) {
        if (source.getCorrectiveMeasures().isEmpty()) {
            return;
        }
        target.setCorrectiveMeasures(copy(source.getCorrectiveMeasures(), CorrectiveMeasure::new));
    }

    private <T> Set<T> copy(Set<T> source, Function<T, T> copyFunction) {
        return source.stream().map(copyFunction).collect(Collectors.toSet());
    }
}
