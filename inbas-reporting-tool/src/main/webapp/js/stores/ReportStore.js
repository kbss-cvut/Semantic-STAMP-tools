'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var ErrorHandlingMixin = require('./mixin/ErrorHandlingMixin');

/**
 * Stores overviews of all reports.
 */
var ReportStore = Reflux.createStore({
    mixins: [ErrorHandlingMixin],

    _reports: null,

    init: function () {
        this.listenTo(Actions.loadAllReports, this.loadReports);
    },

    loadReports: function () {
        Ajax.get('rest/reports').end(function (err, response) {
            if (err) {
                this.handleError(err);
            } else {
                this._reports = response.body;
                this.trigger(this._reports);
            }
        }.bind(this));
    },

    getReports: function () {
        return this._reports;
    }
});

module.exports = ReportStore;
