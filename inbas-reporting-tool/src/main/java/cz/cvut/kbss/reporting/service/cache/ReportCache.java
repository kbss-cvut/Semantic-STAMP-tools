package cz.cvut.kbss.reporting.service.cache;

import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.model.util.ReportLastModifiedComparator;
import cz.cvut.kbss.reporting.service.event.InvalidateCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Map<Long, ReportDto> cache = new ConcurrentHashMap<>();

    private volatile boolean initialized = false;

    public void initialize(Collection<ReportDto> dtos) {
        Objects.requireNonNull(dtos);
        dtos.forEach(dto -> cache.put(dto.getFileNumber(), dto));
        this.initialized = true;
    }

    /**
     * Puts the specified report into the cache, possibly replacing its previous version.
     *
     * @param report The report to add
     */
    public void put(ReportDto report) {
        cache.put(report.getFileNumber(), report);
    }

    /**
     * Evicts the cache, i.e. removes all records.
     */
    public void evict() {
        cache.clear();
        this.initialized = false;
    }

    public void evict(Long fileNumber) {
        assert fileNumber != null;
        cache.remove(fileNumber);
    }

    /**
     * Gets the reports from this cache, ordered by date created and revision number, descending.
     *
     * @return List of cached reports
     */
    public List<ReportDto> getAll() {
        final List<ReportDto> reports = new ArrayList<>(cache.values());
        reports.sort(new ReportLastModifiedComparator());
        return reports;
    }

    /**
     * Gets a page of reports from this cache, ordered by date created and revision number, descending.
     *
     * @return List of cached reports
     */
    public Page<ReportDto> getAll(Pageable pageSpec) {
        final int start = pageSpec.getPageNumber() * pageSpec.getPageSize();
        if (start >= cache.size()) {
            return Page.empty(pageSpec);
        }
        final List<ReportDto> reports = getAll();
        final int end = Math.min(start + pageSpec.getPageSize(), reports.size());
        return new PageImpl<>(reports.subList(start, end), pageSpec, reports.size());
    }

    @Override
    public void onApplicationEvent(InvalidateCacheEvent invalidateCacheEvent) {
        LOG.trace("Invalidate cache event received. Evicting cache...");
        evict();
    }

    /**
     * Returns {@code true} if the cache is empty.
     * <p>
     * This can mean that no reports have been put into it, yet, or it has been evicted recently.
     *
     * @return Emptiness status
     */
    public boolean isInitialized() {
        return initialized;
    }
}
