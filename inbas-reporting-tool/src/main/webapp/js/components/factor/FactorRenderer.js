'use strict';

const JsonLdUtils = require('jsonld-utils').default;
const GanttController = require('./GanttController');
const Vocabulary = require('../../constants/Vocabulary');
const ObjectTypeResolver = require('../../utils/ObjectTypeResolver');
const Constants = require('../../constants/Constants');

const FactorRenderer = {

    greatestReferenceId: Number.MIN_VALUE,

    renderFactors: function (report, eventTypes) {
        this.greatestReferenceId = Number.MIN_VALUE;
        if (report.occurrence) {
            OccurrenceReportFactorRenderer.renderFactors(report, eventTypes);
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
const OccurrenceReportFactorRenderer = {

    renderFactors: function (report, eventTypes) {
        RootAddingFactorRenderer.renderFactors(report, eventTypes, 'occurrence');
    }
};

const RootAddingFactorRenderer = {
    renderFactors: function (report, eventTypes, rootAttribute) {
        if (!report[rootAttribute].referenceId) {
            report[rootAttribute].referenceId = Date.now();
        }
        const factorGraph = report.factorGraph;
        if (factorGraph) {
            const ind = factorGraph.nodes.indexOf(report[rootAttribute].referenceId);
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
const FactorRendererImpl = {

    renderFactors: function (factorGraph, eventTypes) {
        if (!factorGraph) {
            return;
        }
        const edges = this._processEdges(factorGraph.edges);
        this._addNodes(factorGraph.nodes, edges.partOfHierarchy, eventTypes);
        this._addLinks(edges.links);
    },

    _processEdges: function (edges) {
        const nodesToParents = {};
        const links = [];
        if (edges) {
            for (let i = 0, len = edges.length; i < len; i++) {
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
        let node;
        const root = nodes.filter(function(n){return n.javaClass == Constants.OCCURRENCE_JAVA_CLASS})[0];

        const map = {};
        nodes.forEach(function(n){map[n.referenceId] = n;});

        const childrenOf = {};
        Object.entries(partOfHierarchy).forEach(function ([k,v]){if(!childrenOf[v]){childrenOf[v] = []}; childrenOf[v].push(k)})

        const nodesToProcess = [];
        // const parents = Object.values(partOfHierarchy);
        let currentLayer = [root.referenceId];//Object.keys(partOfHierarchy).filter(function(n){ return parents.includes(parseInt(n));});
        // currentLayer.push(root);
        while(currentLayer.length > 0){
            let newLayer = [];
            for(let i = 0; i < currentLayer.length; i++){
                nodesToProcess.push(map[currentLayer[i]]);
                let children = childrenOf[currentLayer[i]];
                if(children && children.length > 0)
                    newLayer = newLayer.concat(children);
            }
            currentLayer = newLayer;
        }

        for (let i = 0, len = nodesToProcess.length; i < len; i++) {
            node = nodesToProcess[i];
            let text = '';
            if (typeof node.name !== 'undefined' && node.name !== null) {
                text = node.name;
            } else if (node.eventType) {
                const eventType = ObjectTypeResolver.resolveType(node.eventType, eventTypes);
                text = eventType ? JsonLdUtils.getJsonAttValue(eventType, Vocabulary.RDFS_LABEL) : node.eventType;
            }
            GanttController.addFactor({
                id: node.referenceId,
                text: text,
                // Temporary fix for gantt issue with 0 as task start/end time
                start_date: new Date(node.startTime === 0 ? node.startTime + 1 : node.startTime),
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
        for (let i = 0, len = links.length; i < len; i++) {
            GanttController.addLink({
                source: links[i].from.referenceId,
                target: links[i].to.referenceId,
                factorType: links[i].linkType,
                uri: links[i].uri
            });
        }
    }
};

module.exports = FactorRenderer;
