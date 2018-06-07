package cz.cvut.kbss.reporting.service.cache;

import cz.cvut.kbss.reporting.dto.reportlist.OccurrenceReportDto;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.model.util.ReportLastModifiedComparator;
import cz.cvut.kbss.reporting.service.event.InvalidateCacheEvent;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ReportCacheTest {

    private ReportCache cache = new ReportCache();

    @Test
    public void getAllReturnsReportsOrderedByLastModifiedOrCreatedDateDescending() {
        final List<ReportDto> reports = generateReports();
        final List<ReportDto> toAdd = new ArrayList<>(reports);
        Collections.shuffle(toAdd); // ensure random insertion
        toAdd.forEach(cache::put);
        final List<ReportDto> res = cache.getAll();
        assertEquals(reports, res);
    }

    private List<ReportDto> generateReports() {
        final List<ReportDto> lst = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(5, 10); i++) {
            final Date date = new Date(System.currentTimeMillis() + i * 10000);
            final ReportDto dtoOne = new OccurrenceReportDto();
            dtoOne.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas#instance-" + i));
            dtoOne.setFileNumber((long) Generator.randomInt());
            dtoOne.setDateCreated(date);
            dtoOne.setLastModified(date);
            dtoOne.setRevision(1);
            lst.add(dtoOne);
        }
        // Ensures expected order
        Collections.reverse(lst);
        return lst;
    }

    @Test
    public void putReplacesOldRecord() {
        final ReportDto oldOne = new OccurrenceReportDto();
        oldOne.setFileNumber(System.currentTimeMillis());
        oldOne.setDateCreated(new Date());
        cache.put(oldOne);
        final ReportDto newOne = new OccurrenceReportDto();
        newOne.setFileNumber(oldOne.getFileNumber());
        newOne.setDateCreated(new Date(System.currentTimeMillis() + 10000));
        cache.put(newOne);

        final List<ReportDto> res = cache.getAll();
        assertEquals(1, res.size());
        assertEquals(newOne.getFileNumber(), res.get(0).getFileNumber());
        assertEquals(newOne.getDateCreated(), res.get(0).getDateCreated());
    }

    @Test
    public void evictRemovesAllRecords() {
        final List<ReportDto> lst = generateReports();
        lst.forEach(cache::put);
        assertFalse(cache.getAll().isEmpty());
        cache.evict();
        assertTrue(cache.getAll().isEmpty());
    }

    @Test
    public void evictByFileNumberRemovesRecordWithCorrespondingFileNumber() {
        final List<ReportDto> lst = generateReports();
        lst.forEach(cache::put);
        final ReportDto toRemove = Environment.randomElement(lst);
        assertTrue(cache.getAll().contains(toRemove));
        cache.evict(toRemove.getFileNumber());
        assertFalse(cache.getAll().contains(toRemove));
    }

    @Test
    public void invalidateCacheEventEvictsCache() {
        final List<ReportDto> lst = generateReports();
        lst.forEach(cache::put);
        assertFalse(cache.getAll().isEmpty());
        cache.onApplicationEvent(new InvalidateCacheEvent(this));
        assertTrue(cache.getAll().isEmpty());
    }

    @Test
    public void initializeSetsInitializedStatusOnCache() {
        final List<ReportDto> lst = generateReports();
        assertFalse(cache.isInitialized());
        cache.initialize(lst);
        assertTrue(cache.isInitialized());
        assertEquals(lst, cache.getAll());
    }

    @Test
    public void evictingCacheMakesItUninitialized() {
        final List<ReportDto> lst = generateReports();
        cache.initialize(lst);
        assertTrue(cache.isInitialized());
        cache.evict();
        assertFalse(cache.isInitialized());
    }

    @Test
    public void findAllPagedReturnsCorrespondingPage() {
        final List<ReportDto> lst = generateReports();
        cache.initialize(lst);
        lst.sort(new ReportLastModifiedComparator());
        final int page = 0;
        final int pageSize = lst.size() - 1;
        final Page<ReportDto> result = cache.getAll(PageRequest.of(page, pageSize));
        assertEquals(pageSize, result.getNumberOfElements());
        for (int i = 0; i < pageSize; i++) {
            assertEquals(lst.get(i), result.getContent().get(i));
        }
    }

    @Test
    public void findAllPagedReturnsIncompletePageIfThereAreNotEnoughElementsForFullPage() {
        final List<ReportDto> lst = generateReports();
        cache.initialize(lst);
        lst.sort(new ReportLastModifiedComparator());
        final int page = 1;
        final int pageSize = lst.size() - 1;
        final Page<ReportDto> result = cache.getAll(PageRequest.of(page, pageSize));
        assertEquals(1, result.getNumberOfElements());
        assertEquals(lst.get(lst.size() - 1), result.getContent().get(0));
        assertTrue(result.isLast());
        assertEquals(lst.size(), result.getTotalElements());
    }

    @Test
    public void findAllPagedReturnsEmptyListForFirstPageElementIndexLargerThanReportCount() {
        final List<ReportDto> lst = generateReports();
        cache.initialize(lst);
        final int pageSize = lst.size() / 2;
        final int page = pageSize * 3;
        final Page<ReportDto> result = cache.getAll(PageRequest.of(page, pageSize));
        assertTrue(result.getContent().isEmpty());
    }
}