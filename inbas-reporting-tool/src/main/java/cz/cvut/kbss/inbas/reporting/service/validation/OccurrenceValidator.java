package cz.cvut.kbss.inbas.reporting.service.validation;

import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssue;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphNodeVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.FactorGraphTraverser;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.IdentityBasedFactorGraphTraverser;

import java.util.Date;

public class OccurrenceValidator extends Validator<Occurrence> {

    @Override
    public void validateForPersist(Occurrence instance) throws ValidationException {
        validate(instance);
    }

    private void validate(Occurrence occurrence) {
        if (occurrence.getStartTime() != null) {
            if (occurrence.getStartTime().compareTo(new Date()) >= 0) {
                throw new ValidationException("Occurrence start cannot be in the future.");
            }
            if (occurrence.getEndTime() != null &&
                    occurrence.getStartTime().compareTo(occurrence.getEndTime()) > 0) {
                throw new ValidationException("Occurrence start cannot be after occurrence end.");
            }
        }
        if (occurrence.getName() == null || occurrence.getName().isEmpty()) {
            throw new ValidationException("Occurrence name cannot be empty.");
        }
        validateEvents(occurrence);
    }

    private void validateEvents(Occurrence occurrence) {
        final FactorGraphTraverser traverser = new IdentityBasedFactorGraphTraverser(new FactorGraphNodeVisitor() {
            @Override
            public void visit(Occurrence occurrence) {
                // Do nothing
            }

            @Override
            public void visit(SafetyIssue issue) {
                // Do nothing
            }

            @Override
            public void visit(Event event) {
                if (event.getStartTime() == null) {
                    throw new ValidationException("Event " + event + " is missing the required start time value.");
                }
                if (event.getEndTime() == null) {
                    throw new ValidationException("Event " + event + " is missing the required end time value.");
                }
                if (event.getStartTime().compareTo(event.getEndTime()) > 0) {
                    throw new ValidationException("Event start cannot be after its end.");
                }
            }
        }, null);
        traverser.traverse(occurrence);
    }

    @Override
    public void validateForUpdate(Occurrence toValidate, Occurrence original) throws ValidationException {
        validate(toValidate);
    }
}
