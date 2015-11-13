package cz.cvut.kbss.inbas.audit.service;

import java.net.URI;
import java.util.Collection;

public interface BaseService<T> {

    Collection<T> findAll();

    T find(URI uri);

    void persist(T instance);

    void persist(Collection<T> instances);

    void update(T instance);

    void remove(T instance);

    void remove(Collection<T> instances);
}
