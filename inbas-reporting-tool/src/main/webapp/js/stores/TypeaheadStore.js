/**
 * @author ledvima1
 */

'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var ErrorHandlingMixin = require('./mixin/ErrorHandlingMixin');

var URL = 'rest/typeahead/options?type=';

var eventTypes = [];
var locations = [];
var operators = [];

var TypeaheadStore = Reflux.createStore({
    mixins: [ErrorHandlingMixin],

    init: function () {
        this.listenTo(Actions.loadEventTypes, this.onLoadEventTypes);
        this.listenTo(Actions.loadLocations, this.onLoadLocations);
        this.listenTo(Actions.loadOperators, this.onLoadOperators);
    },

    onLoadEventTypes: function () {
        this.load('eventType', 'event types', eventTypes, function (data) {
            eventTypes = data;
        });
    },

    load: function (type, resourceName, requiredData, success) {
        if (requiredData.length !== 0) {
            this.trigger();
            return;
        }
        Ajax.get(URL + type).end(function (err, resp) {
            if (err) {
                this.handleError(err);
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
        this.load('location', 'locations', locations, function (data) {
            locations = data;
        });
    },

    getLocations: function () {
        return locations;
    },

    onLoadOperators: function () {
        this.load('operator', 'operators', operators, function (data) {
            operators = data;
        });
    },

    getOperators: function () {
        return operators;
    }
});

module.exports = TypeaheadStore;
