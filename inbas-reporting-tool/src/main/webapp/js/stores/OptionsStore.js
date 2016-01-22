/**
 * @author ledvima1
 */

'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');

var lvpOptions = [];
var occurrenceSeverityOptions = [];

var OptionsStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.loadLvpOptions, this.onLoadLvpOptions);
        this.listenTo(Actions.loadOccurrenceSeverityOptions, this.onLoadOccurrenceSeverityOptions);
    },

    onLoadLvpOptions: function () {
        if (lvpOptions.length !== 0) {
            this.trigger('lvp', lvpOptions);
            return;
        }
        Ajax.get('rest/options/lvp').end(function (data) {
            lvpOptions = data;
            this.trigger('lvp', lvpOptions);
        }.bind(this), function () {
            this.trigger(lvpOptions);
        }.bind(this));
    },

    onLoadingError: function (err, type) {
        console.log('Unable to load ' + type + ' options. Got status ' + err.status);
    },

    onLoadOccurrenceSeverityOptions: function () {
        if (occurrenceSeverityOptions.length !== 0) {
            this.trigger('occurrenceSeverity', occurrenceSeverityOptions);
            return;
        }
        Ajax.get('rest/options/occurrenceSeverity').end(function (data) {
            occurrenceSeverityOptions = data;
            this.trigger('occurrenceSeverity', occurrenceSeverityOptions);
        }.bind(this), function () {
            this.trigger(occurrenceSeverityOptions);
        }.bind(this));
    }
});

module.exports = OptionsStore;
