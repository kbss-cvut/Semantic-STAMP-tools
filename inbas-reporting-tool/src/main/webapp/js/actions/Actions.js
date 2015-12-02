'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',
    'loadAllReports',
    'loadReports', 'findReport', 'createReport', 'updateReport', 'deleteReport',
    'loadInvestigations',
    'loadEventTypes', 'loadLvpOptions', 'loadOccurrenceSeverityOptions', 'loadLocations', 'loadOperators',
    'setTransitionPayload',
    'createInvestigation', 'findInvestigation', 'updateInvestigation', 'deleteInvestigation'
]);

module.exports = Actions;
