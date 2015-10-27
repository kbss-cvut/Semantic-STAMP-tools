'use strict';

/**
 * Provides information about factor styles based on their type, e.g. event type, descriptive factor.
 */
var FactorStyleInfo = {

    getStyleInfo: function (type) {
        switch (type) {
            case 'http://onto.fel.cvut.cz/ontologies/eccairs/model/event-type':
                return {
                    icon: 'resources/images/icons/event_type.gif',
                    cls: 'factor-event-type',
                    title: 'Event type'
                };
            case 'http://onto.fel.cvut.cz/ontologies/eccairs/model/descriptive-factor':
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
    }
};

module.exports = FactorStyleInfo;
