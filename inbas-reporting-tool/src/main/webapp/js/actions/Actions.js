'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',

    'loadAllReports', 'deleteReportChain', 'createReport', 'updateReport', 'submitReport',
    'phaseTransition',
    'loadRevisions', 'loadReport',

    'loadOptions', 'loadEventTypes', 'loadOccurrenceCategories',

    'setTransitionPayload',
    
    'rememberComponentState', 'resetComponentState',

    'loadFormOptions',

    'loadStatistics',

    'publishMessage'
]);

module.exports = Actions;
