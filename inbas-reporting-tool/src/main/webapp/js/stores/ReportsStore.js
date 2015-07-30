/**
 * Created by ledvima1 on 26.5.15.
 */

'use strict';

var Reflux = require('reflux');
var Actions = require('../actions/Actions');
var request = require('superagent');


var reports = null;
var loaded = false;

function loadReports() {
    request.get('rest/reports').accept('json').end(function (err, resp) {
        if (err) {
            if (err.status === 404) {
                ReportsStore.onReportsLoaded([]);
            } else {
                console.log(err.status, err.response);
            }
            return;
        }
        ReportsStore.onReportsLoaded(resp.body);
    });
}

function findReport(key) {
    request.get('rest/reports/' + key).accept('json').end(function (err, resp) {
        if (err) {
            if (err.status !== 404) {
                console.log(err.status, err.response);
            }
            ReportsStore.onReportLoaded(null);
        }
        ReportsStore.onReportLoaded(resp.body);
    });
}

var ReportsStore = Reflux.createStore({
    listenables: [Actions],
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
    onFindReport: function(key) {
        findReport(key);
    },
    onCreateReport: function (report, onSuccess, onError) {
        request.post('rest/reports').send(report).type('json').end(function (err, res) {
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
    handleError: function (err) {
        var error = JSON.parse(err.response.text);
        console.log(err.status, error.message, error.requestUri);
    },
    onUpdateReport: function (report, onSuccess, onError) {
        request.put('rest/reports/' + report.key).send(report).type('json').end(function (err, res) {
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
        request.del('rest/reports/' + report.key).end(function (err, res) {
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

module.exports = ReportsStore;
