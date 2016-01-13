'use strict';

var Reflux = require('reflux');
var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Utils = require('../utils/Utils');

var investigations = [];

var InvestigationStore = Reflux.createStore({
    listenables: [Actions],

    onLoadInvestigations: function () {
        Ajax.get('rest/investigations').end(function (data) {
            investigations = data;
            this.trigger(investigations);
        }.bind(this));
    },

    onCreateInvestigation: function (key, successHandler) {
        Ajax.post('rest/investigations?key=' + key).end(function (data, resp) {
            var key = Utils.extractKeyFromLocationHeader(resp);
            successHandler(key);
        });
    },

    onFindInvestigation: function (key) {
        Ajax.get('rest/investigations/' + key).end(function (data) {
            this.trigger(data);
        }.bind(this), function () {
            this.trigger(null);
        }.bind(this));
    },

    onUpdateInvestigation: function (investigation, successHandler, errorHandler) {
        Ajax.put('rest/investigations/' + investigation.key, investigation).end(successHandler, errorHandler);
    },

    onDeleteInvestigation: function (investigation) {
        Ajax.del('rest/investigations/' + investigation.key).end(this.onLoadInvestigations);
    }
});

module.exports = InvestigationStore;
