'use strict';

var Reflux = require('reflux');
var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');

var investigations = [];

var InvestigationStore = Reflux.createStore({
    listenables: [Actions],

    handleError: function (err) {
        var error = JSON.parse(err.response.text);
        console.log(err.status, error.message, error.requestUri);
    },

    onLoadInvestigations: function () {
        Ajax.get('rest/investigations').end(function (err, res) {
            if (err) {
                this.handleError(err);
            } else {
                investigations = res.body;
                this.trigger(investigations);
            }
        }.bind(this));
    },

    onCreateInvestigation: function (key, successHandler) {
        Ajax.post('rest/investigations?key=' + key).end(function (err, res) {
            if (err) {
                this.handleError(err);
            } else {
                var location = res.headers['location'],
                    key = location.substring(location.lastIndexOf('/') + 1);
                successHandler(key);
            }
        }.bind(this));
    },

    onFindInvestigation: function (key) {
        Ajax.get('rest/investigations/' + key).end(function (err, res) {
            if (err) {
                this.handleError(err);
            } else {
                this.trigger(res.body);
            }
        }.bind(this));
    },

    onUpdateInvestigation: function (investigation, successHandler, errorHandler) {
        Ajax.put('rest/investigations/' + investigation.key, investigation).end(function (err) {
            if (err) {
                if (errorHandler) {
                    var error = JSON.parse(err.response.text);
                    errorHandler(error);
                } else {
                    this.handleError(err);
                }
            } else {
                successHandler();
            }
        }.bind(this));
    }
});

module.exports = InvestigationStore;
