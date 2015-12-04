package cz.cvut.kbss.inbas.audit.service.validation;

import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.ValidatableReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreliminaryReportValidator implements Validator<PreliminaryReport> {

    @Autowired
    private Validator<ValidatableReport> validator;

    @Override
    public void validate(PreliminaryReport instance) throws ValidationException {
        validator.validate(instance);
        if (instance.getTypeAssessments() == null || instance.getTypeAssessments().isEmpty()) {
            throw new ValidationException("Report is missing type assessments.");
        }
    }
}
