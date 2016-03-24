package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.reports.InitialReport;
import org.springframework.stereotype.Repository;

@Repository
public class InitialReportDao extends BaseDao<InitialReport> {

    protected InitialReportDao() {
        super(InitialReport.class);
    }
}
