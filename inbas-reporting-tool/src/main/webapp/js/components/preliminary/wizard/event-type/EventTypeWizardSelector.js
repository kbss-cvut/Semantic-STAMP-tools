'use strict';

var assign = require('object-assign');

var RunwayIncursionSteps = require('./runway-incursion/Steps');
var ConflictingAircraftStep = require('./runway-incursion/ConflictingAircraftStep');
var DefaultWizardSteps = require('./default/Steps');
var Constants = require('../../../../constants/Constants');

var EventTypeWizardSelect = {
    // Here we map wizard type to the wizard steps
    wizardSettings: [
        {
            steps: RunwayIncursionSteps,
            title: 'Runway Incursion Wizard',
            dtoClass: 'cz.cvut.kbss.inbas.audit.dto.incursion.RunwayIncursionDto',
            keywords: ['incursion']
        }
    ],

    getWizardSettings: function (eventType) {
        var statement = {
            eventType: {
                id: eventType.id,
                name: eventType.name,
                dtoClass: eventType.dtoClass,
                type: eventType.type
            }
        };
        var wizardProperties = this._getWizardProperties(eventType);
        if (wizardProperties) {
            wizardProperties.statement = statement;
            statement.dtoClass = wizardProperties.dtoClass;
            return wizardProperties;
        }
        return this._getDefaultWizardProperties(statement);
    },

    _getWizardProperties: function (eventType) {
        var name = eventType.name.toLowerCase();
        for (var i = 0, len = this.wizardSettings.length; i < len; i++) {
            var props = this.wizardSettings[i];
            for (var j = 0, lenn = props.keywords.length; j < lenn; j++) {
                if (name.indexOf(props.keywords[j]) !== -1) {
                    return this._prepareExistingWizardProperties(props);
                }
            }
        }
        return null;
    },

    _prepareExistingWizardProperties: function (properties) {
        var wizardProperties = assign({}, properties);
        wizardProperties.steps = wizardProperties.steps.slice();
        return wizardProperties;
    },

    _getDefaultWizardProperties: function (statement) {
        return {
            steps: DefaultWizardSteps.slice(),
            title: 'Default Event Type Assessment',
            statement: statement
        };
    },

    getWizardSettingsForStatement: function (statement) {
        var wizardProperties;
        if (statement.dtoClass) {
            wizardProperties = this._getWizardPropertiesByDtoClass(statement.dtoClass);
        } else {
            wizardProperties = this._getWizardProperties(statement.eventType);
        }
        if (wizardProperties) {
            if (statement.conflictingAircraft) {
                wizardProperties.steps.push({
                    name: 'Conflicting Aircraft',
                    component: ConflictingAircraftStep,
                    id: Constants.CONFLICTING_AIRCRAFT_STEP_ID
                });
            }
            wizardProperties.statement = statement;
            return wizardProperties;
        }
        return this._getDefaultWizardProperties(statement);
    },

    _getWizardPropertiesByDtoClass: function (dtoClass) {
        for (var i = 0, len = this.wizardSettings.length; i < len; i++) {
            if (this.wizardSettings[i].dtoClass === dtoClass) {
                return this._prepareExistingWizardProperties(this.wizardSettings[i]);
            }
        }
        return null;
    }
};

module.exports = EventTypeWizardSelect;
