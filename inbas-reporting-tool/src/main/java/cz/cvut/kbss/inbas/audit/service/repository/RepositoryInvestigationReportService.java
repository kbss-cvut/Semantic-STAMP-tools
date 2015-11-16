package cz.cvut.kbss.inbas.audit.service.repository;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.InvestigationReportDao;
import cz.cvut.kbss.inbas.audit.service.InvestigationReportService;
import cz.cvut.kbss.inbas.audit.service.security.SecurityUtils;
import cz.cvut.kbss.inbas.audit.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RepositoryInvestigationReportService extends BaseRepositoryService<InvestigationReport>
        implements InvestigationReportService {

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

    @Override
    public InvestigationReport findByKey(String key) {
        return investigationReportDao.findByKey(key);
    }

    @Override
    public InvestigationReport createFromPreliminaryReport(PreliminaryReport preliminaryReport) {
        preliminaryReportValidator.validate(preliminaryReport);
        final InvestigationReport investigation = new InvestigationReport(preliminaryReport);
        initBasicInfo(investigation);
        copyInitialReports(preliminaryReport, investigation);
        copyCorrectiveMeasures(preliminaryReport, investigation);
        investigation.setRootFactor(
                generateFactors(investigation.getOccurrence(), preliminaryReport.getTypeAssessments()));
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

    private Factor generateFactors(Occurrence occurrence, Set<EventTypeAssessment> etas) {
        final Factor root = new Factor();
        root.setStartTime(occurrence.getStartTime());
        root.setEndTime(occurrence.getEndTime());
        if (etas != null) {
            root.setChildren(new HashSet<>(etas.size()));
            for (EventTypeAssessment eta : etas) {
                final Factor child = new Factor(eta);
                child.setStartTime(root.getStartTime());
                child.setEndTime(root.getEndTime());
                root.addChild(child);
            }
        }
        return root;
    }
}
