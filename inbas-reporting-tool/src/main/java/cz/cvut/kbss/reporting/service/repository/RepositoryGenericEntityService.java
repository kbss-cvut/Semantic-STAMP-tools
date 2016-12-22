package cz.cvut.kbss.reporting.service.repository;

import cz.cvut.kbss.reporting.persistence.dao.GenericEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Objects;

@Service
public class RepositoryGenericEntityService implements GenericEntityService {

    @Autowired
    private GenericEntityDao dao;


    @Override
    public <T> T find(Class<T> resultClass, URI uri) {
        Objects.requireNonNull(resultClass);
        Objects.requireNonNull(uri);
        return dao.find(resultClass, uri);
    }
}
