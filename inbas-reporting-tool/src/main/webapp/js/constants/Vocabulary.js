'use strict';

/**
 * Ontological vocabulary used by JSON-LD responses we get.
 */
module.exports = {
    RDFS_LABEL: 'http://www.w3.org/2000/01/rdf-schema#label',
    RDFS_COMMENT: 'http://www.w3.org/2000/01/rdf-schema#comment',

    OCCURRENCE_REPORT: 'http://onto.fel.cvut.cz/ontologies/documentation/occurrence_report',
    SAFETY_ISSUE_REPORT: 'http://onto.fel.cvut.cz/ontologies/documentation/safety_issue_report',
    AUDIT_REPORT: 'http://onto.fel.cvut.cz/ontologies/documentation/audit_report',

    OCCURRENCE: 'http://onto.fel.cvut.cz/ontologies/aviation-safety/Occurrence',
    AUDIT_FINDING: 'http://onto.fel.cvut.cz/ontologies/audit/audit_finding',

    HAS_PART: 'http://onto.fel.cvut.cz/ontologies/ufo/has_part',
    TRANSITION_LABEL: 'http://onto.fel.cvut.cz/ontologies/documentation/transition_label',

    GREATER_THAN: 'http://onto.fel.cvut.cz/ontologies/documentation/is_higher_than'
};
