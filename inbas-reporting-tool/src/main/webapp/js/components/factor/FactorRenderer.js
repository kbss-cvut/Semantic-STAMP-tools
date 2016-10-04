'use strict';

var JsonLdUtils = require('jsonld-utils').default;
var GanttController = require('./GanttController');
var Constants = require('../../constants/Constants');
var Vocabulary = require('../../constants/Vocabulary');
var EventTypeFactory = require('../../model/EventTypeFactory');

var FactorRenderer = {

    greatestReferenceId: Number.MIN_VALUE,

    renderFactors: function (report, eventTypes) {
        this.greatestReferenceId = Number.MIN_VALUE;
        if (report.occurrence) {
            OccurrenceReportFactorRenderer.renderFactors(report, eventTypes);
        } else if (report.safetyIssue) {
            SafetyIssueFactorRenderer.renderFactors(report, eventTypes);
        } else {
            FactorRendererImpl.renderFactors(report.factorGraph, eventTypes);
        }
    }
};

/**
 * Renderer for OccurrenceReports.
 *
 * It needs to add the occurrence to the factor graph.
 */
var OccurrenceReportFactorRenderer = {

    renderFactors: function (report, eventTypes) {
        RootAddingFactorRenderer.renderFactors(report, eventTypes, 'occurrence');
    }
};

/**
 * Renderer for safety issues.
 *
 * It needs to add the safety issue to the factor graph
 */
var SafetyIssueFactorRenderer = {

    renderFactors: function (report, eventTypes) {
        this._generateTimesForNodes(report);
        RootAddingFactorRenderer.renderFactors(report, eventTypes, 'safetyIssue');
    },

    _generateTimesForNodes: function (report) {
        GanttController.setScale(Constants.TIME_SCALES.SECOND);
        var start = Date.now(),
            end = start + 1000,
            factorGraph = report.factorGraph;
        report.safetyIssue.startTime = start;
        report.safetyIssue.endTime = end;
        if (factorGraph) {
            // TODO Set the times so that the layout puts factors of other events before them
            for (var i = 0, len = factorGraph.nodes.length; i < len; i++) {
                if (typeof(factorGraph.nodes[i]) === 'object') {
                    factorGraph.nodes[i].startTime = start;
                    factorGraph.nodes[i].endTime = end;
                }
            }
        }
        GanttController.setScale(Constants.TIME_SCALES.RELATIVE);
    }
};

var RootAddingFactorRenderer = {
    renderFactors: function (report, eventTypes, rootAttribute) {
        if (!report[rootAttribute].referenceId) {
            report[rootAttribute].referenceId = Date.now();
        }
        var factorGraph = report.factorGraph;
        if (factorGraph) {
            var ind = factorGraph.nodes.indexOf(report[rootAttribute].referenceId);
            if (ind !== -1) {
                factorGraph.nodes[ind] = report[rootAttribute];
            }
        } else {
            report.factorGraph = {
                nodes: [report[rootAttribute]]
            };
        }
        report[rootAttribute].readOnly = true;
        GanttController.setRootEventId(report[rootAttribute].referenceId);
        FactorRendererImpl.renderFactors(report.factorGraph, eventTypes);
    }
};

/**
 * This does the actual factor graph rendering.
 *
 * Use decorators to do any necessary setup before the rendering.
 */
var FactorRendererImpl = {

    renderFactors: function (factorGraph, eventTypes) {
        if (!factorGraph) {
            return;
        }
        var edges = this._processEdges(factorGraph.edges);
        this._addNodes(factorGraph.nodes, edges.partOfHierarchy, eventTypes);
        this._addLinks(edges.links);
    },

    _processEdges: function (edges) {
        var nodesToParents = {};
        var links = [];
        if (edges) {
            for (var i = 0, len = edges.length; i < len; i++) {
                if (edges[i].linkType === Vocabulary.HAS_PART) {
                    nodesToParents[edges[i].to.referenceId] = edges[i].from.referenceId;
                } else {
                    links.push(edges[i]);
                }
            }
        }
        return {
            partOfHierarchy: nodesToParents,
            links: links
        };
    },

    _addNodes: function (nodes, partOfHierarchy, eventTypes) {
        var node;
        for (var i = 0, len = nodes.length; i < len; i++) {
            node = nodes[i];
            var text = '';
            if (typeof node.name !== 'undefined' && node.name !== null) {
                text = node.name;
            } else if (node.eventType) {
                var eventType = EventTypeFactory.resolveEventType(node.eventType, eventTypes);
                text = eventType ? JsonLdUtils.getJsonAttValue(eventType, Vocabulary.RDFS_LABEL) : node.eventType;
            }
            GanttController.addFactor({
                id: node.referenceId,
                text: text,
                start_date: new Date(node.startTime),
                end_date: new Date(node.endTime),
                readonly: node.readOnly,
                statement: node
            }, partOfHierarchy[node.referenceId]);
            if (FactorRenderer.greatestReferenceId < node.referenceId) {
                FactorRenderer.greatestReferenceId = node.referenceId;
            }
        }
    },

    _addLinks: function (links) {
        for (var i = 0, len = links.length; i < len; i++) {
            GanttController.addLink({
                source: links[i].from.referenceId,
                target: links[i].to.referenceId,
                factorType: links[i].linkType
            });
        }
    }
};

module.exports = FactorRenderer;
