'use strict';

const Reflux = require('reflux');

const Actions = require('../actions/Actions');
let Ajax = require('../utils/Ajax');
const Constants = require('../constants/Constants');
const JsonReferenceResolver = require('../utils/JsonReferenceResolver').default;
const ReportType = require('../model/ReportType');
const ReportFactory = require('../model/ReportFactory');
const Utils = require('../utils/Utils');

const BASE_URL = 'rest/reports';
const BASE_URL_WITH_SLASH = 'rest/reports/';
const ECCAIRS_REPORT_URL = 'rest/eccairs/latest/';

var BASE_ECCAIRS_URL_WITH_SLASH = 'rest/eccairs/';

// When reports are being loaded, do not send the request again
let reportsLoading = false;
// Was the last report load filtered by report keys
let lastLoadWithKeys = false;

const ReportStore = Reflux.createStore({
    listenables: [Actions],

    _reports: null,
    _searchReports: null,
    _pendingLoad: null,

    _resetPendingLoad: function () {
        this._pendingLoad = null;
    },

    onLoadAllReports: function (keys = []) {
        if (reportsLoading) {
            return;
        }
        reportsLoading = true;
        lastLoadWithKeys = keys.length !== 0;
        Ajax.get(this._initLoadUri(keys)).end((data) => {
            reportsLoading = false;
            for (let i = 0, len = data.length; i < len; i++) {
                ReportFactory.addMethodsToReportInstance(data[i]);
            }
            this._reports = data;
            if (!lastLoadWithKeys) {
                this._searchReports = this._reports;
                this.trigger({
                    action: Actions.loadReportsForSearch,
                    reports: this._searchReports
                });
            }
            this.trigger({
                action: Actions.loadAllReports,
                reports: this._reports
            });
        }, () => {
            reportsLoading = false;
            this.trigger({
                action: Actions.loadAllReports,
                reports: []
            });
        });
    },

    _initLoadUri: function (keys) {
        let url = BASE_URL;
        for (let i = 0, len = keys.length; i < len; i++) {
            url += (i === 0 ? '?' : '&') + 'key=' + keys[i];
        }
        return url;
    },

    onLoadReportsForSearch: function () {
        if (reportsLoading) {
            if (lastLoadWithKeys) {
                this._actuallyLoadReportsForSearch();
            }
        } else {
            if (this._reports && !lastLoadWithKeys) {
                this._searchReports = this._reports;
                this.trigger({
                    action: Actions.loadReportsForSearch,
                    reports: this._searchReports
                });
            } else {
                this._actuallyLoadReportsForSearch();
            }
        }
    },

    _actuallyLoadReportsForSearch: function () {
        Ajax.get(BASE_URL).end((data) => {
            this._searchReports = data;
            this.trigger({
                action: Actions.loadReportsForSearch,
                reports: this._searchReports
            });
        }, () => {
            this.trigger({
                action: Actions.loadReportsForSearch,
                reports: []
            });
        });
    },

    onLoadReport: function (key) {
        if (this._pendingLoad === key) {
            return;
        }
        this._pendingLoad = key;
        Ajax.get(BASE_URL_WITH_SLASH + key).end(function (data) {
            this._resetPendingLoad();
            JsonReferenceResolver.resolveReferences(data);
            ReportFactory.addMethodsToReportInstance(data);
            this.trigger({
                action: Actions.loadReport,
                report: data
            });
        }.bind(this), function () {
            this._resetPendingLoad();
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

    onCreateReport: function (report, onSuccess, onError) {
        JsonReferenceResolver.encodeReferences(report);
        Ajax.post(BASE_URL, report).end(function (data, resp) {
            if (onSuccess) {
                const key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
            this.onLoadAllReports();
        }.bind(this), onError);
    },

    onImportE5Report: function (file, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + 'importE5').attach(file).end((data, resp) => {
            if (onSuccess) {
                const key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
        }, onError);
    },

    onImportSafaExcel: function (file, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + 'importSafa').attach(file).end(() => {
            if (onSuccess) {
                onSuccess();
            }
        }, onError);
    },

    onUpdateReport: function (report, onSuccess, onError) {
        JsonReferenceResolver.encodeReferences(report);
        Ajax.put(BASE_URL_WITH_SLASH + report.key).send(report).end(onSuccess, onError);
    },

    onSubmitReport: function (report, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + 'chain/' + report.fileNumber + '/revisions').end(function (data, resp) {
            if (onSuccess) {
                const key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
        }, onError);
    },

    onPhaseTransition: function (report, onSuccess, onError) {
        Ajax.put(BASE_URL_WITH_SLASH + report.key + '/phase').end(onSuccess, onError);
    },

    onDeleteReportChain: function (fileNumber, onSuccess, onError) {
        Ajax.del(BASE_URL_WITH_SLASH + 'chain/' + fileNumber).end(function () {
            if (onSuccess) {
                onSuccess();
            }
            this.onLoadAllReports();
        }.bind(this), onError);
    },

    onFindLatestEccairsVersion: function (report, onSuccess, onError) {
        Ajax.get(BASE_ECCAIRS_URL_WITH_SLASH + 'latest/' + report.key, report).end(function () {
            if (onSuccess) {
                onSuccess();
            }
            this.onLoadAllReports();
        }.bind(this), onError);
    },

    onAddSafetyIssueBase: function (key, newBase) {
        this._pendingLoad = key;
        Ajax.get(BASE_URL_WITH_SLASH + key).end(function (data) {
            JsonReferenceResolver.resolveReferences(data);
            this._addSafetyIssueBase(data, newBase);
        }.bind(this), function () {
            this._resetPendingLoad();
            this.trigger({
                action: Actions.addSafetyIssueBase,
                report: null
            });
        }.bind(this));
    },

    _addSafetyIssueBase: function (issue, newBase) {
        const report = ReportType.getReport(issue),
            result = report.addBase(newBase.event, newBase.report),
            message = result ? 'safetyissue.base-add-success' : 'safetyissue.base-add-duplicate';
        this._resetPendingLoad();
        this.trigger({
            action: Actions.addSafetyIssueBase,
            report: report
        });
        Actions.publishMessage(message, result ? Constants.MESSAGE_TYPE.SUCCESS : Constants.MESSAGE_TYPE.WARNING, Actions.addSafetyIssueBase);
    },

    onLoadEccairsReport: function (forReport) {
        Ajax.get(ECCAIRS_REPORT_URL + forReport.key).end((data) => {
            this._resetPendingLoad();
            this.trigger({
                action: Actions.loadEccairsReport,
                key: data
            })
        }, (data, resp) => {
            this._resetPendingLoad();
            if (resp.status === 404) {
                this.trigger({
                    action: Actions.loadEccairsReport,
                    key: null
                });
            }
        });
    },

    onNewRevisionFromLatestEccairs: function (report, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + 'chain/' + report.fileNumber + '/revisions/eccairs').end((data, resp) => {
            if (onSuccess) {
                const key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
        }, onError);
    },

    getReports: function () {
        return this._reports;
    },

    getReportsForSearch: function () {
        return this._searchReports;
    }
});

module.exports = ReportStore;
