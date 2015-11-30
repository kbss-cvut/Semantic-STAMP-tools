package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class InitialReportDao extends BaseDao<InitialReport> {

    protected InitialReportDao() {
        super(InitialReport.class);
    }

    @Override
    protected void persist(InitialReport entity, EntityManager em) {
        entity.addType(Vocabulary.Report);
        super.persist(entity, em);
    }

    @Override
    protected void update(InitialReport entity, EntityManager em) {
        entity.addType(Vocabulary.Report);
        super.update(entity, em);
    }
}
