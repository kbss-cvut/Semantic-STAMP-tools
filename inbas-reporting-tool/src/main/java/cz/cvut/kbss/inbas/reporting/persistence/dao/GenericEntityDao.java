package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;

/**
 * This DAO can be used to load instances of entities which do not have regular DAOs.
 */
@Repository
public class GenericEntityDao {

    @Autowired
    private EntityManagerFactory emf;

    /**
     * Loads instance of the target class with the specified identifier.
     *
     * @param resultClass Target class
     * @param uri         Instance identifier
     * @return Matching instance or {@code null}
     */
    public <T> T find(Class<T> resultClass, URI uri) {
        final EntityManager em = emf.createEntityManager();
        try {
            return em.find(resultClass, uri);
        } finally {
            em.close();
        }
    }
}
