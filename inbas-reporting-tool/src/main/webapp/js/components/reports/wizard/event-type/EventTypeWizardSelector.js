/**
 * Created by ledvima1 on 30.6.15.
 */

'use strict';

var RunwayIncursionSteps = require('./runway-incursion/Steps');
var DefaultWizardSteps = require('./default/Steps');

var EventTypeWizardSelect = {
    // Here we map wizard type to the wizard steps
    wizardSettings: {
        'cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion': {
            steps: RunwayIncursionSteps,
            title: 'Runway Incursion Wizard'
        }
    },

    getWizardSettings: function (eventType) {
        var statement = {
            eventType: {
                id: eventType.id,
                name: eventType.name,
                dtoClass: eventType.dtoClass
            }
        };
        if (this.wizardSettings[eventType.dtoClass]) {
            var wizardProperties = this.wizardSettings[eventType.dtoClass];
            wizardProperties.statement = statement;
            statement.dtoClass = eventType.dtoClass;
            return wizardProperties;
        }
        return {
            steps: DefaultWizardSteps,
            title: 'Default Event Type Assessment',
            statement: statement
        };
    }
};

module.exports = EventTypeWizardSelect;