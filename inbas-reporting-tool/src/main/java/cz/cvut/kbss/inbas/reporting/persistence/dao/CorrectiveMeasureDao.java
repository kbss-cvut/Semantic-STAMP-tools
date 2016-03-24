package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.reports.CorrectiveMeasure;
import org.springframework.stereotype.Repository;

@Repository
public class CorrectiveMeasureDao extends BaseDao<CorrectiveMeasure> {

    public CorrectiveMeasureDao() {
        super(CorrectiveMeasure.class);
    }
}
