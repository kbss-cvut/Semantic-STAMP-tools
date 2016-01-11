package cz.cvut.kbss.inbas.audit.service.repository;

import cz.cvut.kbss.inbas.audit.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.persistence.dao.*;
import cz.cvut.kbss.inbas.audit.service.InvestigationReportService;
import cz.cvut.kbss.inbas.audit.service.security.SecurityUtils;
import cz.cvut.kbss.inbas.audit.service.validation.Validator;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
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
    @Autowired
    private OccurrenceReportDao reportDao;

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
        investigation.setRootFactor(generateFactors(investigation, preliminaryReport.getTypeAssessments()));
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

    private Factor generateFactors(InvestigationReport report, Set<EventTypeAssessment> etas) {
        final Factor root = new Factor();
        root.setStartTime(report.getOccurrenceStart());
        root.setEndTime(report.getOccurrenceEnd());
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

    @Override
    public InvestigationReport createNewRevision(InvestigationReport report) {
        Objects.requireNonNull(report);

        final InvestigationReport newRevision = new InvestigationReport(report);
        newRevision.setAuthor(securityUtils.getCurrentUser());
        newRevision.setCreated(new Date());
        newRevision.setRevision(report.getRevision() + 1);
        copyFactors(report, newRevision);
        investigationReportDao.persist(newRevision);
        return newRevision;
    }

    private void copyFactors(InvestigationReport orig, InvestigationReport copy) {
        final Map<Factor, Factor> factorMapping = new HashMap<>();
        final Factor rootCopy = copyFactorHierarchy(orig.getRootFactor(), factorMapping);
        copyCausesAndMitigates(orig.getRootFactor(), rootCopy, factorMapping);
        copy.setRootFactor(rootCopy);
    }

    private Factor copyFactorHierarchy(Factor root, Map<Factor, Factor> factorMapping) {
        final Factor copy = new Factor(root);
        if (root.getChildren() != null) {
            root.getChildren().forEach(child -> copy.addChild(copyFactorHierarchy(child, factorMapping)));
        }
        factorMapping.put(root, copy);
        return copy;
    }

    private void copyCausesAndMitigates(Factor root, Factor rootCopy, Map<Factor, Factor> factorMapping) {
        if (root.getCauses() != null) {
            root.getCauses().forEach(cause -> rootCopy.addCause(factorMapping.get(cause)));
        }
        if (root.getMitigatingFactors() != null) {
            root.getMitigatingFactors()
                .forEach(mitigation -> rootCopy.addMitigatingFactor(factorMapping.get(mitigation)));
        }
        if (root.getChildren() != null) {
            root.getChildren().forEach(child -> copyCausesAndMitigates(child, factorMapping.get(child), factorMapping));
        }
    }

    @Override
    public List<ReportRevisionInfo> getRevisionsForOccurrence(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);

        return reportDao.getRevisionsForOccurrence(occurrence, Vocabulary.InvestigationReport);
    }
}
