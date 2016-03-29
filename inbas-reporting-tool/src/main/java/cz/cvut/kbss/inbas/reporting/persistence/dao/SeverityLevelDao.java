package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model_new.SeverityLevel;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * All modification methods throw {@link UnsupportedOperationException}, because severity levels should be predefined in
 * the ontology and only referenced by the application.
 */
@Repository
public class SeverityLevelDao extends BaseDao<SeverityLevel> {

    public SeverityLevelDao() {
        super(SeverityLevel.class);
    }

    @Override
    public void persist(SeverityLevel entity) {
        throw new UnsupportedOperationException("SeverityLevels cannot be persisted.");
    }

    @Override
    protected void persist(SeverityLevel entity, EntityManager em) {
        throw new UnsupportedOperationException("SeverityLevels cannot be persisted.");
    }

    @Override
    public void persist(Collection<SeverityLevel> entities) {
        throw new UnsupportedOperationException("SeverityLevels cannot be persisted.");
    }

    @Override
    public void update(SeverityLevel entity) {
        throw new UnsupportedOperationException("SeverityLevels cannot be updated.");
    }

    @Override
    protected void update(SeverityLevel entity, EntityManager em) {
        throw new UnsupportedOperationException("SeverityLevels cannot be updated.");
    }

    @Override
    public void remove(SeverityLevel entity) {
        throw new UnsupportedOperationException("SeverityLevels cannot be removed.");
    }

    @Override
    protected void remove(SeverityLevel entity, EntityManager em) {
        throw new UnsupportedOperationException("SeverityLevels cannot be removed.");
    }

    @Override
    public void remove(Collection<SeverityLevel> entities) {
        throw new UnsupportedOperationException("SeverityLevels cannot be removed.");
    }
}
