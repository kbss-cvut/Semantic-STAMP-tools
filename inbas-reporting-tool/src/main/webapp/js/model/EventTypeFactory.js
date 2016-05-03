'use strict';

var TypeaheadStore = require('../stores/TypeaheadStore');
var EventTypeStatement = require('./EventTypeStatement');
var RunwayIncursion = require('./RunwayIncursion');

module.exports = {
    create: function (data) {
        if (data.intruder) {
            return new RunwayIncursion(data);
        } else {
            return new EventTypeStatement(data);
        }
    },

    /**
     * Gets event type (occurrence category) instance with the specified id.
     * @param id Event type id
     * @param items Event types
     * @return {*} Matching instance or null
     */
    resolveEventType: function (id, items) {
        if (!id) {
            return null;
        }
        if (!items) {
            items = TypeaheadStore.getEventTypes();
        }
        return items.find((item) => {
            return item['@id'] === id;
        });
    }
};
