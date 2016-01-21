'use strict';

var Reflux = require('reflux');
var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Utils = require('../utils/Utils');

var BASE_URL = 'rest/investigations';
var BASE_URL_WITH_SLASH = 'rest/investigations/';

var investigations = [];

var InvestigationStore = Reflux.createStore({
    listenables: [Actions],

    onLoadInvestigations: function () {
        Ajax.get(BASE_URL).end(function (data) {
            investigations = data;
            this.trigger(investigations);
        }.bind(this));
    },

    onCreateInvestigation: function (key, successHandler) {
        Ajax.post(BASE_URL + '?key=' + key).end(function (data, resp) {
            var key = Utils.extractKeyFromLocationHeader(resp);
            successHandler(key);
        });
    },

    onFindInvestigation: function (key) {
        Ajax.get(BASE_URL_WITH_SLASH + key).end(function (data) {
            this.trigger({action: Actions.findInvestigation, investigation: data});
        }.bind(this), function () {
            this.trigger({action: Actions.findInvestigation, investigation: null});
        }.bind(this));
    },

    onUpdateInvestigation: function (investigation, successHandler, errorHandler) {
        Ajax.put(BASE_URL_WITH_SLASH + investigation.key, investigation).end(successHandler, errorHandler);
    },

    onDeleteInvestigation: function (investigation) {
        Ajax.del('rest/investigations/' + investigation.key).end(this.onLoadInvestigations);
    },

    onLoadInvestigationRevisions: function (occurrenceKey) {
        Ajax.get(BASE_URL_WITH_SLASH + 'revisions/' + occurrenceKey).end(function (data) {
            this.trigger({action: Actions.loadInvestigationRevisions, revisions: data});
        }.bind(this));
    },

    onSubmitInvestigation: function (investigation, successHandler, errorHandler) {
        Ajax.post(BASE_URL_WITH_SLASH + investigation.key + '/revisions').end(function (data, resp) {
            if (successHandler) {
                var key = Utils.extractKeyFromLocationHeader(resp);
                successHandler(key);
            }
        }, errorHandler);
    }
});

module.exports = InvestigationStore;
