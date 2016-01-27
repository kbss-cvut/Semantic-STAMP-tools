'use strict';

var Constants = require('../constants/Constants');
var I18nStore = require('../stores/I18nStore');
var PreliminaryController = require('../components/preliminary/ReportDetailController');
var InvestigationController = require('../components/investigation/InvestigationController');

var ReportType = {

    getDetailController: function (report) {
        if (this._isPreliminary(report)) {
            return PreliminaryController;
        } else {
            return InvestigationController;
        }
    },

    _isPreliminary: function (report) {
        return !report.types || report.types.indexOf(Constants.PRELIMINARY_REPORT_TYPE) !== -1;
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
