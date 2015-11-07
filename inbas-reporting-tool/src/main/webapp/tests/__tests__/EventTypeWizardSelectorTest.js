'use strict';


describe('EventTypeWizardSelector tests', function () {

    function areStepsEqual(expected, actual) {
        if (expected.length != actual.length) {
            return false;
        }
        for (var i = 0, len = expected.length; i < len; i++) {
            if (expected[i].name !== actual[i].name || expected[i].id !== actual[i].id) {
                return false;
            }
        }
        return true;
    }

    var WizardSelector = require('../../js/components/reports/wizard/event-type/EventTypeWizardSelector'),
        Constants = require('../../js/constants/Constants');

    it('Returns RI wizard properties for Runway Incursion related event', function () {
        var eventType = {
                description: 'An event involving a runway incursion.',
                id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100',
                name: '2200100 - Runway incursions',
                type: ['http://onto.fel.cvut.cz/ontologies/eccairs/model/event-type'],
                dtoClass: 'cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursionDto'
            },
            result = WizardSelector.getWizardSettings(eventType);
        expect(result.statement).toBeDefined();
        expect(areStepsEqual(require('../../js/components/reports/wizard/event-type/runway-incursion/Steps'), result.steps)).toBeTruthy();
    });

    it('Returns default wizard properties for unmatched event type', function () {
        var eventType = {
                description: 'This includes the AIRPROX defined as: A situation in which, i...',
                id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2180000-2180100-2180101',
                name: '2180101 - Loss of separation - aircraft both airborne',
                type: ['http://onto.fel.cvut.cz/ontologies/eccairs/model/event-type']
            },
            result = WizardSelector.getWizardSettings(eventType);
        expect(result.statement).toBeDefined();
        expect(areStepsEqual(require('../../js/components/reports/wizard/event-type/default/Steps'), result.steps)).toBeTruthy();
    });

    it('Returns wizard properties including conflicting aircraft step when it is present in existing statement', function () {
        var statement = {
                dtoClass: 'cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursionDto',
                eventType: {
                    description: 'An event involving a runway incursion.',
                    id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100',
                    name: '2200100 - Runway incursions',
                    type: ['http://onto.fel.cvut.cz/ontologies/eccairs/model/event-type']
                },
                conflictingAircraft: {
                    registration: 'CZ'
                }
            },
            conflictingAircraftStepFound = false,
            result = WizardSelector.getWizardSettingsForStatement(statement);

        for (var i = 0, len = result.steps.length; i < len; i++) {
            if (result.steps[i].id === Constants.CONFLICTING_AIRCRAFT_STEP_ID) {
                conflictingAircraftStepFound = true;
                break;
            }
        }
        expect(result.statement).toBeDefined();
        expect(conflictingAircraftStepFound).toBeTruthy();
    });
});
