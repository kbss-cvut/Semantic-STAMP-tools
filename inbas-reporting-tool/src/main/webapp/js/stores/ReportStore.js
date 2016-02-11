'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Utils = require('../utils/Utils');

var BASE_URL = 'rest/reports';
var BASE_URL_WITH_SLASH = 'rest/reports/';
var PRELIMINARY_DTO = '.PreliminaryReportDto';

var ReportStore = Reflux.createStore({
    listenables: [Actions],

    _reports: null,

    onLoadAllReports: function () {
        Ajax.get(BASE_URL).end(function (data) {
            this._reports = data;
            this.trigger({
                action: Actions.loadAllReports,
                reports: this._reports
            });
        }.bind(this), function () {
            this.trigger({
                action: Actions.loadAllReports,
                reports: []
            });
        }.bind(this));
    },

    onLoadReport: function (key) {
        Ajax.get(BASE_URL_WITH_SLASH + key).end(function (data) {
            this.trigger({
                action: Actions.loadReport,
                report: data
            });
        }.bind(this), function () {
            this.trigger({
                action: Actions.loadReport,
                report: null
            });
        }.bind(this));
    },

    onLoadRevisions: function (fileNumber) {
        Ajax.get(BASE_URL_WITH_SLASH + 'chain/' + fileNumber + '/revisions').end(function (data) {
            this.trigger({
                action: Actions.loadRevisions,
                revisions: data
            });
        }.bind(this));
    },

    onCreatePreliminary: function (report, onSuccess, onError) {
        report.dtoClass = PRELIMINARY_DTO;
        Ajax.post(BASE_URL, report).end(function () {
            if (onSuccess) {
                onSuccess();
            }
            this.onLoadAllReports();
        }.bind(this), onError);
    },

    onCreateInvestigation: function (fileNumber, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + 'chain/' + fileNumber + '/revisions?investigate=true').end(function (data, resp) {
            if (onSuccess) {
                var key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
        }, onError);
    },

    onUpdateReport: function (report, onSuccess, onError) {
        Ajax.put(BASE_URL_WITH_SLASH + report.key).send(report).end(function () {
            if (onSuccess) {
                onSuccess();
            }
        }, onError);
    },

    onSubmitReport: function (report, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + 'chain/' + report.fileNumber + '/revisions').end(function (data, resp) {
            if (onSuccess) {
                var key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
        }, onError);
    },

    onDeleteReportChain: function (fileNumber, onSuccess, onError) {
        Ajax.del(BASE_URL_WITH_SLASH + 'chain/' + fileNumber).end(function () {
            if (onSuccess) {
                onSuccess();
            }
            this.onLoadAllReports();
        }.bind(this), onError);
    },

    getReports: function () {
        return this._reports;
    }
});

module.exports = ReportStore;
