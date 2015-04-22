package cz.cvut.kbss.inbas.audit.services;

import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

/**
 * Created by ledvima1 on 7.4.15.
 */
@Service
public class PersonService {

    @Autowired
    private GenericDao<Person> dao;

    public void persist(Person person) {
        dao.persist(person);
    }

    public Person find(URI uri) {
        return dao.find(Person.class, uri);
    }

    public List<Person> findAll() {
        return dao.findAll(Person.class);
    }
}
