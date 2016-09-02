'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',

    'loadAllReports', 'deleteReportChain', 'createReport', 'updateReport', 'submitReport', 'importE5Report',
    'phaseTransition',
    'loadRevisions', 'loadReport',

    'loadOptions', 'loadEventTypes', 'loadOccurrenceCategories',

    'setTransitionPayload',
    
    'rememberComponentState', 'resetComponentState',

    'loadFormOptions',

    'loadStatistics',

    'publishMessage',

    'addSafetyIssueBase'
]);

module.exports = Actions;
