'use strict';

var Reflux = require('reflux');
var assign = require('object-assign');

var Actions = require('../actions/Actions');

var WizardStore = Reflux.createStore({
    listenables: [Actions],

    _stepData: [],
    _data: {},

    onInitWizard: function (data, stepData) {
        this._data = data ? assign({}, data) : {};
        this._stepData = [];
        if (stepData) {
            for (var i = 0, len = stepData.length; i < len; i++) {
                stepData.push(assign({}, stepData[i]));
            }
        }
    },

    onUpdateData: function (update) {
        if (!update) {
            return;
        }
        // Defensive copy
        this._data = assign({}, this._data, update);
        this.trigger();
    },

    onUpdateStepData: function (index, update) {
        if (!update || index < 0 || index >= this._stepData.length) {
            return;
        }
        var step = this._stepData[index];
        // Defensive copy
        this._stepData[index] = assign({}, step, update);
        this.trigger();
    },

    onInsertStep: function (index, stepData) {
        this._stepData.splice(index + 1, 0, stepData ? assign({}, stepData) : {});
        this.trigger();
    },

    onRemoveStep: function (index) {
        this._stepData.splice(index, 1);
        this.trigger();
    },

    getData: function () {
        return this._data;
    },

    getStepData: function (index) {
        return index ? this._stepData[index] : this._stepData;
    }
});

module.exports = WizardStore;
