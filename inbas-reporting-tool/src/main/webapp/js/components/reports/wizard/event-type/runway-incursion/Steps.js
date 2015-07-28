/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var LowVisibilityProcedureStep = require('./LowVisibilityProcedureStep');
var WasConflictingAircraftStep = require('./WasConflictingAircraftStep');
var RunwayIntruderStep = require('./RunwayIntruderStep');

module.exports = [
    {
        name: 'Low Visibility Procedure',
        component: LowVisibilityProcedureStep,
        id: 'lvp'
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
