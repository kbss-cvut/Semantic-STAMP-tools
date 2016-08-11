'use strict';

var Constants = require('../constants/Constants');
var Vocabulary = require('../constants/Vocabulary');
var Utils = require('../utils/Utils');

module.exports = {

    createReport: function (type, options) {
        switch (type) {
            // Intentional fall-through
            case Vocabulary.OCCURRENCE_REPORT:
            case Constants.OCCURRENCE_REPORT_JAVA_CLASS:
                return this.createOccurrenceReport(options);
            case Vocabulary.SAFETY_ISSUE_REPORT:
            case Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS:
                return this.createSafetyIssueReport(options);
            default:
                throw 'Unsupported report type ' + type;
        }
    },

    createOccurrenceReport: function () {
        return {
            occurrence: {
                javaClass: Constants.OCCURRENCE_JAVA_CLASS,
                referenceId: Utils.randomInt(),
                name: '',
                // Round the time to whole seconds
                startTime: (Date.now() / 1000) * 1000,
                endTime: (Date.now() / 1000) * 1000
            },
            isNew: true,
            javaClass: Constants.OCCURRENCE_REPORT_JAVA_CLASS
        };
    },

    createSafetyIssueReport: function (options) {
        var report = {
            safetyIssue: {
                javaClass: Constants.SAFETY_ISSUE_JAVA_CLASS,
                referenceId: Utils.randomInt(),
                name: ''
            },
            isNew: true,
            javaClass: Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS
        };
        this._enhanceSafetyIssueWithOptions(report, options);
        return report;
    },

    _enhanceSafetyIssueWithOptions: function (report, options) {
        if (!options) {
            return;
        }
        if (options.basedOn) {
            var basedOn = options.basedOn;
            report.safetyIssue.basedOn = [basedOn];
            if (basedOn.factorGraph) {
                this._copyFactorGraph(report, basedOn);
                // Get rid of the original factor graph. We don't need it for safety issue persisting and it causes
                // deserialization issues
                delete basedOn.factorGraph;
            }
        }
    },

    _copyFactorGraph: function (target, source) {
        target.factorGraph = {
            nodes: [],
            edges: []
        };
        target.factorGraph.nodes.push(target.safetyIssue.referenceId);
        var referenceMap = {};
        referenceMap[source.factorGraph.nodes[0].referenceId] = target.safetyIssue.referenceId;
        var node, newReferenceId;
        for (var i = 1, len = source.factorGraph.nodes.length; i < len; i++) {
            node = source.factorGraph.nodes[i];
            newReferenceId = Utils.randomInt();
            referenceMap[node.referenceId] = newReferenceId;
            target.factorGraph.nodes.push({
                referenceId: newReferenceId,
                eventType: node.eventType,
                types: node.types,
                index: node.index,
                javaClass: node.javaClass
            });
        }
        var edge;
        for (i = 0, len = source.factorGraph.edges.length; i < len; i++) {
            edge = source.factorGraph.edges[i];
            target.factorGraph.edges.push({
                linkType: edge.linkType,
                from: referenceMap[edge.from],
                to: referenceMap[edge.to]
            });
        }
    },

    createFactor: function () {
        return {
            javaClass: Constants.EVENT_JAVA_CLASS
        }
    }
};
