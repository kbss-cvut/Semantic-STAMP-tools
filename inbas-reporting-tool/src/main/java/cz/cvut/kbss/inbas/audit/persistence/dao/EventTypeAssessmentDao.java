package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.EventTypeAssessment;
import org.springframework.stereotype.Repository;

/**
 * @author ledvima1
 */
@Repository
public class EventTypeAssessmentDao extends BaseDao<EventTypeAssessment> {

    public EventTypeAssessmentDao() {
        super(EventTypeAssessment.class);
    }
}
