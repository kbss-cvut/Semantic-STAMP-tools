'use strict';

var LowVisibilityProcedureStep = require('./LowVisibilityProcedureStep');
var IncursionLocationStep = require('./IncursionLocationStep');
var WasConflictingAircraftStep = require('./WasConflictingAircraftStep');
var RunwayIntruderStep = require('./RunwayIntruderStep');

module.exports = [
    {
        name: 'Low Visibility Procedure',
        component: LowVisibilityProcedureStep,
        id: 'lvp'
    },
    {
        name: 'Incursion Location',
        component: IncursionLocationStep,
        id: 'location'
    },
    {
        name: 'Runway Intruding Object',
        component: RunwayIntruderStep,
        defaultNextDisabled: true,
        id: 'intruder'
    },
    {
        name: 'Conflicting Aircraft Presence',
        component: WasConflictingAircraftStep,
        id: 'wasConflict'
    }
];
