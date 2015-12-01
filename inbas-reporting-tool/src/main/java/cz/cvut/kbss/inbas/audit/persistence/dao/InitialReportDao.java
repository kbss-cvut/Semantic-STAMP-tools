package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import org.springframework.stereotype.Repository;

@Repository
public class InitialReportDao extends BaseDao<InitialReport> {

    protected InitialReportDao() {
        super(InitialReport.class);
    }
}
