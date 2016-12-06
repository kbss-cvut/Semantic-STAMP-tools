'use strict';

const Reflux = require('reflux');

module.exports = Reflux.createActions([
    'loadUser',

    'loadAllReports', 'deleteReportChain', 'createReport', 'updateReport', 'submitReport',
    'phaseTransition',
    'loadRevisions', 'loadReport',

    'loadOptions',

    'setTransitionPayload',

    'rememberComponentState', 'resetComponentState',

    'loadFormOptions',

    'loadStatistics',

    'publishMessage',

    'fullTextSearch'
]);
