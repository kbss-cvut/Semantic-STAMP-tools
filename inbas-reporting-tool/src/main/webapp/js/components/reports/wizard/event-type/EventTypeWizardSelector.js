/**
 * Created by ledvima1 on 30.6.15.
 */

'use strict';

var assign = require('object-assign');

var RunwayIncursionSteps = require('./runway-incursion/Steps');
var ConflictingAircraftStep = require('./runway-incursion/ConflictingAircraftStep');
var DefaultWizardSteps = require('./default/Steps');
var Constants = require('../../../../constants/Constants');

var EventTypeWizardSelect = {
    // Here we map wizard type to the wizard steps
    wizardSettings: {
        'cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion': {
            steps: RunwayIncursionSteps,
            title: 'Runway Incursion Wizard'
        }
    },

    // ! Always copy the steps so that when they are modified in the wizard, it does not affect other instances of the wizard
    getWizardSettings: function (eventType) {
        var statement = {
            eventType: {
                id: eventType.id,
                name: eventType.name,
                dtoClass: eventType.dtoClass
            }
        };
        if (this.wizardSettings[eventType.dtoClass]) {
            var wizardProperties = this.getExistingWizardProperties(eventType.dtoClass, statement);
            statement.dtoClass = eventType.dtoClass;
            return wizardProperties;
        }
        return this.getDefaultWizardProperties(statement);
    },

    getExistingWizardProperties(dtoClass, statement) {
        var wizardProperties = assign({}, this.wizardSettings[dtoClass]);
        wizardProperties.steps = wizardProperties.steps.slice();
        wizardProperties.statement = statement;
        return wizardProperties;
    },

    getDefaultWizardProperties(statement) {
        return {
            steps: DefaultWizardSteps.slice(),
            title: 'Default Event Type Assessment',
            statement: statement
        };
    },

    getWizardSettingsForStatement: function(statement) {
        if (this.wizardSettings[statement.dtoClass]) {
            var wizardProperties = this.getExistingWizardProperties(statement.dtoClass, statement);
            if (statement.conflictingAircraft) {
                wizardProperties.steps.push({
                    name: 'Conflicting Aircraft',
                    component: ConflictingAircraftStep,
                    id: Constants.CONFLICTING_AIRCRAFT_STEP_ID
                });
            }
            return wizardProperties;
        }
        return this.getDefaultWizardProperties(statement);
    }
};

module.exports = EventTypeWizardSelect;
