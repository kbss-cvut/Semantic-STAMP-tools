'use strict';

var Constants = require('../constants/Constants');

var Factory = {
    createReport: function () {
        return {
            // Round the time to whole seconds
            occurrenceStart: (Date.now() / 1000) * 1000,
            occurrenceEnd: (Date.now() / 1000) * 1000,
            occurrence: {
                reportingPhase: Constants.PRELIMINARY_REPORT_PHASE
            },
            isNew: true
        };
    }
};

module.exports = Factory;
