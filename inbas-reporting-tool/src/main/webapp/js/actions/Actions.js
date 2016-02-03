'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',

    'loadAllReports', 'deleteReportChain', 'createPreliminary', 'createInvestigation', 'updateReport', 'submitReport',
    'loadRevisions', 'loadReport',

    'loadEventTypes', 'loadLvpOptions', 'loadOccurrenceSeverityOptions', 'loadLocations', 'loadOperators',

    'setTransitionPayload'
]);

module.exports = Actions;
