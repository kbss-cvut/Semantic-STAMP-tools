'use strict';

var Constants = require('../constants/Constants');

var FactorJsonSerializer = {
    ganttController: null,

    setGanttController: function (controller) {
        this.ganttController = controller;
    },
    
    getFactorGraph: function(report) {
        this.verifyGanttControllerIsSet();
        report.occurrence.referenceId = 1;
        // TODO
        return {
            nodes: [1],
            edges: []
        };
    },

    getFactorHierarchy: function () {
        this.verifyGanttControllerIsSet();
        var root = this.ganttController.getFactor(this.ganttController.occurrenceEventId);
        root.children = this._getChildren(this.ganttController.occurrenceEventId);
        return root;
    },

    verifyGanttControllerIsSet: function () {
        if (!this.ganttController) {
            throw 'Missing Gantt controller!';
        }
    },

    _getChildren: function (parentId) {
        var childFactors = this.ganttController.getChildren(parentId),
            children = [];
        for (var i = 0, len = childFactors.length; i < len; i++) {
            var factor = childFactors[i];
            factor.children = this._getChildren(childFactors[i].id);
            children.push(factor.statement);
        }
        return children;
    },

    getLinks: function () {
        this.verifyGanttControllerIsSet();
        var links = {
            causes: [],
            mitigates: []
        };
        this._resolveLinks(links);
        if (links.causes.length === 0 && links.mitigates.length === 0) {
            return null;
        }
        return links;
    },

    _resolveLinks: function (links) {
        var ganttLinks = this.ganttController.getLinks();
        for (var i = 0, len = ganttLinks.length; i < len; i++) {
            var ganttLink = ganttLinks[i],
                from = this.ganttController.getFactor(ganttLink.source),
                to = this.ganttController.getFactor(ganttLink.target);
            if (!from || !to) {
                console.error('Unable to find source or target of link ' + ganttLink);
            }
            var link = {
                from: from.referenceId,
                to: to.referenceId
            };
            if (ganttLink.factorType === Constants.LINK_TYPES.CAUSE) {
                links.causes.push(link);
            } else {
                links.mitigates.push(link);
            }
        }
    }
};

module.exports = FactorJsonSerializer;
