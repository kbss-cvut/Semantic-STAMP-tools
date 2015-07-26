package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.SeverityAssessment;
import org.springframework.stereotype.Repository;

/**
 * @author ledvima1
 */
@Repository
public class SeverityAssessmentDao extends BaseDao<SeverityAssessment> {

    public SeverityAssessmentDao() {
        super(SeverityAssessment.class);
    }
}
