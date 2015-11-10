'use strict';

var Constants = require('../constants/Constants');

var Factory = {
    createReport: function() {
        var report = {};
        report.occurrence = {
            // Round the time to whole seconds
            startTime: (Date.now() / 1000) * 1000,
            endTime: (Date.now() / 1000) * 1000,
            reportingPhase: Constants.PRELIMINARY_REPORT_PHASE
        };
        report.isNew = true;
        return report;
    }
};

module.exports = Factory;
