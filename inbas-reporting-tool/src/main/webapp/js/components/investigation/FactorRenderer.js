'use strict';

var GanttController = require('./GanttController');

var FactorRenderer = {
    ganttController: GanttController,

    jsonObjectMap: {},

    renderFactors(investigation) {
        var root = investigation.rootFactor,
            id = Date.now();
        this.ganttController.setOccurrenceEventId(id);
        var startDate = new Date(root.startTime),
            endDate = new Date(root.endTime);
        this.ganttController.addFactor({
            id: id,
            text: investigation.occurrence.name,
            start_date: startDate,
            end_date: endDate,
            readonly: true
        }, null);
        if (root.children) {
            for (var i = 0, len = root.children.length; i < len; i++) {
                this.addChild(root.children[i], id);
            }
        }
    },

    addChild: function (factor, parentId) {
        factor = this.resolveFactorFromJsonId(factor);
        var id = this.ganttController.addFactor({
            text: factor.assessment.eventType.name,
            start_date: new Date(factor.startTime),
            end_date: new Date(factor.endTime),
            parent: parentId,
            statement: factor
        }, parentId);
        if (factor.children) {
            for (var i = 0, len = factor.children.length; i < len; i++) {
                this.addChild(factor.children[i], id);
            }
        }
    },

    /**
     * This function deals with JSON id used by Jackson, which prevents object duplicates by using references to objects
     * in the JSON.
     * @param factor Possibly a JSON reference
     * @returns {*} Corresponding factor object
     */
    resolveFactorFromJsonId: function (factor) {
        if (typeof factor !== 'object') {
            factor = this.jsonObjectMap[factor];
        } else {
            this.jsonObjectMap[factor['@id']] = factor;
        }
        return factor;
    }
};

module.exports = FactorRenderer;
