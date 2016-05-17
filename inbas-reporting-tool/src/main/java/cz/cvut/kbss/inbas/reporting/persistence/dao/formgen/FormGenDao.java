package cz.cvut.kbss.inbas.reporting.persistence.dao.formgen;

import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.Descriptor;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.net.URI;
import java.util.Objects;
import java.util.Random;

public abstract class FormGenDao<T> {

    private final Random random = new Random();

    @Autowired
    @Qualifier("formGen")
    private EntityManagerFactory emf;

    public URI persist(T instance) {
        Objects.requireNonNull(instance);
        final URI contextUri = generateContextUri();
        final EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            final Descriptor descriptor = new EntityDescriptor(contextUri);
            prePersist(instance, em, descriptor);
            em.persist(instance, descriptor);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return contextUri;
    }

    private URI generateContextUri() {
        return URI.create(Constants.FORM_GEN_CONTEXT_BASE + random.nextInt());
    }

    /**
     * Pre-persist hook, called before the instance is passed to entity manager for persisting.
     *
     * @param instance   The instance to persist
     * @param em         EntityManager with a running transaction
     * @param descriptor Persist descriptor specifying context into which the instance will be persisted
     */
    void prePersist(T instance, EntityManager em, Descriptor descriptor) {
        // Do nothing, intended for overriding
    }
}
