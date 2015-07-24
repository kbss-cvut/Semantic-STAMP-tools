package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.services.InbasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collection;

/**
 * @author ledvima1
 */
@Service
public abstract class BaseService<T> implements InbasService<T> {

    protected abstract GenericDao<T> getPrimaryDao();

    @Override
    public Collection<T> findAll() {
        return getPrimaryDao().findAll();
    }

    @Override
    public T find(URI uri) {
        return getPrimaryDao().findByUri(uri);
    }

    @Override
    public void persist(T instance) {
        getPrimaryDao().persist(instance);
    }

    @Override
    public void persist(Collection<T> instances) {
        getPrimaryDao().persist(instances);
    }

    @Override
    public void update(T instance) {
        getPrimaryDao().update(instance);
    }

    @Override
    public void remove(T instance) {
        getPrimaryDao().remove(instance);
    }

    @Override
    public void remove(Collection<T> instances) {
        getPrimaryDao().remove(instances);
    }
}
