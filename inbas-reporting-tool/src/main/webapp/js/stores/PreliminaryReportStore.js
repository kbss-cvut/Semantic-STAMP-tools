'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var ErrorHandlingMixin = require('./mixin/ErrorHandlingMixin');
var Utils = require('../utils/Utils');

var BASE_URL = 'rest/preliminaryReports';
var BASE_URL_WITH_SLASH = BASE_URL + '/';

var reports = null;
var loaded = false;

function loadReports() {
    Ajax.get(BASE_URL).end(function (err, resp) {
        if (err) {
            console.log(err.status, err.response);
            return;
        }
        PreliminaryReportStore.onReportsLoaded(resp.body);
    });
}

function findReport(key) {
    Ajax.get(BASE_URL_WITH_SLASH + key).end(function (err, resp) {
        if (err) {
            if (err.status !== 404) {
                console.log(err.status, err.response);
            }
            PreliminaryReportStore.onReportLoaded(null);
        }
        PreliminaryReportStore.onReportLoaded(resp.body);
    });
}

var PreliminaryReportStore = Reflux.createStore({
    listenables: [Actions],

    mixins: [ErrorHandlingMixin],

    getReports: function () {
        return reports;
    },

    onLoadPreliminaries: function () {
        loadReports();
    },

    onReportsLoaded: function (data) {
        reports = data;
        loaded = true;
        this.trigger(reports);
    },

    onReportLoaded: function (report) {
        this.trigger({
            action: Actions.findPreliminary,
            report: report
        });
    },

    onFindPreliminary: function (key) {
        findReport(key);
    },

    onCreatePreliminary: function (report, onSuccess, onError) {
        Ajax.post(BASE_URL, report).end(function (err) {
            if (err) {
                var error = JSON.parse(err.response.text);
                onError ? onError(error) : this.handleError(err);
            } else {
                if (onSuccess) {
                    onSuccess();
                }
                loadReports();
            }
        }.bind(this));
    },

    onUpdatePreliminary: function (report, onSuccess, onError) {
        Ajax.put(BASE_URL_WITH_SLASH + report.key, report).end(function (err) {
            if (err) {
                var error = JSON.parse(err.response.text);
                onError ? onError(error) : this.handleError(err);
            } else if (onSuccess) {
                onSuccess();
            }
        }.bind(this));
    },

    onDeletePreliminary: function (report, onSuccess, onError) {
        Ajax.del(BASE_URL_WITH_SLASH + report.key).end(function (err) {
            if (err) {
                var error = JSON.parse(err.response.text);
                onError ? onError(error) : this.handleError(err);
            } else {
                if (onSuccess) {
                    onSuccess();
                }
                loadReports();
            }
        }.bind(this));
    },

    onSubmitPreliminary: function (report, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + report.key + '/revisions').end(function (err, res) {
            if (err) {
                var error = JSON.parse(err.response.text);
                onError ? onError(error) : this.handleError(err);
            } else if (onSuccess) {
                var key = Utils.extractKeyFromLocationHeader(res);
                onSuccess(key);
            }
        }.bind(this));
    },

    onLoadPreliminaryRevisions: function (occurrenceKey) {
        Ajax.get(BASE_URL_WITH_SLASH + 'revisions/' + occurrenceKey).end(function (err, res) {
            if (err) {
                this.handleError(err);
            } else {
                this.trigger({
                    action: Actions.loadPreliminaryRevisions,
                    revisions: res.body
                });
            }
        }.bind(this));
    },

    isLoaded: function () {
        return loaded;
    }
});

module.exports = PreliminaryReportStore;
