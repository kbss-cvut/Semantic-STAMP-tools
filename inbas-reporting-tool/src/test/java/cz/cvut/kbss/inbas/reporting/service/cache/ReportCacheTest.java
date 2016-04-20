package cz.cvut.kbss.inbas.reporting.service.cache;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ReportCacheTest {

    private ReportCache cache = new ReportCache();

    @Test
    public void getAllReturnsReportsOrderedByCreatedDateAndRevisionNumberDescending() {
        final List<ReportDto> reports = generateReports();
        final List<ReportDto> toAdd = new ArrayList<>(reports);
        Collections.shuffle(toAdd); // ensure random insertion
        toAdd.forEach(cache::put);
        final List<ReportDto> res = cache.getAll();
        assertEquals(reports, res);
    }

    private List<ReportDto> generateReports() {
        final List<ReportDto> lst = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final Date date = new Date(System.currentTimeMillis() + i * 10000);
            final ReportDto dtoOne = new ReportDto();
            dtoOne.setFileNumber((long) Generator.randomInt());
            dtoOne.setDateCreated(date);
            dtoOne.setRevision(i + 1);
            final ReportDto dtoTwo = new ReportDto();
            dtoTwo.setFileNumber((long) Generator.randomInt());
            dtoTwo.setDateCreated(date);
            dtoTwo.setRevision(dtoOne.getRevision() + 1);
            lst.add(dtoOne);
            lst.add(dtoTwo);
        }
        // Ensures expected order
        Collections.reverse(lst);
        assertEquals(lst.get(0).getDateCreated(), lst.get(1).getDateCreated());
        assertTrue(lst.get(0).getRevision() > lst.get(1).getRevision());
        return lst;
    }

    @Test
    public void putReplacesOldRecord() {
        final ReportDto oldOne = new ReportDto();
        oldOne.setFileNumber(System.currentTimeMillis());
        oldOne.setDateCreated(new Date());
        cache.put(oldOne);
        final ReportDto newOne = new ReportDto();
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
}