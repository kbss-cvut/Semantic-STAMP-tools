'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Utils = require('../utils/Utils');

var BASE_URL = 'rest/preliminaryReports';
var BASE_URL_WITH_SLASH = BASE_URL + '/';

var reports = null;
var loaded = false;

function loadReports() {
    Ajax.get(BASE_URL).end(function (data) {
        PreliminaryReportStore.onReportsLoaded(data);
    });
}

function findReport(key) {
    Ajax.get(BASE_URL_WITH_SLASH + key).end(PreliminaryReportStore.onReportLoaded, function () {
        PreliminaryReportStore.onReportLoaded(null);
    });
}

var PreliminaryReportStore = Reflux.createStore({
    listenables: [Actions],

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
        Ajax.post(BASE_URL, report).end(function () {
            if (onSuccess) {
                onSuccess();
            }
            loadReports();
        }, onError);
    },

    onUpdatePreliminary: function (report, onSuccess, onError) {
        Ajax.put(BASE_URL_WITH_SLASH + report.key, report).end(onSuccess, onError);
    },

    onDeletePreliminary: function (report, onSuccess, onError) {
        Ajax.del(BASE_URL_WITH_SLASH + report.key).end(function () {
            if (onSuccess) {
                onSuccess();
            }
            loadReports();
        }, onError);
    },

    onSubmitPreliminary: function (report, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + report.key + '/revisions').end(function (data, resp) {
            if (onSuccess) {
                var key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
        }, onError);
    },

    onLoadPreliminaryRevisions: function (occurrenceKey) {
        Ajax.get(BASE_URL_WITH_SLASH + 'revisions/' + occurrenceKey).end(function (data) {
            this.trigger({
                action: Actions.loadPreliminaryRevisions,
                revisions: data
            });
        }.bind(this));
    },

    isLoaded: function () {
        return loaded;
    }
});

module.exports = PreliminaryReportStore;
