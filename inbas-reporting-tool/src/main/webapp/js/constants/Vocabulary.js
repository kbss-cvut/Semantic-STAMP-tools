'use strict';

/**
 * Ontological vocabulary used by JSON-LD responses we get.
 */
module.exports = {
    RDFS_LABEL: 'http://www.w3.org/2000/01/rdf-schema#label',
    RDFS_COMMENT: 'http://www.w3.org/2000/01/rdf-schema#comment',

    OCCURRENCE_REPORT: 'http://onto.fel.cvut.cz/ontologies/aviation-safety/occurrence_report',

    HAS_PART: 'http://onto.fel.cvut.cz/ontologies/ufo/has_part',
    TRANSITION_LABEL: 'http://onto.fel.cvut.cz/ontologies/documentation/transition_label',
    SUGGESTED: "http://onto.fel.cvut.cz/ontologies/reporting-tool/model/suggested-by-text-analysis",

    ROLE_ADMIN: 'http://onto.fel.cvut.cz/ontologies/reporting-tool/model/admin',

    LOCKED: 'http://onto.fel.cvut.cz/ontologies/reporting-tool/model/locked',
    DISABLED: 'http://onto.fel.cvut.cz/ontologies/reporting-tool/model/disabled',

    HAS_STRUCTURE_PART: 'http://onto.fel.cvut.cz/ontologies/stamp/has-control-structure-element-part',

    LOSS_EVENT_TYPE: 'http://onto.fel.cvut.cz/ontologies/stamp/loss-event-type',
    LOSS_EVENT: 'http://onto.fel.cvut.cz/ontologies/stamp/loss-event',

    EVENT_TYPE: 'http://onto.fel.cvut.cz/ontologies/ufo/event-type',

    FACTOR_EVENT_TYPE: 'http://onto.fel.cvut.cz/ontologies/stamp/unsafe-event',
    EVENT_FLOW_NEXT: 'http://onto.fel.cvut.cz/ontologies/stamp/next',

    HAS_PARTICIPANT: 'http://onto.fel.cvut.cz/ontologies/ufo/has-participant',

    FLOW_CONTROL_EVENT_TYPE: 'http://onto.fel.cvut.cz/ontologies/bpmn/flow-control-event-type',
    START_EVENT_TYPE: 'http://onto.fel.cvut.cz/ontologies/bpmn/process-start',
    END_EVENT_TYPE: 'http://onto.fel.cvut.cz/ontologies/bpmn/process-end'
};
