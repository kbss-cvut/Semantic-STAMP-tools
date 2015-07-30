package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.CorrectiveMeasure;
import org.springframework.stereotype.Repository;

/**
 * @author ledvima1
 */
@Repository
public class CorrectiveMeasureDao extends BaseDao<CorrectiveMeasure> {

    public CorrectiveMeasureDao() {
        super(CorrectiveMeasure.class);
    }
}
