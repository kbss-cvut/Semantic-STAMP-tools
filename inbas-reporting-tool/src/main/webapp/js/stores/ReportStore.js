'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');

/**
 * Stores overviews of all reports.
 */
var ReportStore = Reflux.createStore({

    _reports: null,

    init: function () {
        this.listenTo(Actions.loadAllReports, this.loadReports);
        this.listenTo(Actions.deleteReport, this.deleteReport);
    },

    loadReports: function () {
        Ajax.get('rest/reports').end(function (data) {
            this._reports = data;
            this.trigger(this._reports);
        }.bind(this));
    },

    deleteReport: function (report, onSuccess, onError) {
        Ajax.del('rest/reports/' + report.key).end(function () {
            if (onSuccess) {
                onSuccess();
            }
            this.loadReports();
        }.bind(this), onError);
    },

    getReports: function () {
        return this._reports;
    }
});

module.exports = ReportStore;
