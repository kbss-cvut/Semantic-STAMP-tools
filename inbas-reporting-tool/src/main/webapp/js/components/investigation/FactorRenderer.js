'use strict';

var GanttController = require('./GanttController');

var FactorRenderer = {
    ganttController: GanttController,

    jsonObjectMap: {},
    factorsToIds: {}, // Maps factor objects to the ids assigned by the GanttController
    greatestReferenceId: Number.MIN_VALUE,

    renderFactors: function (investigation) {
        var root = investigation.rootFactor,
            id = Date.now();
        this.ganttController.setOccurrenceEventId(id);
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
        factor = this.resolveFactorFromJsonId(factor);
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
        this.factorsToIds[factor] = id;
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
            this.jsonObjectMap[factor.referenceId] = factor;
        }
        return factor;
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
            from = this.resolveFactorFromJsonId(links[i].from);
            to = this.resolveFactorFromJsonId(links[i].to);
            this.ganttController.addLink({
                from: this.factorsToIds[from],
                to: this.factorsToIds[to],
                factorType: linkType
            });
        }
    }
};

module.exports = FactorRenderer;
