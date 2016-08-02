package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssue;
import org.springframework.stereotype.Repository;

@Repository
public class SafetyIssueDao extends BaseDao<SafetyIssue> {

    public SafetyIssueDao() {
        super(SafetyIssue.class);
    }
}
