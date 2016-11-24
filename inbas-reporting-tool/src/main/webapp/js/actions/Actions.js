'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',

    'loadAllReports', 'deleteReportChain', 'createReport', 'updateReport', 'submitReport', 'importE5Report',
    'phaseTransition',
    'loadRevisions', 'loadReport', 'loadEccairsReport', 'newRevisionFromLatestEccairs',

    'calculateArmsIndex', 'calculateSira',

    'loadOptions',

    'setTransitionPayload',
    
    'rememberComponentState', 'resetComponentState',

    'loadFormOptions',

    'loadStatistics',

    'publishMessage',

    'addSafetyIssueBase',

    'fullTextSearch'
]);

module.exports = Actions;
