'use strict';

var TypeaheadStore = require('../stores/TypeaheadStore');
var Vocabulary = require('../constants/Vocabulary');
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
     * Gets the specified JSON-LD object as a simple, more programmatic-friendly object suitable e.g. for typeahead
     * components.
     * @param jsonLd
     * @return {{id: *, type: *, name: *}}
     */
    jsonLdToEventType: function (jsonLd) {
        var res = {
            id: jsonLd['@id'],
            type: jsonLd['@type'],
            name: typeof(jsonLd[Vocabulary.RDFS_LABEL]) === 'string' ? jsonLd[Vocabulary.RDFS_LABEL] : jsonLd[Vocabulary.RDFS_LABEL]['@value']
        };
        if (jsonLd[Vocabulary.RDFS_COMMENT]) {
            if (typeof(jsonLd[Vocabulary.RDFS_COMMENT]) === 'string') {
                res.description = jsonLd[Vocabulary.RDFS_COMMENT];
            } else {
                res.description = jsonLd[Vocabulary.RDFS_COMMENT]['@value'];
            }
        }
        return res;
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
