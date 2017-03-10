package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.reporting.model.Aircraft;
import cz.cvut.kbss.reporting.model.Occurrence;
import cz.cvut.kbss.reporting.model.util.factorgraph.traversal.FactorGraphTraverser;
import cz.cvut.kbss.reporting.model.util.factorgraph.traversal.IdentityBasedFactorGraphTraverser;
import cz.cvut.kbss.reporting.persistence.dao.util.FactorGraphOrphanRemover;
import cz.cvut.kbss.reporting.persistence.dao.util.FactorGraphSaver;
import cz.cvut.kbss.reporting.persistence.dao.util.QuestionSaver;
import cz.cvut.kbss.reporting.util.IdentificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OccurrenceDao extends OwlKeySupportingDao<Occurrence> {

    private OrganizationDao organizationDao;

    @Autowired
    public OccurrenceDao(OrganizationDao organizationDao) {
        super(Occurrence.class);
        this.organizationDao = organizationDao;
    }

    @Override
    protected void persist(Occurrence entity, EntityManager em) {
        assert entity != null;
        entity.setKey(IdentificationUtils.generateKey());
        final FactorGraphSaver saver = new FactorGraphSaver(em, new QuestionSaver());
        final FactorGraphTraverser traverser = new IdentityBasedFactorGraphTraverser(saver, null);
        traverser.traverse(entity);
        persistOrganizationIfNecessary(entity, em);
        em.persist(entity);
    }

    private void persistOrganizationIfNecessary(Occurrence entity, EntityManager em) {
        final Aircraft aircraft = entity.getAircraft();
        if (aircraft != null && !organizationDao.exists(aircraft.getOperator().getUri(), em)) {
            organizationDao.persist(aircraft.getOperator(), em);
        }
    }

    @Override
    protected void update(Occurrence entity, EntityManager em) {
        final Occurrence original = em.find(Occurrence.class, entity.getUri());
        new FactorGraphOrphanRemover(em).removeOrphans(original, entity);
        final FactorGraphSaver saver = new FactorGraphSaver(em, new QuestionSaver());
        final FactorGraphTraverser traverser = new IdentityBasedFactorGraphTraverser(saver, null);
        traverser.traverse(entity);
        persistOrganizationIfNecessary(entity, em);
        if (original.getAircraft() != null && entity.getAircraft() == null) {
            final Aircraft toRemove = original.getAircraft();
            em.remove(toRemove);
        }
        em.merge(entity);
    }
}
