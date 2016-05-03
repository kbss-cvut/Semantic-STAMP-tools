'use strict';

var Routes = require('../utils/Routes');

module.exports = {
    APP_NAME: 'INBAS Reporting Tool',
    CONFLICTING_AIRCRAFT_STEP_ID: 'conflictingAircraft',
    ECCAIRS_URL: 'http://www.icao.int/safety/airnavigation/AIG/Documents/ADREP%20Taxonomy/ECCAIRS%20Aviation%201.3.0.12%20(Entities%20and%20Attributes).en.id.pdf',
    HOME_ROUTE: Routes.dashboard,
    LINK_TYPES: {
        CAUSE: {
            value: 'http://onto.fel.cvut.cz/ontologies/documentation/causes',
            message: 'factors.causes',
            className: 'gantt-link-causes'
        },
        CONTRIBUTE_TO: {
            value: 'http://onto.fel.cvut.cz/ontologies/documentation/contributes-to',
            message: 'factors.contributes_to',
            className: 'gantt-link-contributes'
        },
        MITIGATE: {
            value: 'http://onto.fel.cvut.cz/ontologies/documentation/mitigates',
            message: 'factors.mitigates',
            className: 'gantt-link-mitigates'
        },
        PREVENT: {
            value: 'http://onto.fel.cvut.cz/ontologies/documentation/prevents',
            message: 'factors.prevents',
            className: 'gantt-link-prevents'
        }
    },

    UNAUTHORIZED_USER: {name: 'unauthorized'},

    FILTER_DEFAULT: 'all',

    DASHBOARDS: {
        MAIN: 'main',
        CREATE_REPORT: 'createReport'
    },

    MINUTE: 60 * 1000,   // Minute in milliseconds
    
    // Maximum time difference between occurrence start and end. 24 hours in millis
    MAX_OCCURRENCE_START_END_DIFF: 1000 * 60 * 60 * 24,

    OCCURRENCE_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto',
    EVENT_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.event.EventDto',
    OCCURRENCE_REPORT_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto'
};
