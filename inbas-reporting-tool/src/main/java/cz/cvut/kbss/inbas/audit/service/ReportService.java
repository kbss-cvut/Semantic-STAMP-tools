package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.Report;

import java.net.URI;
import java.util.List;

public interface ReportService {

    List<OccurrenceReport> findAll();

    List<OccurrenceReport> findAll(String type);

    Report find(URI uri);

    Report findByKey(String key);

    List<ReportRevisionInfo> getReportChainRevisions(Long fileNumber);

    void persist(PreliminaryReport report);

    void update(PreliminaryReport report);

    void update(InvestigationReport report);

    <T extends Report> void remove(T report);
}
