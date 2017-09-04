'use strict';

const Reflux = require('reflux');

module.exports = Reflux.createActions([
    'loadUser', 'updateUser',

    'loadAllReports', 'deleteReportChain', 'createReport', 'updateReport', 'submitReport', 'importInitialReport',
    'phaseTransition',
    'loadRevisions', 'loadReport',

    'loadOptions',

    'setTransitionPayload',

    'rememberComponentState', 'resetComponentState',

    'loadFormOptions',

    'loadStatistics',

    'publishMessage',

    'fullTextSearch', 'loadReportsForSearch'
]);
