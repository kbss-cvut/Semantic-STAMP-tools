'use strict';

var Constants = require('../constants/Constants');
var EventTypeFactory = require('../model/EventTypeFactory');

/**
 * Provides information about factor styles based on their type, e.g. event type, descriptive factor.
 */
var FactorStyleInfo = {

    getStyleInfo: function (type) {
        switch (type) {
            case 'http://onto.fel.cvut.cz/ontologies/eccairs/event-type':
                return {
                    icon: 'resources/images/icons/event_type.gif',
                    cls: 'factor-event-type',
                    title: 'Event type'
                };
            case 'http://onto.fel.cvut.cz/ontologies/eccairs/descriptive-factor':
                return {
                    icon: 'resources/images/icons/descriptive_factor.gif',
                    cls: 'factor-descriptive-factor',
                    title: 'Descriptive factor'
                };
            default:
                return {
                    cls: 'factor-event-type'
                };
        }
    },

    getLinkStyle: function (link) {
        var linkType = Object.getOwnPropertyNames(Constants.LINK_TYPES).find((item) => {
            return Constants.LINK_TYPES[item].value === link.factorType;
        });
        return linkType ? Constants.LINK_TYPES[linkType].className : '';
    }
};

module.exports = FactorStyleInfo;
