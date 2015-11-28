'use strict';

var Constants = require('../../js/constants/Constants');

/**
 * Generates test data.
 */
var Generator = {

    /**
     * Generates a binary tree of factors of the specified depth.
     *
     * Factors have referenceIds assigned, starting from 0 at the root and going as a BFS traversal of the three.
     * @param startTime Start time, used for all factors
     * @param endTime End time, used for all factors
     * @param maxDepth Tree depth, not including the root
     */
    generateFactors: function (startTime, endTime, maxDepth) {
        var idCounter = {counter: 0},
            root = {
                startTime: startTime,
                endTime: endTime,
                referenceId: idCounter.counter++
            },
            level = 0;
        this._generateChildren(root, level, maxDepth, idCounter);
        return root;
    },

    _generateChildren: function (parent, depth, maxDepth, idCounter) {
        if (depth >= maxDepth) {
            return;
        }
        parent.children = this._getChildNodes(parent.startTime, parent.endTime);
        for (var i = 0, len = parent.children.length; i < len; i++) {
            parent.children[i].referenceId = idCounter.counter++;
        }
        for (i = 0, len = parent.children.length; i < len; i++) {
            this._generateChildren(parent.children[i], depth + 1, maxDepth, idCounter);
        }

    },

    _getChildNodes: function (startTime, endTime) {
        return [
            {
                startTime: startTime,
                endTime: endTime,
                assessment: {
                    eventType: {
                        id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200110',
                        name: '2200110 - Incursions generally',
                        type: 'http://onto.fel.cvut.cz/ontologies/eccairs/model/event-type'
                    },
                    description: 'Event type assessment with description only.'
                }
            },
            {
                startTime: startTime,
                endTime: endTime,
                assessment: {
                    eventType: {
                        id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100',
                        name: '2200100 - Runway incursions',
                        type: 'http://onto.fel.cvut.cz/ontologies/eccairs/model/event-type'
                    },
                    runwayIncursion: {
                        lowVisibilityProcedure: 'NONE',
                        intruder: {
                            aircraft: {
                                'uri': 'http://krizik.felk.cvut.cz/ontologies/inbas-2015#Aircraft_instance994092607',
                                registration: 'OK123',
                                callSign: 'OK123',
                                flightNumber: '1234',
                                flightPhase: 'taxi_to_runway',
                                operationType: 'passenger'
                            }
                        }
                    }
                }
            }
        ]
    },

    generateGanttTasksForFactors: function (rootFactor, ganttController) {
        var idToTask = {}, parentToChildren = {};
        ganttController.occurrenceEventId = rootFactor.referenceId;
        idToTask[rootFactor.referenceId] = {id: rootFactor.referenceId, statement: rootFactor};
        this._generateTasks(rootFactor, idToTask, parentToChildren, ganttController);
        ganttController.getFactor.and.callFake(function (id) {
            return idToTask[id].statement;
        });
        ganttController.getChildren.and.callFake(function (parent) {
            var children = parentToChildren[parent];
            return children ? children : [];
        });
    },

    _generateTasks: function (parent, idToTask, parentToChildren, ganttController) {
        if (!parent.children) {
            return;
        }
        var children = parent.children,
            childTasks = [];
        for (var i = 0, len = children.length; i < len; i++) {
            var task = {
                id: children[i].referenceId,
                statement: children[i]
            };
            idToTask[children[i].referenceId] = task;
            childTasks.push(task);
            this._generateTasks(children[i], idToTask, parentToChildren, ganttController);
        }
        parentToChildren[parent.referenceId] = childTasks;
    },

    /**
     * Generates links between factors in the tree.
     *
     * Links are created only between siblings, a complete binary tree is assumed. Left subtree gets a causality link,
     * right subtree gets mitigation subtree.
     * @param rootFactor
     * @param ganttController
     */
    generateGanttLinksForFactors: function (rootFactor, ganttController) {
        if (!rootFactor.children) {
            return;
        }
        var links = [];
        this._generateLink(rootFactor, links, Constants.LINK_TYPES.CAUSE);
        ganttController.getLinks.and.returnValue(links);
    },

    _generateLink: function (factor, links, linkType) {
        if (!factor.children) {
            return;
        }
        links.push({
            id: Date.now(),
            source: factor.children[0].referenceId,
            target: factor.children[1].referenceId,
            factorType: linkType
        });
        this._generateLink(factor.children[0], links, Constants.LINK_TYPES.CAUSE);
        this._generateLink(factor.children[1], links, Constants.LINK_TYPES.MITIGATE);
    },

    /**
     * Gets an investigation object with occurrence and root factor.
     */
    generateInvestigation: function () {
        return {
            occurrence: {
                name: 'TestOccurrence',
                startTime: Date.now() - 10000,
                endTime: Date.now()
            },
            rootFactor: {
                startTime: Date.now() - 10000,
                endTime: Date.now()
            }
        };
    }
};

module.exports = Generator;
