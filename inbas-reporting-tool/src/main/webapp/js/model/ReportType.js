'use strict';

var Constants = require('../constants/Constants');
var I18nStore = require('../stores/I18nStore');

var REPORT_TYPES = {
    'http://onto.fel.cvut.cz/ontologies/documentation/occurrence_report': 'Occurrence report'
};

var ReportType = {

    getDetailController: function (report) {
        // Use require in method call to prevent circular dependencies with RevisionInfo
        return require('../components/report/occurrence/OccurrenceReportController');
    },

    getIconSrc: function (report) {
        return 'resources/images/icons/investigation.png';
    },

    asString: function (report) {
        if (report.types) {
            for (var i = 0, len = report.types.length; i < len; i++) {
                if (REPORT_TYPES[report.types[i]]) {
                    return REPORT_TYPES[report.types[i]];
                }
            }
        }
        return '';
    }
};

module.exports = ReportType;
