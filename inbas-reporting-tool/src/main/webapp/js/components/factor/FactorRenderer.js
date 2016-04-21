'use strict';

var GanttController = require('./GanttController');

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
        }
    },

    addNodes: function(root, nodes) {
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

    addLinks: function(links, linkType) {
        if (!links) {
            return;
        }
        var from, to;
        for (var i = 0, len = links.length; i < len; i++) {
            from = links[i].from;
            to = links[i].to;
            this.ganttController.addLink({
                source: this.referenceIdsToGanttIds[from],
                target: this.referenceIdsToGanttIds[to],
                factorType: linkType
            });
        }
    }
};

module.exports = FactorRenderer;
