package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;

import java.net.URI;
import java.util.List;

public abstract class BaseReportDao<T> extends BaseDao<T> {

    protected BaseReportDao(Class<T> type) {
        super(type);
    }

    @Override
    protected List<T> findAll(EntityManager em) {
        return em.createNativeQuery("SELECT ?x WHERE { " +
                "?x a ?type ; " +
                "?hasFileNumber ?fileNo ;" +
                "?hasStartTime ?startTime ;" +
                "?hasRevision ?revision . " +
                "{ SELECT (MAX(?rev) AS ?maxRev) ?iFileNo WHERE " +
                "{ ?y a ?type; ?hasFileNumber ?iFileNo ; ?hasRevision ?rev . } GROUP BY ?iFileNo }" +
                "FILTER (?revision = ?maxRev && ?fileNo = ?iFileNo)" +
                "} ORDER BY DESC(?startTime) DESC(?revision)", type)
                 .setParameter("type", typeUri)
                 .setParameter("hasRevision", URI.create(Vocabulary.p_revision))
                 .setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                 .setParameter("hasStartTime", URI.create(Vocabulary.p_startTime))
                 .getResultList();
    }
}
