package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;

import java.net.URI;
import java.util.Collection;

public abstract class BaseService<T> {

    protected abstract GenericDao<T> getPrimaryDao();

    public Collection<T> findAll() {
        return getPrimaryDao().findAll();
    }

    public T find(URI uri) {
        return getPrimaryDao().find(uri);
    }

    public void persist(T instance) {
        getPrimaryDao().persist(instance);
    }

    public void persist(Collection<T> instances) {
        getPrimaryDao().persist(instances);
    }

    public void update(T instance) {
        getPrimaryDao().update(instance);
    }

    public void remove(T instance) {
        getPrimaryDao().remove(instance);
    }

    public void remove(Collection<T> instances) {
        getPrimaryDao().remove(instances);
    }
}
