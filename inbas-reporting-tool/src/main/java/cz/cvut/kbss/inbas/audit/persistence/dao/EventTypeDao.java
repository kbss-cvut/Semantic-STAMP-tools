package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.EventType;
import org.springframework.stereotype.Repository;

/**
 * @author ledvima1
 */
@Repository
public class EventTypeDao extends BaseDao<EventType> {

    public EventTypeDao() {
        super(EventType.class);
    }
}
