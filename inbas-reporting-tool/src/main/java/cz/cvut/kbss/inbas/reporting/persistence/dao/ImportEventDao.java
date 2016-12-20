/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.repo.ImportEvent;
import cz.cvut.kbss.inbas.reporting.persistence.PersistenceException;
import static cz.cvut.kbss.inbas.reporting.persistence.dao.BaseDao.LOG;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.query.Query;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Repository
public class ImportEventDao extends BaseDao<ImportEvent>{
    
    public ImportEventDao() {
        super(ImportEvent.class);
    }
    
    public String getLatestEccairsReport(URI uri){
        Objects.requireNonNull(uri);
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT ?r { "
                    + "?im ?hasDate ?d. ?im ?hasTarget ?r.\n"
                    + "?r a ?reportType. ?r (?revisions)+ ?uri.\n"
                    + "FILTER NOT EXISTS{ ?r1 ?revisions ?r. FILTER (?r1 != ?r)}\n"
                    + "}ORDER BY DSEC(?d)\n LIMIT 1");
            q.setParameter("hasDate", URI.create(Vocabulary.s_p_has_start_time));
            q.setParameter("hasTarget", URI.create(Vocabulary.s_p_has_target));
            q.setParameter("reportType", URI.create(cz.cvut.kbss.eccairs.Vocabulary.s_c_report_from_eccairs));
            q.setParameter("revisions", URI.create(cz.cvut.kbss.eccairs.Vocabulary.s_p_is_revision_of));
            q.setParameter("uri", uri);
            
            List results = q.getResultList();
            if(results != null && !results.isEmpty()){
                return results.get(0).toString();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when finding latest import from eccairs by key.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
        return null;
    }
    
    public String getLatestEccairsReportByEccairsKey(String uri, String eccairsKey){
        Objects.requireNonNull(eccairsKey);
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT ?r { "
                    + "?im ?hasDate ?d. ?im ?hasTarget ?r.\n"
                    + "FILTER(?r != ?uri)"
                    + "?r ?hasKey ?key. }ORDER BY DSEC(?d)\n LIMIT 1");
            q.setParameter("hasDate", URI.create(Vocabulary.s_p_has_start_time));
            q.setParameter("hasTarget", URI.create(Vocabulary.s_p_has_target));
            q.setParameter("uri", URI.create(uri));
            q.setParameter("hasKey", URI.create(cz.cvut.kbss.eccairs.Vocabulary.hasId));
            q.setParameter("key", eccairsKey);
            List results = q.getResultList();
            if(results != null && !results.isEmpty()){
                return results.get(0).toString();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when finding latest import from eccairs by key.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
        return null;
    }
    
}
