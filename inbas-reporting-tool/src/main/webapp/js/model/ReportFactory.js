'use strict';

var Constants = require('../constants/Constants');

module.exports = {
    createOccurrenceReport: function () {
        return {
            // Round the time to whole seconds
            occurrenceStart: (Date.now() / 1000) * 1000,
            occurrenceEnd: (Date.now() / 1000) * 1000,
            occurrence: {
                javaClass: Constants.OCCURRENCE_JAVA_CLASS,
                name: ''
            },
            isNew: true,
            javaClass: Constants.OCCURRENCE_REPORT_JAVA_CLASS
        };
    }
};
