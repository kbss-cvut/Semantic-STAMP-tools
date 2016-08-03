package cz.cvut.kbss.inbas.reporting.model.util.factorgraph;

import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssue;

/**
 * Visits nodes during factor graph traversal.
 */
public interface FactorGraphNodeVisitor {

    void visit(Occurrence occurrence);

    void visit(SafetyIssue issue);

    void visit(Event event);
}
