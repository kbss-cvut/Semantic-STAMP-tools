'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',

    'loadAllReports', 'deleteReportChain', 'createPreliminary', 'createInvestigation', 'updateReport', 'submitReport',
    'loadRevisions', 'loadReport',

    'loadOptions', 'loadEventTypes', 'loadLocations', 'loadOperators', 'loadOccurrenceCategories',

    'setTransitionPayload'
]);

module.exports = Actions;
