package cz.cvut.kbss.inbas.audit.service.repository;

import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.inbas.audit.service.InvestigationReportService;
import cz.cvut.kbss.inbas.audit.service.OccurrenceReportService;
import cz.cvut.kbss.inbas.audit.service.PreliminaryReportService;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class RepositoryOccurrenceReportService extends BaseRepositoryService<OccurrenceReport>
        implements OccurrenceReportService {

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Autowired
    private PreliminaryReportService preliminaryReportService;
    @Autowired
    private InvestigationReportService investigationReportService;

    @Override
    protected GenericDao<OccurrenceReport> getPrimaryDao() {
        return occurrenceReportDao;
    }

    @Override
    public Collection<OccurrenceReport> findAll(String type) {
        if (type == null) {
            return findAll();
        }
        return occurrenceReportDao.findAll(type);
    }

    @Override
    public OccurrenceReport findByKey(String key) {
        Objects.requireNonNull(key);
        return occurrenceReportDao.findByKey(key);
    }

    @Override
    public void remove(OccurrenceReport instance) {
        Objects.requireNonNull(instance);
        if (isPreliminary(instance)) {
            removePreliminaryReport(instance);
        } else if (isInvestigation(instance)) {
            removeInvestigationReport(instance);
        }
    }

    private boolean isPreliminary(OccurrenceReport instance) {
        return instance.getTypes().contains(Vocabulary.PreliminaryReport);
    }

    private void removePreliminaryReport(OccurrenceReport instance) {
        final PreliminaryReport pr = preliminaryReportService.find(instance.getUri());
        if (pr != null) {
            preliminaryReportService.remove(pr);
        }
    }

    private boolean isInvestigation(OccurrenceReport instance) {
        return instance.getTypes().contains(Vocabulary.InvestigationReport);
    }

    private void removeInvestigationReport(OccurrenceReport instance) {
        final InvestigationReport ir = investigationReportService.find(instance.getUri());
        if (ir != null) {
            investigationReportService.remove(ir);
        }
    }

    @Override
    public void remove(Collection<OccurrenceReport> instances) {
        Objects.requireNonNull(instances);
        if (instances.isEmpty()) {
            return;
        }
        final List<OccurrenceReport> preliminaryReports = new ArrayList<>(instances.size());
        final List<OccurrenceReport> investigationReports = new ArrayList<>(instances.size());
        for (OccurrenceReport or : instances) {
            if (isPreliminary(or)) {
                preliminaryReports.add(or);
            } else if (isInvestigation(or)) {
                investigationReports.add(or);
            }
        }
        preliminaryReports.forEach(this::removePreliminaryReport);
        investigationReports.forEach(this::removeInvestigationReport);
    }
}
