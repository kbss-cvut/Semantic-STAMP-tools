/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var LowVisibilityProcedureStep = require('./LowVisibilityProcedureStep');
var AircraftClearedStep = require('./AircraftClearedStep');
var RunwayIntruderStep = require('./RunwayIntruderStep');

module.exports = [
    {
        name: 'Low Visibility Procedure',
        component: LowVisibilityProcedureStep
    },
    {
        name: 'Aircraft Cleared to Use Runway',
        component: AircraftClearedStep
    },
    {
        name: 'Runway Intruding Object',
        component: RunwayIntruderStep
    }
];
