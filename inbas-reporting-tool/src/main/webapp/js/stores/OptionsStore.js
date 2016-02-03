'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');

var lvpOptions = [];
var occurrenceSeverityOptions = [];

var OptionsStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.loadOptions, this.onLoadOptions);
    },

    onLoadOptions: function () {
        this._loadLvpOptions();
        this._loadOccurrenceSeverityOptions();
    },

    _loadLvpOptions: function () {
        if (lvpOptions.length !== 0) {
            this.trigger('lvp', lvpOptions);
            return;
        }
        Ajax.get('rest/options/lvp').end(function (data) {
            lvpOptions = data;
            this.trigger('lvp', lvpOptions);
        }.bind(this), function () {
            this.trigger('lvp', lvpOptions);
        }.bind(this));
    },

    _loadOccurrenceSeverityOptions: function () {
        if (occurrenceSeverityOptions.length !== 0) {
            this.trigger('occurrenceSeverity', occurrenceSeverityOptions);
            return;
        }
        Ajax.get('rest/options/occurrenceSeverity').end(function (data) {
            occurrenceSeverityOptions = data;
            this.trigger('occurrenceSeverity', occurrenceSeverityOptions);
        }.bind(this), function () {
            this.trigger('occurrenceSeverity', occurrenceSeverityOptions);
        }.bind(this));
    },

    getLowVisibilityProcedureOptions: function () {
        return lvpOptions;
    },

    getOccurrenceSeverityOptions: function () {
        return occurrenceSeverityOptions;
    }
});

module.exports = OptionsStore;
