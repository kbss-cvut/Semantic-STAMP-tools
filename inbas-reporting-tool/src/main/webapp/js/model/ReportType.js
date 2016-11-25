'use strict';

const React = require('react');
const assign = require('object-assign');
const JsonLdUtils = require('jsonld-utils').default;
const CollapsibleText = require('../components/CollapsibleText');
const Constants = require('../constants/Constants');
const Vocabulary = require('../constants/Vocabulary');
const ArmsUtils = require('../utils/ArmsUtils').default;
const I18nStore = require('../stores/I18nStore');
const SafetyIssueBase = require('./SafetyIssueBase').default;
const Utils = require('../utils/Utils');

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
        for (let i = 0, len = phaseMapping.length; i < len; i++) {
            if (phaseMapping[i]['@id'] === this.phase) {
                return JsonLdUtils.getLocalized(phaseMapping[i][Vocabulary.RDFS_LABEL], intl);
            }
        }
        return this.phase;
    }

    getPrimaryLabel() {
        return 'occurrencereport.label';
    }

    /**
     * Returns all the labels this report type supports.
     * @return {[string]}
     */
    getLabels() {
        return ['occurrencereport.label'];
    }

    /**
     * Gets a CSS class representing status of this report.
     *
     * More specifically, it represents ARMS index evaluation.
     */
    getStatusCssClass() {
        return ArmsUtils.resolveArmsIndexClass(this.armsIndex);
    }

    /**
     * Gets textual information about the status of this report.
     *
     * More specifically, it returns text containing the ARMS index value.
     */
    getStatusInfo() {
        return this.armsIndex ? I18nStore.i18n('arms.index.tooltip') + this.armsIndex : '';
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

    /**
     * Gets a CSS class representing status of this report.
     *
     * More specifically, it represents safety issue risk assessment (SIRA).
     */
    getStatusCssClass() {
        return this.sira ? Constants.SIRA_COLORS[this.sira] : '';
    }

    /**
     * Gets textual information about the status of this report.
     *
     * More specifically, it returns text containing the SIRA value.
     */
    getStatusInfo(options, intl) {
        if (this.sira) {
            for (var i = 0, len = options.length; i < len; i++) {
                if (this.sira === options[i]['@id']) {
                    return JsonLdUtils.getLocalized(options[i][Vocabulary.RDFS_LABEL], intl);
                }
            }
        }
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
     * @param newBase The new base
     * @param report Report documenting the new base
     */
    addBase(newBase, report) {
        if (this.safetyIssue.basedOn) {
            if (this.safetyIssue.basedOn.find((item) => item.uri === newBase.uri)) {
                return false;
            }
            this.safetyIssue.basedOn.push(SafetyIssueBase.create(newBase, report));
        } else {
            this.safetyIssue.basedOn = [SafetyIssueBase.create(newBase, report)];
        }
        if (report && report.factorGraph) {
            this._copyFactorGraph(report);
        }
        if (newBase.factors) {
            this._addFactorsToFactorGraph(newBase);
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
                eventTypes: node.eventTypes,
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

    _addFactorsToFactorGraph(finding) {
        if (!this.factorGraph) {
            this.factorGraph = {
                nodes: [],
                edges: []
            };
            this.factorGraph.nodes.push(this.safetyIssue);
        }
        var node;
        for (var i = 0, len = finding.factors.length; i < len; i++) {
            node = {
                eventTypes: [finding.factors[i]],
                types: [finding.factors[i]],
                referenceId: Utils.randomInt(),
                javaClass: Constants.EVENT_JAVA_CLASS
            };
            this.factorGraph.nodes.push(node);
            this.factorGraph.edges.push({
                from: this.factorGraph.nodes[0],
                to: node,
                linkType: Vocabulary.HAS_PART
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

    /**
     * Gets a CSS class representing status of this report.
     *
     * Currently, audit reports do not have any kind of status representation, so this method returns an empty string.
     */
    getStatusCssClass() {
        return '';
    }

    /**
     * Gets textual information about this report's status.
     *
     * Currently, audit reports do not have any kind of status representation, so this method returns an empty string.
     */
    getStatusInfo() {
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

const REPORT_TYPES = {};

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
        return REPORT_TYPES[type] ? new REPORT_TYPES[type]().getPrimaryLabel() : null;
    },

    getReport: function (data, suppressError) {
        const cls = this._getReportClass(data);
        if (!suppressError && !cls) {
            throw 'Unsupported report type ' + data;
        }
        return cls ? new cls(data) : null;
    },

    _getReportClass: function (data) {
        if (data.types) {
            for (let i = 0, len = data.types.length; i < len; i++) {
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
