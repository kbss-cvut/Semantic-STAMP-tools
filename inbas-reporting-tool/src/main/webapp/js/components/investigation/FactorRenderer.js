'use strict';

var GanttController = require('./GanttController');

var FactorRenderer = {
    ganttController: GanttController,

    referenceIdsToGanttIds: {},     // JSON reference IDs to task IDs assigned by the GanttController
    greatestReferenceId: Number.MIN_VALUE,

    renderFactors: function (investigation) {
        var root = investigation.rootFactor,
            id = Date.now();
        this.ganttController.setOccurrenceEventId(id);
        this.referenceIdsToGanttIds[root.referenceId] = id;
        this.greatestReferenceId = root.referenceId;
        this.ganttController.addFactor({
            id: id,
            text: investigation.occurrence.name,
            start_date: new Date(root.startTime),
            end_date: new Date(root.endTime),
            statement: root,
            readonly: true
        }, null);
        if (root.children) {
            for (var i = 0, len = root.children.length; i < len; i++) {
                this.addChild(root.children[i], id);
            }
        }
        this.renderLinks(investigation);
    },

    addChild: function (factor, parentId) {
        if (factor.referenceId > this.greatestReferenceId) {
            this.greatestReferenceId = factor.referenceId;
        }
        var id = this.ganttController.addFactor({
            text: factor.assessment.eventType.name,
            start_date: new Date(factor.startTime),
            end_date: new Date(factor.endTime),
            parent: parentId,
            statement: factor
        }, parentId);
        this.referenceIdsToGanttIds[factor.referenceId] = id;
        if (factor.children) {
            for (var i = 0, len = factor.children.length; i < len; i++) {
                this.addChild(factor.children[i], id);
            }
        }
    },

    renderLinks: function (investigation) {
        if (!investigation.links) {
            return;
        }
        this.addLinks(investigation.links.causes, 'cause');
        this.addLinks(investigation.links.mitigates, 'mitigate');
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
