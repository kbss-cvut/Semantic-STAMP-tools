'use strict';

const Reflux = require('reflux');

module.exports = Reflux.createActions([
    'loadUsers', 'loadUser', 'updateUser', 'unlockUser', 'enableUser', 'disableUser',

    'loadAllReports', 'deleteReportChain', 'createReport', 'updateReport', 'submitReport', 'importInitialReport',
    'phaseTransition',
    'loadRevisions', 'loadReport',

    'loadOptions', 'loadProcessFlow',

    'replaceSchema', 'mergeSchema', 'fetchSchemaMetadata',

    'setTransitionPayload',

    'rememberComponentState', 'resetComponentState',

    'loadFormOptions',

    'loadStatistics',

    'publishMessage',

    'fullTextSearch', 'loadReportsForSearch'
]);
