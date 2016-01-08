'use strict';

var InvestigationValidator = {

    /**
     * Checks whether 376 fields are filled.
     * @param report Report to validate
     * @return {boolean} Validity status
     */
    isValid: function (report) {
        if (!report.occurrence) {
            return false;
        }
        if (!report.occurrence.name || report.occurrence.name.length === 0) {
            return false;
        }
        if (!report.occurrenceStart) {
            return false;   // Don't expect this to happen, but just to be sure
        }
        if (!report.severityAssessment) {
            return false;
        }
        if (!report.summary || report.summary.length === 0) {
            return false;
        }
        if (!report.rootFactor) {
            return false;
        }
        return true;
    }
};

module.exports = InvestigationValidator;