/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.descriptors.Descriptor;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.reporting.model.com.EMail;
import cz.cvut.kbss.reporting.model.com.Vocabulary;
import cz.cvut.kbss.reporting.util.Constants;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Repository
public class EmailDao extends BaseDao<EMail> {

    private static final Logger LOG = LoggerFactory.getLogger(EmailDao.class);
    
    protected Descriptor d = new EntityDescriptor(URI.create("http://onto.fel.cvut.cz/ontologies/ucl-sisel-context-mails"));
    public EmailDao() {
        super(EMail.class);
    }
    
    
    /**
     * Finds entity mail by its unique id.
     *
     * @param mailId mail id
     * @return Entity instance or {@code null} if no such matching exists
     */
    public EMail findByMailId(String mailId) {
        Objects.requireNonNull(mailId);
        final EntityManager em = entityManager();
        try {
            return findByMailId(mailId, em);
        } finally {
            em.close();
        }
    }
    
    public EMail findByX(Function<EntityManager, EMail> findByImpl) {
        final EntityManager em = entityManager();
        EMail email = findByImpl.apply(em);
        em.close();
        return email;
    }

    protected EMail findByMailId(String mailId, EntityManager em) {
        try {
            return em.createNativeQuery("SELECT ?x WHERE { ?x ?hasMailId ?mailId ;" +
                    "a ?type }", type)
                     .setParameter("hasMailId", URI.create(Vocabulary.hasId))
                     .setParameter("mailId", mailId, Constants.PU_LANGUAGE).setParameter("type", typeUri).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public EMail findEmailByReportUri(String reportUri){
        try {
            return findByX(em -> em.createNativeQuery("SELECT ?x WHERE { ?x ?hasPart ?report; a ?type.}", type)
                     .setParameter("hasPart", URI.create(Vocabulary.hasPart))
                     .setParameter("report", URI.create(reportUri))
                     .setParameter("type", typeUri).getSingleResult());
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    protected void persist(EMail entity, EntityManager em) {
        em.persist(entity, d);
    }

    @Override
    protected void update(EMail entity, EntityManager em) {
        em.merge(entity, d);
    }
    
}
