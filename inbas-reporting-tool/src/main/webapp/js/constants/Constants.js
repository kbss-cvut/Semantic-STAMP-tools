'use strict';

var Routes = require('../utils/Routes');

module.exports = {
    APP_NAME: 'SAFety Reporting and Analysis',
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

    // ARMS Index values, according to https://www.easa.europa.eu/essi/documents/ARMS.pdf, slide 27
    ARMS_INDEX: {
        'NEGLIGIBLE': {
            'EFFECTIVE': 1,
            'LIMITED': 1,
            'MINIMAL': 1,
            'NOT_EFFECTIVE': 1
        },
        'MINOR': {
            'EFFECTIVE': 2,
            'LIMITED': 4,
            'MINIMAL': 20,
            'NOT_EFFECTIVE': 100
        },
        'MAJOR': {
            'EFFECTIVE': 10,
            'LIMITED': 20,
            'MINIMAL': 100,
            'NOT_EFFECTIVE': 500
        },
        'CATASTROPHIC': {
            'EFFECTIVE': 50,
            'LIMITED': 100,
            'MINIMAL': 500,
            'NOT_EFFECTIVE': 2500
        }
    },

    MINUTE: 60 * 1000,   // Minute in milliseconds
    
    // Maximum time difference between occurrence start and end. 24 hours in millis
    MAX_OCCURRENCE_START_END_DIFF: 1000 * 60 * 60 * 24
};
