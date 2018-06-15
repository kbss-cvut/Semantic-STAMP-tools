package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.model.Event;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.persistence.BaseDaoTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class ExistingDataDaoTest extends BaseDaoTestRunner {

    @Autowired
    private ExistingDataDao dao;

    @Autowired
    private OccurrenceReportDao reportDao;

    @Test
    public void getUsedOccurrenceCategoriesRetrievesCategoriesUsedByOccurrencesInRepository() {
        final Person author = Generator.getPerson();
        persistPerson(author);
        final List<OccurrenceReport> reports = OccurrenceReportGenerator.generateReports(true, 10);
        reports.forEach(r -> r.setAuthor(author));
        reportDao.persist(reports);
        final Set<URI> occurrenceCats = reports.stream().map(r -> r.getOccurrence().getEventType())
                                               .collect(Collectors.toSet());

        final Set<URI> result = dao.getUsedOccurrenceCategories();
        assertEquals(occurrenceCats, result);
    }

    @Test
    public void getUsedOccurrenceCategoriesReturnsEmptySetForEmptyRepository() {
        final Set<URI> result = dao.getUsedOccurrenceCategories();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getUsedEventTypesRetrievesEventTypesUsedByEventsInRepository() {
        final Person author = Generator.getPerson();
        persistPerson(author);
        final List<OccurrenceReport> reports = IntStream.range(0, 5).mapToObj(i -> {
            final OccurrenceReport r = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
            r.setAuthor(author);
            return r;
        }).collect(Collectors.toList());
        reportDao.persist(reports);
        final Set<URI> eventTypes = resolveEventTypes(reports);

        final Set<URI> result = dao.getUsedEventTypes();
        assertEquals(eventTypes, result);
    }

    private static Set<URI> resolveEventTypes(List<OccurrenceReport> reports) {
        final Set<URI> eventTypes = new HashSet<>();
        for (OccurrenceReport report : reports) {
            final Queue<Event> q = new ArrayDeque<>(report.getOccurrence().getChildren());
            Event e;
            while ((e = q.poll()) != null) {
                if (e.getChildren() != null) {
                    q.addAll(e.getChildren());
                }
                eventTypes.add(e.getEventType());
            }
        }
        return eventTypes;
    }

    @Test
    public void getUsedEventTypesReturnsEmptySetForNoExistingEventsInRepository() {
        final Person author = Generator.getPerson();
        persistPerson(author);
        final List<OccurrenceReport> reports = OccurrenceReportGenerator.generateReports(true, 5);
        reports.forEach(r -> r.setAuthor(author));
        reportDao.persist(reports);

        final Set<URI> result = dao.getUsedEventTypes();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}