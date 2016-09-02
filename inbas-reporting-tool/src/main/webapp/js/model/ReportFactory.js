'use strict';

var Constants = require('../constants/Constants');
var Vocabulary = require('../constants/Vocabulary');
var ReportType = require('./ReportType');
var Utils = require('../utils/Utils');

module.exports = {

    createReport: function (type, options) {
        switch (type) {
            // Intentional fall-through
            case Vocabulary.OCCURRENCE_REPORT:
            case Constants.OCCURRENCE_REPORT_JAVA_CLASS:
                return this.createOccurrenceReport(options);
            case Vocabulary.SAFETY_ISSUE_REPORT:
            case Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS:
                return this.createSafetyIssueReport(options);
            case Vocabulary.AUDIT_REPORT:
            case Constants.AUDIT_REPORT_JAVA_CLASS:
                return this.createAuditReport(options);
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

    createSafetyIssueReport: function (options) {
        var report = {
            safetyIssue: {
                javaClass: Constants.SAFETY_ISSUE_JAVA_CLASS,
                referenceId: Utils.randomInt(),
                name: ''
            },
            isNew: true,
            javaClass: Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS
        };
        report = this._enhanceSafetyIssueWithOptions(report, options);
        return report;
    },

    _enhanceSafetyIssueWithOptions: function (report, options) {
        if (!options) {
            return report;
        }
        report = ReportType.getReport(report);
        if (options.basedOn) {
            report.addBase(options.basedOn);
        }
        return report;
    },

    createAuditReport: function (options) {
        return {
            audit: {
                name: '',
                // Round to whole minutes. Start date a minute before end date
                startDate: Math.floor((Date.now() / (60 * 1000)) * 60 * 1000 - 60000),
                endDate: Math.floor((Date.now() / (60 * 1000)) * 60 * 1000)
            },
            isNew: true,
            javaClass: Constants.AUDIT_REPORT_JAVA_CLASS
        };
    },

    createFactor: function () {
        return {
            javaClass: Constants.EVENT_JAVA_CLASS
        }
    }
};
