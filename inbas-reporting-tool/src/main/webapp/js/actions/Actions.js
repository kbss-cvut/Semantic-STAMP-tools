'use strict';

const Reflux = require('reflux');

module.exports = Reflux.createActions([
    'loadUser',

    'loadAllReports', 'deleteReportChain', 'createReport', 'updateReport', 'submitReport', 'importE5Report', 'importSafaExcel',
    'phaseTransition',
    'loadRevisions', 'loadReport', 'loadEccairsReport', 'newRevisionFromLatestEccairs',

    'calculateArmsIndex', 'calculateSira',

    'findLatestEccairsVersion',

    'loadOptions',

    'setTransitionPayload',

    'rememberComponentState', 'resetComponentState',

    'loadFormOptions',

    'loadStatistics',

    'publishMessage',

    'addSafetyIssueBase',

    'fullTextSearch', 'loadReportsForSearch',

    'loadStatisticsConfig'
]);
