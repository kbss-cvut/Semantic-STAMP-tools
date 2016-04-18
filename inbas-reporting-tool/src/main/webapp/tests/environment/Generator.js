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
    generateOccurrenceReport: function () {
        return {
            key: this.getRandomInt().toString(),
            occurrenceStart: Date.now() - 10000,
            occurrenceEnd: Date.now(),
            occurrenceCategory: {
                id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200110',
                name: '2200110 - Incursions generally',
                description: 'Blablabla'
            },
            revision: 1,
            occurrence: {
                key: this.getRandomInt().toString(),
                name: 'TestOccurrence'
            }
        };
    },

    /**
     * Gets a preliminary report with occurrence and an initial report.
     */
    generatePreliminaryReport: function () {
        return {
            phase: Constants.PRELIMINARY_REPORT_PHASE,
            key: this.getRandomInt().toString(),
            occurrence: {key: this.getRandomInt().toString()},
            initialReports: [{text: 'First Initial Report'}],
            occurrenceStart: Date.now() - 10000,
            occurrenceEnd: Date.now(),
            occurrenceCategory: {
                id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200110',
                name: '2200110 - Incursions generally',
                description: 'Blablabla'
            },
            revision: 1
        }
    },

    /**
     * Generates random integer between 0 (included) and max(excluded).
     * @param max [optional] Maximum generated number, optional. If not specified, max safe integer value is used.
     * @return {number}
     */
    getRandomInt: function (max) {
        var min = 0,
            bound = max ? max : Number.MAX_SAFE_INTEGER;
        return Math.floor(Math.random() * (bound - min)) + min;
    },

    /**
     * Generates a random number of evenly distributed preliminary and investigation reports.
     */
    generateReports: function () {
        var count = this.getRandomInt(100),
            reports = [],
            report,
            preliminary = true,
            categories = this.getCategories();
        for (var i = 0; i < count; i++) {
            if (preliminary) {
                report = this.generatePreliminaryReport();
            } else {
                report = this.generateOccurrenceReport();
            }
            report.uri = 'http://www.inbas.cz/reporting-tool/reports#Instance' + i;
            report.occurrenceCategory = categories[this.getRandomInt(categories.length)];
            reports.push(report);
            preliminary = !preliminary;
        }
        return reports;
    },

    getCategories: function () {
        return [
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-1",
                "description": "1 - AMAN: Abrupt maneuvre",
                "name": "1 - AMAN: Abrupt maneuvre"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-10",
                "description": "10 - ICE: Icing",
                "name": "10 - ICE: Icing"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-100",
                "description": "100 - UIMC: Unintended flight in IMC",
                "name": "100 - UIMC: Unintended flight in IMC"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-101",
                "description": "101 - EXTL: External load related occurrences",
                "name": "101 - EXTL: External load related occurrences"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-102",
                "description": "102 - CTOL: Collision with obstacle(s) during take-off and landing",
                "name": "102 - CTOL: Collision with obstacle(s) during take-off and landing"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-103",
                "description": "103 - LOLI: Loss of lifting conditions en-route",
                "name": "103 - LOLI: Loss of lifting conditions en-route"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-104",
                "description": "104 - GTOW: Glider towing related events",
                "name": "104 - GTOW: Glider towing related events"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-11",
                "description": "11 - LALT: Low altitude operations",
                "name": "11 - LALT: Low altitude operations"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-12",
                "description": "12 - LOC-G: Loss of control - ground",
                "name": "12 - LOC-G: Loss of control - ground"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-13",
                "description": "13 - LOC-I: Loss of control - inflight",
                "name": "13 - LOC-I: Loss of control - inflight"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-14",
                "description": "14 - MAC: Airprox/ ACAS alert/ loss of separation/ (near) midair collisions",
                "name": "14 - MAC: Airprox/ ACAS alert/ loss of separation/ (near) midair collisions"
            },
            {
                "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-15",
                "description": "15 - RE: Runway excursion",
                "name": "15 - RE: Runway excursion"
            }];
    }
};

module.exports = Generator;
