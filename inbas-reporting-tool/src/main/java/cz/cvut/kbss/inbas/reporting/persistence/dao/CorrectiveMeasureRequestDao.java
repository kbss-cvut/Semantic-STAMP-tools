package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model_new.CorrectiveMeasureRequest;
import org.springframework.stereotype.Repository;

@Repository
public class CorrectiveMeasureRequestDao extends BaseDao<CorrectiveMeasureRequest> {

    public CorrectiveMeasureRequestDao() {
        super(CorrectiveMeasureRequest.class);
    }
}
