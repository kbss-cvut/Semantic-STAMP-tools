package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model_new.EventType;
import org.springframework.stereotype.Repository;

@Repository
public class EventTypeDao extends BaseDao<EventType> {

    public EventTypeDao() {
        super(EventType.class);
    }
}
