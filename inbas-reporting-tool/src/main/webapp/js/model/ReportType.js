'use strict';

var React = require('react');
var assign = require('object-assign');
var JsonLdUtils = require('jsonld-utils').default;
var CollapsibleText = require('../components/CollapsibleText');
var Constants = require('../constants/Constants');
var SafetyIssueBase = require('./SafetyIssueBase').default;
var Utils = require('../utils/Utils');
var Vocabulary = require('../constants/Vocabulary');

class OccurrenceReport {
    constructor(data) {
        assign(this, data);
    }

    static getDetailController() {
        // Use require in method call to prevent circular dependencies with RevisionInfo
        return require('../components/report/occurrence/OccurrenceReportController');
    }

    getPhase(phaseMapping, intl) {
        if (!this.phase) {
            return '';
        }
        for (var i = 0, len = phaseMapping.length; i < len; i++) {
            if (phaseMapping[i]['@id'] === this.phase) {
                return JsonLdUtils.getLocalized(phaseMapping[i][Vocabulary.RDFS_LABEL], intl);
            }
        }
        return this.phase;
    }

    getLabel() {
        return 'occurrencereport.label';
    }

    toString() {
        return 'occurrencereport.title';
    }

    getHeadline() {
        return this.occurrence.name;
    }

    renderMoreInfo() {
        return <CollapsibleText text={this.summary}/>;
    }
}

class SafetyIssueReport {
    constructor(data) {
        assign(this, data);
    }

    static getDetailController() {
        return require('../components/report/safetyissue/SafetyIssueReportController');
    }

    getPhase() {
        return '';
    }

    getLabel() {
        return 'safetyissuereport.label';
    }

    toString() {
        return 'safetyissuereport.title';
    }

    getHeadline() {
        return this.safetyIssue.name;
    }

    renderMoreInfo() {
        return <CollapsibleText text={this.summary}/>;
    }

    /**
     * Adds the specified report as this safety issue report's base.
     * @param baseReport The new base
     */
    addBase(baseReport) {
        if (this.safetyIssue.basedOn) {
            if (this.safetyIssue.basedOn.find((item) => item.uri === baseReport.occurrence.uri)) {
                return false;
            }
            this.safetyIssue.basedOn.push(SafetyIssueBase.create(null, baseReport));
        } else {
            this.safetyIssue.basedOn = [SafetyIssueBase.create(null, baseReport)];
        }
        if (baseReport.factorGraph) {
            this._copyFactorGraph(baseReport);
            // Get rid of the original factor graph. We don't need it for safety issue persisting and it causes
            // deserialization issues
            delete baseReport.factorGraph;
        }
        return true;
    }

    _copyFactorGraph(source) {
        if (!this.factorGraph) {
            this.factorGraph = {
                nodes: [],
                edges: []
            };
            this.factorGraph.nodes.push(this.safetyIssue);
        }
        var referenceMap = {};
        referenceMap[source.factorGraph.nodes[0].referenceId] = this.safetyIssue;   //This is the occurrence
        var node, nodeClone;
        for (var i = 1, len = source.factorGraph.nodes.length; i < len; i++) {
            node = source.factorGraph.nodes[i];
            nodeClone = {
                referenceId: Utils.randomInt(),
                eventType: node.eventType,
                types: node.types,
                index: node.index,
                javaClass: node.javaClass
            };
            referenceMap[node.referenceId] = nodeClone;
            this.factorGraph.nodes.push(nodeClone);
        }
        var edge;
        for (i = 0, len = source.factorGraph.edges.length; i < len; i++) {
            edge = source.factorGraph.edges[i];
            this.factorGraph.edges.push({
                linkType: edge.linkType,
                from: referenceMap[edge.from.referenceId],
                to: referenceMap[edge.to.referenceId]
            });
        }
    }
}

class AuditReport {
    constructor(data) {
        assign(this, data);
    }

    static getDetailController() {
        // Use require in method call to prevent circular dependencies with RevisionInfo
        return require('../components/report/audit/AuditReportController');
    }

    getPhase() {
        return '';
    }

    getLabel() {
        return 'auditreport.label';
    }

    toString() {
        return 'auditreport.title';
    }

    getHeadline() {
        return this.audit.name;
    }

    renderMoreInfo() {
        return <CollapsibleText text={this.summary}/>;
    }
}

var REPORT_TYPES = {};

REPORT_TYPES[Vocabulary.OCCURRENCE_REPORT] = OccurrenceReport;
REPORT_TYPES[Constants.OCCURRENCE_REPORT_JAVA_CLASS] = OccurrenceReport;
REPORT_TYPES[Constants.OCCURRENCE_REPORT_LIST_ITEM_JAVA_CLASS] = OccurrenceReport;
REPORT_TYPES[Vocabulary.SAFETY_ISSUE_REPORT] = SafetyIssueReport;
REPORT_TYPES[Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS] = SafetyIssueReport;
REPORT_TYPES[Constants.SAFETY_ISSUE_REPORT_LIST_ITEM_JAVA_CLASS] = SafetyIssueReport;
REPORT_TYPES[Vocabulary.AUDIT_REPORT] = AuditReport;
REPORT_TYPES[Constants.AUDIT_REPORT_JAVA_CLASS] = AuditReport;
REPORT_TYPES[Constants.AUDIT_REPORT_LIST_ITEM_JAVA_CLASS] = AuditReport;

var ReportType = {

    getDetailController: function (report) {
        return this._getReportClass(report).getDetailController();
    },

    getTypeLabel: function (type) {
        return REPORT_TYPES[type] ? new REPORT_TYPES[type]().getLabel() : null;
    },

    getReport: function (data, suppressError) {
        var cls = this._getReportClass(data);
        if (!suppressError && !cls) {
            throw 'Unsupported report type ' + data;
        }
        return cls ? new cls(data) : null;
    },

    _getReportClass: function (data) {
        if (data.types) {
            for (var i = 0, len = data.types.length; i < len; i++) {
                if (REPORT_TYPES[data.types[i]]) {
                    return REPORT_TYPES[data.types[i]];
                }
            }
        }
        if (data.javaClass && REPORT_TYPES[data.javaClass]) {
            return REPORT_TYPES[data.javaClass];
        }
        return null;
    }
};

module.exports = ReportType;
