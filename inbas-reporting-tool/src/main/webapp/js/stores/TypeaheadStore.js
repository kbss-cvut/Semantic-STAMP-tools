'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');

var URL = 'rest/options?type=';

var eventTypes = [];
var occurrenceCategories = [];
var locations = [];
var operators = [];

var TypeaheadStore = Reflux.createStore({

    init: function () {
        this.listenTo(Actions.loadEventTypes, this.onLoadEventTypes);
        this.listenTo(Actions.loadLocations, this.onLoadLocations);
        this.listenTo(Actions.loadOperators, this.onLoadOperators);
        this.listenTo(Actions.loadOccurrenceCategories, this.onLoadOccurrenceCategories);
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
        Ajax.get(URL + type).end(function (data) {
            success(data);
            this.trigger();
        }.bind(this), function () {
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
    },

    onLoadOccurrenceCategories() {
        this.load('occurrenceCategory', 'occurrenceCategories', occurrenceCategories, function (data) {
            occurrenceCategories = data;
        });
    },

    getOccurrenceCategories() {
        return occurrenceCategories;
    }
});

module.exports = TypeaheadStore;
