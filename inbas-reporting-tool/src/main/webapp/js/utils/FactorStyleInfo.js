'use strict';

var Constants = require('../constants/Constants');

/**
 * Provides information about factor styles based on their type, e.g. event type, descriptive factor.
 */
var FactorStyleInfo = {

    getStyleInfo: function (type) {
        switch (type) {
            case 'http://onto.fel.cvut.cz/ontologies/eccairs/event-type':
                return {
                    value: 'ET',
                    bsStyle: 'default',
                    ganttCls: 'factor-event-type',
                    title: 'Event type'
                };
            case 'http://onto.fel.cvut.cz/ontologies/eccairs/descriptive-factor':
                return {
                    value: 'DF',
                    bsStyle: 'success',
                    ganttCls: 'factor-descriptive-factor',
                    title: 'Descriptive factor'
                };
            default:
                return {
                    ganttCls: 'factor-event-type'
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
