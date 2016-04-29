'use strict';

var Vocabulary = require('../constants/Vocabulary');

var FactorJsonSerializer = {
    ganttController: null,
    ganttIdsToNodes: null,

    setGanttController: function (controller) {
        this.ganttController = controller;
    },

    getFactorGraph: function (report) {
        this.verifyGanttControllerIsSet();
        this.ganttIdsToNodes = {};
        var nodes = this._getNodes(),
            edges = this._getEdges();
        // TODO We will want to use reference to the occurrence instance, which is already present in report
        return {
            nodes: nodes,
            edges: edges
        };
    },

    verifyGanttControllerIsSet: function () {
        if (!this.ganttController) {
            throw 'Missing Gantt controller!';
        }
    },

    _getNodes: function () {
        var nodes = [],
            me = this;
        this.ganttController.forEach((item) => {
            var node = item.statement;
            node.startTime = item.start_date.getTime();
            node.endTime = item.end_date.getTime();
            me.ganttIdsToNodes[item.id] = node;
            nodes.push(node);
        });
        return nodes;
    },

    _getEdges: function () {
        var edges = [];
        Array.prototype.push.apply(edges, this._resolvePartOfHierarchy());
        Array.prototype.push.apply(edges, this._resolveFactorLinks());
        return edges;
    },

    _resolvePartOfHierarchy: function () {
        var partOfEdges = [];
        this.ganttController.forEach((item) => {
            var children = this.ganttController.getChildren(item.id);
            if (children.length === 0) {
                return;
            }
            for (var i = 0, len = children.length; i < len; i++) {
                partOfEdges.push({
                    from: this.ganttIdsToNodes[item.id].referenceId,
                    to: this.ganttIdsToNodes[children[i].id].referenceId,
                    linkType: Vocabulary.HAS_PART
                });
            }
        });
        return partOfEdges;
    },

    _resolveFactorLinks: function () {
        var links = this.ganttController.getLinks(),
            edges = [];
        for (var i = 0, len = links.length; i < len; i++) {
            edges.push({
                from: this.ganttIdsToNodes[links[i].source],
                to: this.ganttIdsToNodes[links[i].target],
                linkType: links[i].factorType
            });
        }
        return edges;
    }
};

module.exports = FactorJsonSerializer;
