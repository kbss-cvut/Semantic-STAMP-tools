'use strict';

var GanttController = require('./GanttController');
var Vocabulary = require('../../constants/Vocabulary');

var FactorRenderer = {
    ganttController: GanttController,

    referenceIdsToGanttIds: {},     // JSON reference IDs to task IDs assigned by the GanttController
    greatestReferenceId: Number.MIN_VALUE,

    renderFactors: function (report) {
        var root = report.occurrence,
            id = Date.now();
        this.ganttController.setOccurrenceEventId(id);
        this.referenceIdsToGanttIds[root.referenceId] = id;
        this.greatestReferenceId = root.referenceId;
        this.ganttController.addFactor({
            id: id,
            text: root.name,
            start_date: new Date(root.startTime),
            end_date: new Date(root.endTime),
            statement: root,
            readonly: true
        }, null);
        if (report.factorGraph) {
            this.addNodes(root, report.factorGraph.nodes);
            this.addEdges(report.factorGraph.edges);
        }
    },

    addNodes: function (root, nodes) {
        var node,
            startDate = new Date(root.startTime),
            endDate = new Date(root.endTime);
        for (var i = 0, len = nodes.length; i < len; i++) {
            if (typeof(nodes[i]) === 'number' && nodes[i] === root.referenceId) {
                continue;   // JSON reference to the root
            }
            node = nodes[i];
            var id = this.ganttController.addFactor({
                text: node.eventType,   // TODO We should map event type ids to their name
                start_date: startDate,
                end_date: endDate,
                statement: node
            }, null);
            this.referenceIdsToGanttIds[node.referenceId] = id;
        }
    },

    addEdges: function (edges) {
        if (!edges) {
            return;
        }
        for (var i = 0, len = edges.length; i < len; i++) {
            if (edges[i].linkType === Vocabulary.HAS_PART) {
                this.addParent(edges[i]);
            } else {
                this.addLink(edges[i]);
            }
        }
    },

    addParent: function (edge) {
        this.ganttController.setFactorParent(this.referenceIdsToGanttIds[edge.to], this.referenceIdsToGanttIds[edge.from]);
    },

    addLinks: function (edge) {
        this.ganttController.addLink({
            source: this.referenceIdsToGanttIds[edge.from],
            target: this.referenceIdsToGanttIds[edge.to],
            factorType: edge.linkType
        });
    }
};

module.exports = FactorRenderer;
