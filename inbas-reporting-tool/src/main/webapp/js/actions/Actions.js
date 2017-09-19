'use strict';

const Reflux = require('reflux');

module.exports = Reflux.createActions([
    'loadUsers', 'loadUser', 'updateUser', 'unlockUser',

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
