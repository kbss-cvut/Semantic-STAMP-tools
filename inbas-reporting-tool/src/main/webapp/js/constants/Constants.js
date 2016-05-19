'use strict';

var Routes = require('../utils/Routes');

module.exports = {
    APP_NAME: 'INBAS Reporting Tool',
    CONFLICTING_AIRCRAFT_STEP_ID: 'conflictingAircraft',
    ECCAIRS_URL: 'http://www.icao.int/safety/airnavigation/AIG/Documents/ADREP%20Taxonomy/ECCAIRS%20Aviation%201.3.0.12%20(Entities%20and%20Attributes).en.id.pdf',
    HOME_ROUTE: Routes.dashboard,
    LINK_TYPES: {
        CAUSE: {
            value: 'http://onto.fel.cvut.cz/ontologies/aviation-safety/causes',
            message: 'factors.causes',
            className: 'gantt-link-causes'
        },
        CONTRIBUTE_TO: {
            value: 'http://onto.fel.cvut.cz/ontologies/aviation-safety/contributes-to',
            message: 'factors.contributes_to',
            className: 'gantt-link-contributes'
        },
        MITIGATE: {
            value: 'http://onto.fel.cvut.cz/ontologies/aviation-safety/mitigates',
            message: 'factors.mitigates',
            className: 'gantt-link-mitigates'
        },
        PREVENT: {
            value: 'http://onto.fel.cvut.cz/ontologies/aviation/core#prevents',
            message: 'factors.prevents',
            className: 'gantt-link-prevents'
        }
    },
    OPTIONS: {
        OCCURRENCE_CLASS: 'occurrenceClass'
    },
    /**
     * Sorting glyph icons
     */
    SORTING: {
        NO: {glyph: 'sort', title: 'sort.no'},
        ASC: {glyph: 'chevron-up', title: 'sort.asc'},
        DESC: {glyph: 'chevron-down', title: 'sort.desc'}
    },

    UNAUTHORIZED_USER: {name: 'unauthorized'},

    FILTER_DEFAULT: 'all',

    DASHBOARDS: {
        MAIN: {
            id: 'main',
            title: 'dashboard.welcome'
        },
        CREATE_REPORT: {
            id: 'createReport',
            title: 'dashboard.create-tile'
        },
        IMPORT_REPORT: {
            id: 'importReport',
            title: 'dashboard.create-import-tile'
        }
    },

    /**
     * Navigation between dashboards. Key is the current dashboard, value is the target to navigate to on goBack
     */
    DASHBOARD_GO_BACK: {
        'main': 'main',
        'createReport': 'main',
        'importReport': 'createReport'
    },

    MINUTE: 60 * 1000,   // Minute in milliseconds

    // Maximum time difference between occurrence start and end. 24 hours in millis
    MAX_OCCURRENCE_START_END_DIFF: 1000 * 60 * 60 * 24,

    OCCURRENCE_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto',
    EVENT_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.event.EventDto',
    OCCURRENCE_REPORT_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto',

    // Form Generator
    SECTION: 'http://onto.fel.cvut.cz/ontologies/form/section',
    FORM: 'http://onto.fel.cvut.cz/ontologies/form/form',
    HAS_SECTION: 'http://onto.fel.cvut.cz/ontologies/form/has-section',
    QUESTION: 'http://onto.fel.cvut.cz/ontologies/form/question',
    HAS_SUBQUESTION: 'http://onto.fel.cvut.cz/ontologies/form/has-subsection',
    HAS_ANSWER: 'http://onto.fel.cvut.cz/ontologies/form/has-question',
    HAS_OPTION: 'http://onto.fel.cvut.cz/ontologies/form/has-possible-value',
    IS_DISABLED: 'http://onto.fel.cvut.cz/ontologies/aviation/form-376/is-disabled',
    LAYOUT_CLASS: 'http://onto.fel.cvut.cz/ontologies/form-layout/layout-class',
    QUESTION_TYPEAHEAD: 'type-ahead',

    GENERATED_ROW_SIZE: 1
};
