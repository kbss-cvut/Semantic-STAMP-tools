package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.services.InbasService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Collection;

/**
 * @author ledvima1
 */
public class BaseService<T> implements InbasService<T> {

    @Autowired
    private GenericDao<T> dao;

    @Override
    public Collection<T> findAll() {
        // TODO
        return null;
    }

    @Override
    public T find(URI uri) {
        // TODO
        return null;
    }

    @Override
    public void persist(T instance) {
        dao.persist(instance);
    }

    @Override
    public void update(T instance) {
        dao.update(instance);
    }

    @Override
    public void remove(T instance) {
        dao.remove(instance);
    }
}
