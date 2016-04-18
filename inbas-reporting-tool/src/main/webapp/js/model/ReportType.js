'use strict';

var Constants = require('../constants/Constants');
var I18nStore = require('../stores/I18nStore');

var ReportType = {

    getDetailController: function (report) {
        // Use require in method call to prevent circular dependencies with RevisionInfo
        return require('../components/report/occurrence/OccurrenceReportController');
    },

    _isPreliminary: function (report) {
        return !report.phase || report.phase === Constants.PRELIMINARY_REPORT_PHASE;
    },

    getIconSrc: function (report) {
        if (this._isPreliminary(report)) {
            return 'resources/images/icons/preliminary.png';
        } else {
            return 'resources/images/icons/investigation.png';
        }
    },

    asString: function (report) {
        if (this._isPreliminary(report)) {
            return I18nStore.i18n('preliminary.type');
        } else {
            return I18nStore.i18n('investigation.type');
        }
    }
};

module.exports = ReportType;
