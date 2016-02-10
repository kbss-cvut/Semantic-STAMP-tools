'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');

var lvpOptions = [];
var occurrenceSeverityOptions = [];
var barrierEffectiveness = [];
var accidentOutcomes = [];

var OptionsStore = Reflux.createStore({

    init: function () {
        this.listenTo(Actions.loadOptions, this.onLoadOptions);
    },

    onLoadOptions: function () {
        this._loadLvpOptions();
        this._loadOccurrenceSeverityOptions();
        this._loadBarrierEffectivenessOptions();
        this._loadAccidentOutcomeOptions();
    },

    _loadLvpOptions: function () {
        if (lvpOptions.length !== 0) {
            this.trigger('lvp', lvpOptions);
            return;
        }
        Ajax.get('rest/options?type=lvp').end(function (data) {
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
        Ajax.get('rest/options?type=occurrenceSeverity').end(function (data) {
            occurrenceSeverityOptions = data;
            this.trigger('occurrenceSeverity', occurrenceSeverityOptions);
        }.bind(this), function () {
            this.trigger('occurrenceSeverity', occurrenceSeverityOptions);
        }.bind(this));
    },

    _loadBarrierEffectivenessOptions: function () {
        if (barrierEffectiveness.length !== 0) {
            this.trigger('barrierEffectiveness', barrierEffectiveness);
            return;
        }
        Ajax.get('rest/options/barrierEffectiveness').end(function (data) {
            barrierEffectiveness = data;
            this.trigger('barrierEffectiveness', barrierEffectiveness);
        }.bind(this), function () {
            this.trigger('barrierEffectiveness', barrierEffectiveness);
        }.bind(this));
    },

    _loadAccidentOutcomeOptions: function () {
        if (accidentOutcomes.length !== 0) {
            this.trigger('accidentOutcome', accidentOutcomes);
            return;
        }
        Ajax.get('rest/options/accidentOutcome').end(function (data) {
            accidentOutcomes = data;
            this.trigger('accidentOutcome', accidentOutcomes);
        }.bind(this), function () {
            this.trigger('accidentOutcome', accidentOutcomes);
        }.bind(this));
    },

    getLowVisibilityProcedureOptions: function () {
        return lvpOptions;
    },

    getOccurrenceSeverityOptions: function () {
        return occurrenceSeverityOptions;
    },

    getBarrierEffectivenessOptions: function () {
        return barrierEffectiveness;
    },

    getAccidentOutcomeOptions: function () {
        return accidentOutcomes;
    }
});

module.exports = OptionsStore;
