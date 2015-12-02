'use strict';

var Constants = require('../constants/Constants');
var Routes = require('../utils/Routes');

var ReportType = {
    /**
     * Gets route to open for report detail.
     * @param report Report to open detail
     */
    getDetailRoute: function(report) {
        if (report.types.indexOf(Constants.PRELIMINARY_REPORT_TYPE) !== -1) {
            return Routes.editReport;
        } else {
            return Routes.editInvestigation;
        }
    },

    asString: function(report) {
        if (report.types.indexOf(Constants.PRELIMINARY_REPORT_TYPE) !== -1) {
            return 'Preliminary';
        } else {
            return 'Investigation';
        }
    }
};

module.exports = ReportType;
