'use strict';

var GanttController = require('./GanttController');

var FactorRenderer = {
    ganttController: GanttController,

    jsonObjectMap: {},
    factorsToIds: {}, // Maps factor objects to the ids assigned by the GanttController

    renderFactors: function (investigation) {
        var root = investigation.rootFactor,
            id = Date.now();
        this.ganttController.setOccurrenceEventId(id);
        this.ganttController.addFactor({
            id: id,
            text: investigation.occurrence.name,
            start_date: new Date(root.startTime),
            end_date: new Date(root.endTime),
            readonly: true
        }, null);
        if (root.children) {
            for (var i = 0, len = root.children.length; i < len; i++) {
                this.addChild(root.children[i], id);
            }
        }
        this.renderLinks(root);
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
            this.jsonObjectMap[factor['@id']] = factor;
        }
        return factor;
    },

    renderLinks: function (factor) {
        factor = this.resolveFactorFromJsonId(factor);
        if (factor.causes) {
            this.renderCauses(factor);
        }
        if (factor.mitigatingFactors) {
            this.renderMitigatingFactors(factor);
        }
        if (factor.children) {
            for (var i = 0, len = factor.children.length; i < len; i++) {
                this.renderLinks(factor.children[i]);
            }
        }
    },

    renderCauses: function (factor) {
        this.addLinks(factor, 'causes', 'cause');
    },

    addLinks: function(factor, linksField, linkType) {
        var linkStart, linkStartId;
        for (var i = 0, len = factor[linksField].length; i < len; i++) {
            linkStart = this.resolveFactorFromJsonId(factor[linksField][i]);
            linkStartId = this.factorsToIds[linkStart];
            this.ganttController.addLink({
                from: linkStartId,
                to: this.factorsToIds[factor],
                factorType: linkType
            });
        }
    },

    renderMitigatingFactors: function(factor) {
        this.addLinks(factor, 'mitigatingFactors', 'mitigate');
    }
};

module.exports = FactorRenderer;
