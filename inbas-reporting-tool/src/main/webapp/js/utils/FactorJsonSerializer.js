'use strict';

var FactorJsonSerializer = {
    ganttController: null,

    definedFactors: {},

    setGanttController: function(controller) {
        this.ganttController = controller;
    },

    getFactorHierarchy: function() {
        if (!this.ganttController) {
            throw 'Missing Gantt controller!';
        }
        var root = this.ganttController.getFactor(this.ganttController.occurrenceEventId).statement;
        root.children = this._getChildren(this.ganttController.occurrenceEventId);
        return root;
    },

    _getChildren: function(parentId) {
        var childFactors = this.ganttController.getChildren(parentId),
            children = [];
        for (var i = 0, len = childFactors.length; i < len; i++) {
            var statement = childFactors[i].statement;
            statement.children = this._getChildren(childFactors[i].id);
            children.push(statement);
        }
        return children;
    }
};

module.exports = FactorJsonSerializer;
