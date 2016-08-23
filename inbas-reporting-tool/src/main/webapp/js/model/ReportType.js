'use strict';

var React = require('react');
var assign = require('object-assign');
var JsonLdUtils = require('jsonld-utils').default;
var CollapsibleText = require('../components/CollapsibleText');
var Constants = require('../constants/Constants');
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

    toReportListItem() {
        var result = assign({}, this);
        delete result.occurrence;
        delete result.factorGraph;
        delete result.correctiveMeasures;
        result.identification = this.occurrence.name;
        result.date = this.occurrence.startTime;
        result.occurrenceCategory = this.occurrence.eventType;
        result.javaClass = Constants.OCCURRENCE_REPORT_LIST_ITEM_JAVA_CLASS;
        return result;
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

    toReportListItem() {
        var result = assign({}, this);
        delete result.safetyIssue;
        delete result.factorGraph;
        delete result.correctiveMeasures;
        result.identification = this.safetyIssue.name;
        result.javaClass = Constants.SAFETY_ISSUE_REPORT_LIST_ITEM_JAVA_CLASS;
        return result;
    }

    /**
     * Adds the specified report as this safety issue report's base.
     * @param baseReport The new base
     */
    addBase(baseReport) {
        if (this.safetyIssue.basedOn) {
            if (this.safetyIssue.basedOn.find((item) => item.key === baseReport.key)) {
                return false;
            }
            this.safetyIssue.basedOn.push(SafetyIssueReport._reportToListItem(baseReport));
        } else {
            this.safetyIssue.basedOn = [SafetyIssueReport._reportToListItem(baseReport)];
        }
        if (baseReport.factorGraph) {
            this._copyFactorGraph(baseReport);
            // Get rid of the original factor graph. We don't need it for safety issue persisting and it causes
            // deserialization issues
            delete baseReport.factorGraph;
        }
        return true;
    }

    static _reportToListItem(baseReport) {
        return ReportType.getReport(baseReport).toReportListItem();
    }

    _copyFactorGraph(source) {
        if (!this.factorGraph) {
            this.factorGraph = {
                nodes: [],
                edges: []
            };
            this.factorGraph.nodes.push(this.safetyIssue.referenceId);
        }
        var referenceMap = {};
        referenceMap[source.factorGraph.nodes[0].referenceId] = this.safetyIssue.referenceId;
        var node, newReferenceId;
        for (var i = 1, len = source.factorGraph.nodes.length; i < len; i++) {
            node = source.factorGraph.nodes[i];
            newReferenceId = Utils.randomInt();
            referenceMap[node.referenceId] = newReferenceId;
            this.factorGraph.nodes.push({
                referenceId: newReferenceId,
                eventType: node.eventType,
                types: node.types,
                index: node.index,
                javaClass: node.javaClass
            });
        }
        var edge;
        for (i = 0, len = source.factorGraph.edges.length; i < len; i++) {
            edge = source.factorGraph.edges[i];
            this.factorGraph.edges.push({
                linkType: edge.linkType,
                from: referenceMap[edge.from],
                to: referenceMap[edge.to]
            });
        }
    }
}
var REPORT_TYPES = {};

REPORT_TYPES[Vocabulary.OCCURRENCE_REPORT] = OccurrenceReport;
REPORT_TYPES[Constants.OCCURRENCE_REPORT_JAVA_CLASS] = OccurrenceReport;
REPORT_TYPES[Constants.OCCURRENCE_REPORT_LIST_ITEM_JAVA_CLASS] = OccurrenceReport;
REPORT_TYPES[Vocabulary.SAFETY_ISSUE_REPORT] = SafetyIssueReport;
REPORT_TYPES[Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS] = SafetyIssueReport;
REPORT_TYPES[Constants.SAFETY_ISSUE_REPORT_LIST_ITEM_JAVA_CLASS] = SafetyIssueReport;

module.exports = {

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
