package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;

import java.net.URI;
import java.util.Collection;

public interface OccurrenceReportService {

    Collection<OccurrenceReport> findAll();

    OccurrenceReport find(URI uri);

    OccurrenceReport findByKey(String key);

    void remove(OccurrenceReport report);
}
