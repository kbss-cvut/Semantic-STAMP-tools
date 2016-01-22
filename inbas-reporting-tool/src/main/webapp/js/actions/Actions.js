'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',

    'loadAllReports', 'deleteReport',

    'loadPreliminaries', 'findPreliminary', 'createPreliminary', 'updatePreliminary', 'deletePreliminary', 'submitPreliminary',
    'loadPreliminaryRevisions',

    'loadInvestigations', 'createInvestigation', 'findInvestigation', 'updateInvestigation', 'deleteInvestigation', 'submitInvestigation',
    'loadInvestigationRevisions',

    'loadEventTypes', 'loadLvpOptions', 'loadOccurrenceSeverityOptions', 'loadLocations', 'loadOperators',

    'setTransitionPayload'
]);

module.exports = Actions;
