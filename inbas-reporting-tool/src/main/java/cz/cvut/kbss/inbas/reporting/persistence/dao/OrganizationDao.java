package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

/**
 * @author ledvima1
 */
@Repository
public class OrganizationDao extends BaseDao<Organization> {

    public OrganizationDao() {
        super(Organization.class);
    }

    /**
     * Gets organization with the specified name.
     *
     * @param name Organization name
     * @return Organization or {@code null}
     */
    public Organization findByName(String name) {
        if (name == null) {
            return null;
        }
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?x WHERE { ?x <" + Vocabulary.p_label + "> \"" + name + "\"@en . }",
                    Organization.class).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
