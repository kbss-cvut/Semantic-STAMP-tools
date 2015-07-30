/**
 * @author ledvima1
 */

'use strict';

var Reflux = require('reflux');
var request = require('superagent');

var Actions = require('../actions/Actions');

var lvpOptions = [];
var occurrenceSeverityOptions = [];

var OptionsStore = Reflux.createStore({
    init: function() {
        this.listenTo(Actions.loadLvpOptions, this.onLoadLvpOptions);
        this.listenTo(Actions.loadOccurrenceSeverityOptions, this.onLoadOccurrenceSeverityOptions);
    },

    onLoadLvpOptions: function() {
        if (lvpOptions.length !== 0) {
            this.trigger(lvpOptions);
            return;
        }
        request.get('rest/options/lvp').accept('json').end(function(err, resp) {
            if (err) {
                this.onLoadingError(err, 'Low visibility procedure');
            } else {
                lvpOptions = resp.body;
            }
            this.trigger(lvpOptions);
        }.bind(this));
    },

    onLoadingError: function(err, type) {
        if (err.status !== 404) {
            console.log('Unable to load ' + type + ' options. Got status ' + err.status);
        }
    },

    onLoadOccurrenceSeverityOptions: function() {
        if (occurrenceSeverityOptions.length !== 0) {
            this.trigger(occurrenceSeverityOptions);
            return;
        }
        request('rest/options/occurrenceSeverity').accept('json').end(function(err, resp) {
            if (err) {
                this.onLoadingError(err, 'Occurrence severity');
            } else {
                occurrenceSeverityOptions = resp.body;
            }
            this.trigger(occurrenceSeverityOptions);
        }.bind(this));
    }
});

module.exports = OptionsStore;
