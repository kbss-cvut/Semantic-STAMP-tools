package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssue;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.FactorGraphTraverser;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.IdentityBasedFactorGraphTraverser;
import cz.cvut.kbss.inbas.reporting.persistence.dao.util.FactorGraphOrphanRemover;
import cz.cvut.kbss.inbas.reporting.persistence.dao.util.FactorGraphSaver;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class SafetyIssueDao extends BaseDao<SafetyIssue> {

    public SafetyIssueDao() {
        super(SafetyIssue.class);
    }

    @Override
    protected void persist(SafetyIssue entity, final EntityManager em) {
        assert entity != null;
        final FactorGraphSaver saver = new FactorGraphSaver(em, null);
        final FactorGraphTraverser traverser = new IdentityBasedFactorGraphTraverser(saver, null);
        traverser.traverse(entity);
        em.persist(entity);
    }

    @Override
    protected void update(SafetyIssue entity, EntityManager em) {
        final SafetyIssue original = em.find(SafetyIssue.class, entity.getUri());
        new FactorGraphOrphanRemover(em).removeOrphans(original, entity);
        final FactorGraphSaver saver = new FactorGraphSaver(em, null);
        final FactorGraphTraverser traverser = new IdentityBasedFactorGraphTraverser(saver, null);
        traverser.traverse(entity);
        em.merge(entity);
    }
}
