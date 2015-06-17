/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var LowVisibilityProcedure = require('./LowVisibilityProcedure');
var AircraftCleared = require('./AircraftCleared');

module.exports = [
    {
        name: 'Low Visibility Procedure',
        component: LowVisibilityProcedure
    },
    {
        name: 'Aircraft Cleared to Use Runway',
        component: AircraftCleared
    }
];
