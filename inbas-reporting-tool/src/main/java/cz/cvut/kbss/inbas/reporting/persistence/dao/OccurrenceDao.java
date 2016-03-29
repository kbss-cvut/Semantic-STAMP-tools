package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model_new.Occurrence;
import org.springframework.stereotype.Repository;

@Repository
public class OccurrenceDao extends SupportsOwlKey<Occurrence> {

    public OccurrenceDao() {
        super(Occurrence.class);
    }


}
