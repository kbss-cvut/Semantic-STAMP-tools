'use strict';

const Routes = require('../utils/Routes');

module.exports = {
    APP_NAME: 'SISel',
    ECCAIRS_URL: 'http://www.icao.int/safety/airnavigation/AIG/Documents/ADREP%20Taxonomy/ECCAIRS%20Aviation%201.3.0.12%20(Entities%20and%20Attributes).en.id.pdf',
    HOME_ROUTE: Routes.dashboard,
    OPTIONS: {
        OCCURRENCE_CLASS: 'occurrenceClass',
        OCCURRENCE_CATEGORY: 'occurrenceCategory',
        AUDIT_FINDING_STATUS: 'auditFindingStatus'
    },

    TIME_SCALES: {
        SECOND: 'second',
        MINUTE: 'minute',
        HOUR: 'hour',
        RELATIVE: 'relative'
    },
    TIME_SCALE_THRESHOLD: 100,

    /**
     * Types of message published by the MessageStore
     */
    MESSAGE_TYPE: {
        SUCCESS: 'success',
        INFO: 'info',
        WARNING: 'warning',
        ERROR: 'danger'
    },

    /**
     * URL of the remote BI solution providing statistics for the app
     */
    STATISTICS: {
        general: 'statistics.general',
        eventTypes: 'statistics.eventType',
        audit: 'statistics.audit',
        safetyIssue: 'statistics.safetyIssue'
    },

    STATISTICS_DASHBOARD: 'statistics.dashboard',

    /**
     * Sorting glyph icons
     */
    SORTING: {
        NO: {glyph: 'sort', title: 'sort.no'},
        ASC: {glyph: 'sort-by-$type$', title: 'sort.asc'},
        DESC: {glyph: 'sort-by-$type$-alt', title: 'sort.desc'}
    },

    UNAUTHORIZED_USER: {name: 'unauthorized'},

    FILTER_DEFAULT: 'all',

    MINUTE: 60 * 1000,   // Minute in milliseconds

    // Maximum number of columns supported by Bootstrap
    COLUMN_COUNT: 12,

    // Default page size (used by the PagingMixin)
    PAGE_SIZE: 20,

    // Maximum time difference between occurrence start and end. 24 hours in millis
    MAX_OCCURRENCE_START_END_DIFF: 1000 * 60 * 60 * 24,

    // Maximum input value length, for which input of type text should be displayed
    INPUT_LENGTH_THRESHOLD: 70,

    OCCURRENCE_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto',
    OCCURRENCE_SAFETY_ISSUE_BASE_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.safetyissue.OccurrenceBase',
    EVENT_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.event.EventDto',
    OCCURRENCE_REPORT_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto',
    OCCURRENCE_REPORT_LIST_ITEM_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto',
    SAFETY_ISSUE_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.event.SafetyIssueDto',
    SAFETY_ISSUE_REPORT_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.SafetyIssueReportDto',
    SAFETY_ISSUE_REPORT_LIST_ITEM_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.reportlist.SafetyIssueReportDto',
    AUDIT_REPORT_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.model.audit.AuditReport',
    AUDIT_REPORT_LIST_ITEM_JAVA_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.reportlist.AuditReportDto',
    AUDIT_FINDING_SAFETY_ISSUE_BASE_CLASS: 'cz.cvut.kbss.inbas.reporting.dto.safetyissue.AuditFindingBase',

    // Audit finding level, numbering starts at 1 and the maximum is included
    FINDING_LEVEL_MAX: 2,

    // Currently supported corrective measure attributes
    CORRECTIVE_MEASURE: {
        DESCRIPTION: 'description',
        DEADLINE: 'deadline',
        IMPLEMENTED: 'implemented'
    },

    SAFETY_ISSUE_STATE: {
        OPEN: 'http://onto.fel.cvut.cz/ontologies/reporting-tool/model/opened-safety-issue-state',
        CLOSED: 'http://onto.fel.cvut.cz/ontologies/reporting-tool/model/closed-safety-issue-state'
    },

    ARMS: {
        ACCIDENT_OUTCOME: 'accidentOutcome',
        BARRIER_EFFECTIVENESS: 'barrierEffectiveness'
    },

    SIRA_COLORS: {
        'http://onto.fel.cvut.cz/ontologies/arms/sira/model/stop': 'sira-red',
        'http://onto.fel.cvut.cz/ontologies/arms/sira/model/improve': 'sira-orange',
        'http://onto.fel.cvut.cz/ontologies/arms/sira/model/secure': 'sira-yellow',
        'http://onto.fel.cvut.cz/ontologies/arms/sira/model/monitor': 'sira-aqua',
        'http://onto.fel.cvut.cz/ontologies/arms/sira/model/accept': 'sira-green',
        'http://onto.fel.cvut.cz/ontologies/arms/sira/model/unassigned': ''
    },

    FULL_TEXT_SEARCH_OPTION: {
        id: 'full-text-search',
        identification: 'Full text search'
    },

    FILTERS: [{
        path: 'occurrenceCategories',
        type: 'select',
        options: 'occurrenceCategory',
        label: 'report.occurrence.category.label'
    }, {
        path: 'phase',
        type: 'select',
        options: 'reportingPhase',
        label: 'reports.phase'
    }, {
        path: 'types',
        type: 'select',
        options: 'reportType',
        label: 'reports.type.filter'
    }]
};
