package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;

import java.net.URI;
import java.util.Collection;

public interface OccurrenceReportService {

    Collection<OccurrenceReport> findAll();

    /**
     * Gets occurrence reports of the specified type (OWL class).
     * <p>
     * If the type is not specified, this method behaves as {@link #findAll()} and returns all available reports.
     *
     * @param type Report ontological type
     * @return Collection of occurrence reports
     */
    Collection<OccurrenceReport> findAll(String type);

    OccurrenceReport find(URI uri);

    OccurrenceReport findByKey(String key);

    void remove(OccurrenceReport report);
}
