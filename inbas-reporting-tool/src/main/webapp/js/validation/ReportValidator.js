'use strict';

var Constants = require('../constants/Constants');

var ReportValidator = {

    /**
     * Checks whether 376 fields are filled.
     * @param report Report to validate
     * @return {boolean} Validity status
     */
    isValid: function (report) {
        return this.getValidationMessage(report) === null;
    },

    /**
     * Gets I18N property corresponding to the result of validation of the specified report.
     *
     * I.e., when the report is missing some property value, a message about missing required fields is returned, if
     * any
     * of the report values is invalid, then a message about the invalid property value is returned. Null is returned
     * for a valid report.
     * @param report Report to validate
     * @return {*} Message identifier or null
     */
    getValidationMessage: function (report) {
        if (!report.occurrence) {
            return 'detail.invalid-tooltip';
        }
        if (!report.occurrence.name || report.occurrence.name.length === 0) {
            return 'detail.invalid-tooltip';
        }
        if (!report.occurrenceStart) {
            return 'detail.invalid-tooltip';   // Don't expect this to happen, but just to be sure
        }
        if (!report.severityAssessment) {
            return 'detail.invalid-tooltip';
        }
        if (!report.summary || report.summary.length === 0) {
            return 'detail.invalid-tooltip';
        }
        if (!report.occurrenceCategory) {
            return 'detail.invalid-tooltip';
        }
        if (report.occurrenceEnd - report.occurrenceStart > Constants.MAX_START_END_DIFF) {
            return 'detail.large-time-diff-tooltip';
        }
        return null;
    }
};

module.exports = ReportValidator;
