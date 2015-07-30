/**
 * @author ledvima1
 */

'use strict';

var Reflux = require('reflux');
var request = require('superagent');

var Actions = require('../actions/Actions');

var URL = 'rest/typeahead/options?type=';

var eventTypes = [];
var locations = [];

var TypeaheadStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.loadEventTypes, this.onLoadEventTypes);
        this.listenTo(Actions.loadLocations, this.onLoadLocations);
    },

    onLoadEventTypes: function () {
        this.load('eventTypes', 'event types', eventTypes, function (data) {
            eventTypes = data[0].nodes;
        });
    },

    load: function (type, resourceName, requiredData, success) {
        if (requiredData.length !== 0) {
            this.trigger();
            return;
        }
        request.get(URL + type).accept('json').end(function (err, resp) {
            if (err) {
                if (err.status !== 404) {
                    console.log('Unable to load ' + resourceName + '. Got status ' + err.status);
                }
            } else {
                success(resp.body);
            }
            this.trigger();
        }.bind(this));
    },

    getEventTypes: function () {
        return eventTypes;
    },

    onLoadLocations: function () {
        this.load('locations', 'locations', locations, function (data) {
            locations = data;
        });
    },

    getLocations: function () {
        return locations;
    }
});

module.exports = TypeaheadStore;
