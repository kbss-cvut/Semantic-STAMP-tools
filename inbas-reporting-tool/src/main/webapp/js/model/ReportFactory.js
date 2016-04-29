'use strict';

var Constants = require('../constants/Constants');

module.exports = {
    createOccurrenceReport: function () {
        return {
            occurrence: {
                javaClass: Constants.OCCURRENCE_JAVA_CLASS,
                referenceId: Date.now(),
                name: '',
                // Round the time to whole seconds
                startTime: (Date.now() / 1000) * 1000,
                endTime: (Date.now() / 1000) * 1000
            },
            isNew: true,
            javaClass: Constants.OCCURRENCE_REPORT_JAVA_CLASS
        };
    }
};
