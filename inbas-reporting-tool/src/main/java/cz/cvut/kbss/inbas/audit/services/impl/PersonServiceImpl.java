package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.exceptions.UsernameExistsException;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.persistence.dao.PersonDao;
import cz.cvut.kbss.inbas.audit.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

/**
 * @author ledvima1
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonDao dao;

    public Person find(URI uri) {
        return dao.findByUri(Person.class, uri);
    }

    public Person findByUsername(String username) {
        return dao.findByUsername(username);
    }

    public List<Person> findAll() {
        return dao.findAll(Person.class);
    }

    public void persist(Person person) {
        if (findByUsername(person.getUsername()) != null) {
            throw new UsernameExistsException("Username " + person.getUsername() + " already exists.");
        }
        person.generateUri();
        dao.persist(person);
    }

    @Override
    public void update(Person instance) {

    }

    @Override
    public void remove(Person instance) {

    }
}
