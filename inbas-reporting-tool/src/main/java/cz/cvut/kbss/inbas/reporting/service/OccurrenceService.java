package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.model_new.Occurrence;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;

import java.util.Collection;

public interface OccurrenceService extends BaseService<Occurrence> {

    /**
     * Finds occurrence with the specified key.
     *
     * @param key Occurrence key
     * @return Matching occurrence instance or {@code null}, if none exists
     */
    Occurrence findByKey(String key);

    /**
     * Gets reports related to the specified occurrence.
     *
     * @param occurrence Occurrence to find reports for
     * @return Collection of matching reports (possibly empty)
     */
    Collection<OccurrenceReport> getReports(Occurrence occurrence);
}
