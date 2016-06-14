package cz.cvut.kbss.inbas.reporting.service.cache;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.util.DocumentDateAndRevisionComparator;
import cz.cvut.kbss.inbas.reporting.service.event.InvalidateCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A very simple cache for improving performance of access to the list of all reports.
 * <p>
 * The goal is to prevent reading the latest revisions of report chains from the storage when a list of reports, which
 * contains only a subset of each report's data, is requested. As long as the reports do not change, there is no reason
 * to read the data from the storage on every report list request.
 * <p>
 * We are using Spring events to listen to cache events throughout the application.
 */
@Service
public class ReportCache implements ApplicationListener<InvalidateCacheEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportCache.class);

    // Using synchronized methods for synchronizing access. If this proves to be a performance issue, other strategies
    // can be used.

    private final Map<Long, ReportDto> cache = new HashMap<>();

    /**
     * Puts the specified report into the cache, possibly replacing its previous version.
     *
     * @param report The report to add
     */
    public synchronized void put(ReportDto report) {
        cache.put(report.getFileNumber(), report);
    }

    /**
     * Evicts the cache, i.e. removes all records.
     */
    public synchronized void evict() {
        cache.clear();
    }

    public synchronized void evict(Long fileNumber) {
        assert fileNumber != null;
        cache.remove(fileNumber);
    }

    /**
     * Gets the reports from this cache, ordered by date created and revision number, descending.
     *
     * @return List of cached reports
     */
    public synchronized List<ReportDto> getAll() {
        final List<ReportDto> reports = new ArrayList<>(cache.values());
        Collections.sort(reports, new DocumentDateAndRevisionComparator());
        return reports;
    }

    @Override
    public void onApplicationEvent(InvalidateCacheEvent invalidateCacheEvent) {
        LOG.trace("Invalidate cache event received. Evicting cache...");
        evict();
    }
}
