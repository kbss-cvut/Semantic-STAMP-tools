'use strict';

var Constants = require('../constants/Constants');
var Routes = require('../utils/Routes');
var I18nStore = require('../stores/I18nStore');

var ReportType = {
    /**
     * Gets route to open for report detail.
     * @param report Report to open detail
     */
    getDetailRoute: function(report) {
        if (this._isPreliminary(report)) {
            return Routes.editReport;
        } else {
            return Routes.editInvestigation;
        }
    },

    _isPreliminary: function(report) {
        return report.types.indexOf(Constants.PRELIMINARY_REPORT_TYPE) !== -1;
    },

    getIconSrc: function(report) {
        if (this._isPreliminary(report)) {
            return 'resources/images/icons/preliminary.png';
        } else {
            return 'resources/images/icons/investigation.png';
        }
    },

    asString: function(report) {
        if (this._isPreliminary(report)) {
            return I18nStore.i18n('preliminary.type');
        } else {
            return I18nStore.i18n('investigation.type');
        }
    }
};

module.exports = ReportType;
