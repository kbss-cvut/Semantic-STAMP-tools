'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',
    'loadReports', 'findReport', 'createReport', 'updateReport', 'deleteReport',
    'loadInvestigations',
    'loadEventTypes', 'loadLvpOptions', 'loadOccurrenceSeverityOptions', 'loadLocations', 'loadOperators',
    'setTransitionPayload',
    'createInvestigation', 'findInvestigation', 'updateInvestigation'
]);

module.exports = Actions;
