/**
 * Created by ledvima1 on 30.6.15.
 */

'use strict';

var RunwayIncursionSteps = require('./runway-incursion/Steps');
var DefaultWizardSteps = require('./default/Steps');

var EventTypeWizardSelect = {
    // Here we map wizard type to the wizard steps
    wizardSettings: {
        'runway_incursion': {
            steps: RunwayIncursionSteps,
            title: 'Runway Incursion Wizard'
        }
    },

    getWizardSettings: function (eventType) {
        var statement = {
            eventType: {
                id: eventType.id,
                name: eventType.name
            }
        };
        if (this.wizardSettings[eventType.wizard]) {
            var wizardProperties = this.wizardSettings[eventType.wizard];
            wizardProperties.statement = statement;
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