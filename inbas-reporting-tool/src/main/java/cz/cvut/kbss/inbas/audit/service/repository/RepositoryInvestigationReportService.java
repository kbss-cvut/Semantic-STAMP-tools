package cz.cvut.kbss.inbas.audit.service.repository;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.persistence.dao.*;
import cz.cvut.kbss.inbas.audit.service.InvestigationReportService;
import cz.cvut.kbss.inbas.audit.service.security.SecurityUtils;
import cz.cvut.kbss.inbas.audit.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RepositoryInvestigationReportService extends BaseRepositoryService<InvestigationReport>
        implements InvestigationReportService {

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private Validator<ValidatableReport> reportValidator;

    @Autowired
    private OccurrenceDao occurrenceDao;
    @Autowired
    private CorrectiveMeasureDao correctiveMeasureDao;
    @Autowired
    private FactorDao factorDao;
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
        reportValidator.validate(preliminaryReport);
        final InvestigationReport investigation = new InvestigationReport(preliminaryReport);
        initBasicInfo(investigation);
        copyInitialReports(preliminaryReport, investigation);
        copyCorrectiveMeasures(preliminaryReport, investigation);
        investigation.setRootFactor(
                generateFactors(investigation.getOccurrence(), preliminaryReport.getTypeAssessments()));
        investigationReportDao.persist(investigation);
        occurrenceDao.update(investigation.getOccurrence());
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

    @Override
    public void update(InvestigationReport instance) {
        instance.setLastEdited(new Date());
        instance.setLastEditedBy(securityUtils.getCurrentUser());
        reportValidator.validate(instance);
        final InvestigationReport original = find(instance.getUri());
        super.update(instance);
        removeObsoleteFactors(original, instance);
        removeObsoleteCorrectiveMeasures(original, instance);
    }

    private void removeObsoleteFactors(InvestigationReport original, InvestigationReport update) {
        final Collection<Factor> factorsToRemove = new HashSet<>();
        getFactorsToRemove(original.getRootFactor(), update.getRootFactor(), factorsToRemove);
        if (!factorsToRemove.isEmpty()) {
            factorDao.remove(factorsToRemove);
        }
    }

    private void getFactorsToRemove(Factor originalRoot, Factor root, Collection<Factor> toRemove) {
        if (originalRoot.getChildren().isEmpty()) {
            return;
        }
        final Map<URI, Factor> updated = new HashMap<>(root.getChildren().size());
        for (Factor orig : root.getChildren()) {
            updated.put(orig.getUri(), orig);
        }
        for (Factor child : originalRoot.getChildren()) {
            if (!updated.containsKey(child.getUri())) {
                toRemove.add(child);
            } else {
                getFactorsToRemove(child, updated.get(child.getUri()), toRemove);
            }
        }
    }

    private void removeObsoleteCorrectiveMeasures(InvestigationReport original, InvestigationReport update) {
        if (original.getCorrectiveMeasures().isEmpty()) {
            return;
        }
        final Set<CorrectiveMeasure> toRemove = new HashSet<>();
        final Set<URI> uris = update.getCorrectiveMeasures().stream().map(CorrectiveMeasure::getUri)
                                    .collect(Collectors.toSet());
        toRemove.addAll(original.getCorrectiveMeasures().stream().filter(cm -> !uris.contains(cm.getUri())).collect(
                Collectors.toList()));
        correctiveMeasureDao.remove(toRemove);
    }
}
