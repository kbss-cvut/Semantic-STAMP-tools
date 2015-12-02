'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var ErrorHandlingMixin = require('./mixin/ErrorHandlingMixin');

var reports = null;
var loaded = false;

function loadReports() {
    Ajax.get('rest/preliminaryReports').end(function (err, resp) {
        if (err) {
            console.log(err.status, err.response);
            return;
        }
        PreliminaryReportStore.onReportsLoaded(resp.body);
    });
}

function findReport(key) {
    Ajax.get('rest/preliminaryReports/' + key).end(function (err, resp) {
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
    getCurrentState: function () {
        return {
            reports: reports
        };
    },
    getReports: function () {
        return reports;
    },
    onLoadReports: function () {
        loadReports();
    },
    onReportsLoaded: function (data) {
        reports = data;
        loaded = true;
        this.trigger(this.getCurrentState());
    },
    onReportLoaded: function (report) {
        this.trigger(report);
    },
    onFindReport: function (key) {
        findReport(key);
    },
    onCreateReport: function (report, onSuccess, onError) {
        Ajax.post('rest/preliminaryReports', report).end(function (err, res) {
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
    onUpdateReport: function (report, onSuccess, onError) {
        Ajax.put('rest/preliminaryReports/' + report.key, report).end(function (err, res) {
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
    onDeleteReport: function (report, onSuccess, onError) {
        Ajax.del('rest/preliminaryReports/' + report.key).end(function (err, res) {
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
    isLoaded: function () {
        return loaded;
    }
});

module.exports = PreliminaryReportStore;
