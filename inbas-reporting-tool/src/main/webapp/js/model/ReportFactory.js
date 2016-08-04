'use strict';

var Constants = require('../constants/Constants');
var Vocabulary = require('../constants/Vocabulary');
var Utils = require('../utils/Utils');

module.exports = {

    createReport: function (type) {
        switch (type) {
            // Intentional fall-through
            case Vocabulary.OCCURRENCE_REPORT:
            case Constants.OCCURRENCE_REPORT_JAVA_CLASS:
                return this.createOccurrenceReport();
            case Vocabulary.SAFETY_ISSUE_REPORT:
            case Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS:
                return this.createSafetyIssueReport();
            default:
                throw 'Unsupported report type ' + type;
        }
    },

    createOccurrenceReport: function () {
        return {
            occurrence: {
                javaClass: Constants.OCCURRENCE_JAVA_CLASS,
                referenceId: Utils.randomInt(),
                name: '',
                // Round the time to whole seconds
                startTime: (Date.now() / 1000) * 1000,
                endTime: (Date.now() / 1000) * 1000
            },
            isNew: true,
            javaClass: Constants.OCCURRENCE_REPORT_JAVA_CLASS
        };
    },

    createSafetyIssueReport: function () {
        return {
            safetyIssue: {
                javaClass: Constants.SAFETY_ISSUE_JAVA_CLASS,
                referenceId: Utils.randomInt(),
                name: ''
            },
            isNew: true,
            javaClass: Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS
        };
    },

    createFactor: function () {
        return {
            javaClass: Constants.EVENT_JAVA_CLASS
        }
    }
};
