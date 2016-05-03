'use strict';

var GanttController = require('./GanttController');
var Vocabulary = require('../../constants/Vocabulary');
var EventTypeFactory = require('../../model/EventTypeFactory');

var FactorRenderer = {
    ganttController: GanttController,

    referenceIdsToGanttIds: {},     // JSON reference IDs to task IDs assigned by the GanttController
    greatestReferenceId: Number.MIN_VALUE,

    renderFactors: function (report, eventTypes) {
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
            this._addNodes(root, report.factorGraph.nodes, eventTypes);
            this._addEdges(report.factorGraph.edges);
            this._applyUpdates();
        }
    },

    _addNodes: function (root, nodes, eventTypes) {
        var node;
        for (var i = 0, len = nodes.length; i < len; i++) {
            if (typeof(nodes[i]) === 'number' && nodes[i] === root.referenceId) {
                continue;   // JSON reference to the root
            }
            node = nodes[i];
            var eventType = EventTypeFactory.resolveEventType(node.eventType, eventTypes);
            this.referenceIdsToGanttIds[node.referenceId] = this.ganttController.addFactor({
                text: eventType ? eventType[Vocabulary.RDFS_LABEL] : node.eventType,
                start_date: new Date(node.startTime),
                end_date: new Date(node.endTime),
                statement: node
            }, null);
            if (this.greatestReferenceId < node.referenceId) {
                this.greatestReferenceId = node.referenceId;
            }
        }
    },

    _addEdges: function (edges) {
        if (!edges) {
            return;
        }
        for (var i = 0, len = edges.length; i < len; i++) {
            if (edges[i].linkType === Vocabulary.HAS_PART) {
                this._addParent(edges[i]);
            } else {
                this._addLink(edges[i]);
            }
        }
    },

    _addParent: function (edge) {
        this.ganttController.setFactorParent(this.referenceIdsToGanttIds[edge.to], this.referenceIdsToGanttIds[edge.from]);
    },

    _addLink: function (edge) {
        this.ganttController.addLink({
            source: this.referenceIdsToGanttIds[edge.from],
            target: this.referenceIdsToGanttIds[edge.to],
            factorType: edge.linkType
        });
    },

    /**
     * Applies updates to the gantt component.
     *
     * This is necessary for example for the part-of relationships, which are added to the nodes after rendering
     * @private
     */
    _applyUpdates: function () {
        var updates = [],
            keys = Object.getOwnPropertyNames(this.referenceIdsToGanttIds);
        for (var i = 0, len = keys.length; i < len; i++) {
            updates.push(this.referenceIdsToGanttIds[keys[i]]);
        }
        this.ganttController.applyUpdates(updates, true);
    }
};

module.exports = FactorRenderer;
