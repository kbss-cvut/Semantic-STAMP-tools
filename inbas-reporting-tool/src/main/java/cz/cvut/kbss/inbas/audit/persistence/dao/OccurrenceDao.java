package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import org.springframework.stereotype.Repository;

@Repository
public class OccurrenceDao extends BaseDao<Occurrence> {

    public OccurrenceDao() {
        super(Occurrence.class);
    }


}
