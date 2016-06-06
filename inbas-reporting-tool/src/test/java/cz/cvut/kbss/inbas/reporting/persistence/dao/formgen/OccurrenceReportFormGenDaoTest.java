package cz.cvut.kbss.inbas.reporting.persistence.dao.formgen;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.PersonDao;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Test;
import org.openrdf.model.Resource;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class OccurrenceReportFormGenDaoTest extends BaseDaoTestRunner {

    @Autowired
    private OccurrenceReportFormGenDao dao;

    @Autowired
    private OccurrenceReportDao reportDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    @Qualifier("formGen")
    private EntityManagerFactory emf;

    @Test
    public void persistSavesNewReportIntoUniqueContext() throws Exception {
        final EntityManager em = emf.createEntityManager();
        RepositoryConnection connection = null;
        try {
            final Repository repository = em.unwrap(Repository.class);
            connection = repository.getConnection();
            assertFalse(connection.getContextIDs().hasNext());
            final OccurrenceReport report = Generator.generateOccurrenceReportWithFactorGraph();
            report.setFileNumber(null);
            report.setRevision(null);
            report.getAuthor().generateUri();
            final URI ctx = dao.persist(report);
            assertTrue(connection.getContextIDs().hasNext());
            final Set<URI> contexts = getContexts(connection);
            assertTrue(contexts.contains(ctx));
        } finally {
            em.close();
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Set<URI> getContexts(RepositoryConnection connection) throws Exception {
        final RepositoryResult<Resource> res = connection.getContextIDs();
        final Set<URI> set = new HashSet<>();
        while (res.hasNext()) {
            set.add(URI.create(res.next().stringValue()));
        }
        return set;
    }

    @Test
    public void persistSavesExistingReportIntoUniqueContext() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReportWithFactorGraph();
        personDao.persist(report.getAuthor());
        reportDao.persist(report);
        final URI ctx = dao.persist(report);
        assertNotNull(ctx);
    }

    @Test
    public void persistRemovesCorrectiveMeasuresFromReport() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReportWithFactorGraph();
        report.setCorrectiveMeasures(Generator.generateCorrectiveMeasureRequests());
        report.getAuthor().generateUri();
        assertFalse(report.getCorrectiveMeasures().isEmpty());
        dao.persist(report);
        assertTrue(report.getCorrectiveMeasures().isEmpty());
        final EntityManager em = emf.createEntityManager();
        try {
            assertFalse(
                    em.createNativeQuery("ASK { ?x a ?measure . }", Boolean.class).setParameter("measure", URI.create(
                            Vocabulary.s_c_corrective_measure_request)).getSingleResult());
        } finally {
            em.close();
        }
    }
}
