package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

import java.net.URI;

/**
 * @author ledvima1
 */
@Repository
public class PersonDao extends BaseDao<Person> {

    public PersonDao() {
        super(Person.class);
    }

    public Person findByUsername(String username) {
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery(
                    "SELECT ?x WHERE { ?x ?hasUsername ?username . }", Person.class).setParameter("hasUsername",
                    URI.create(Vocabulary.p_username)).setParameter("username", username, "en")
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
