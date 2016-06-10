'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',

    'loadAllReports', 'deleteReportChain', 'createReport', 'updateReport', 'submitReport', 'importE5Report',
    'phaseTransition',
    'loadRevisions', 'loadReport',

    'loadOptions', 'loadEventTypes', 'loadLocations', 'loadOperators', 'loadOccurrenceCategories',

    'setTransitionPayload',

    // Wizard actions
    'initWizard', 'updateData', 'updateStepData', 'insertStep', 'removeStep'
]);

module.exports = Actions;
