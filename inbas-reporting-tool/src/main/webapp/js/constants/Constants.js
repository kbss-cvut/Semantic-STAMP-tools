'use strict';

var Routes = require('../utils/Routes');

module.exports = {
    APP_NAME: 'INBAS Reporting Tool',
    CONFLICTING_AIRCRAFT_STEP_ID: 'conflictingAircraft',
    ECCAIRS_URL: 'http://www.icao.int/safety/airnavigation/AIG/Documents/ADREP%20Taxonomy/ECCAIRS%20Aviation%201.3.0.12%20(Entities%20and%20Attributes).en.id.pdf',
    HOME_ROUTE: Routes.dashboard,
    PRELIMINARY_REPORT_PHASE: 'PRELIMINARY',
    INVESTIGATION_REPORT_PHASE: 'INVESTIGATION',
    LINK_TYPES: {
        CAUSE: 'cause',
        MITIGATE: 'mitigate'
    },

    UNAUTHORIZED_USER: {name: 'unauthorized'},

    FILTER_DEFAULT: 'all',

    DASHBOARDS: {
        MAIN: 'main',
        CREATE_REPORT: 'createReport'
    },

    MINUTE: 60 * 1000,   // Minute in milliseconds
    
    // Maximum time difference between occurrence start and end. 24 hours in millis
    MAX_OCCURRENCE_START_END_DIFF: 1000 * 60 * 60 * 24
};
