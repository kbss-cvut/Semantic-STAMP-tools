/**
 * @author ledvima1
 */

'use strict';

var Reflux = require('reflux');
var request = require('superagent');

var Actions = require('../actions/Actions');

var lvpOptions = [];
var eventSeverityOptions = [];

var OptionsStore = Reflux.createStore({
    init: function() {
        this.listenTo(Actions.loadLvpOptions, this.onLoadLvpOptions);
        this.listenTo(Actions.loadEventSeverityOptions, this.onLoadEventSeverityOptions);
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

    onLoadEventSeverityOptions: function() {
        if (eventSeverityOptions.length !== 0) {
            this.trigger(eventSeverityOptions);
            return;
        }
        request('rest/options/eventSeverity').accept('json').end(function(err, resp) {
            if (err) {
                this.onLoadingError(err, 'Event severity');
            } else {
                eventSeverityOptions = resp.body;
            }
            this.trigger(eventSeverityOptions);
        }.bind(this));
    }
});

module.exports = OptionsStore;
