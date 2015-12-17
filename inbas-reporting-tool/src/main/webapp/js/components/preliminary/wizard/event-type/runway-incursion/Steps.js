'use strict';

var LowVisibilityProcedureStep = require('./LowVisibilityProcedureStep');
var IncursionLocationStep = require('./IncursionLocationStep');
var WasConflictingAircraftStep = require('./WasConflictingAircraftStep');
var RunwayIntruderStep = require('./RunwayIntruderStep');
var I18nStore = require('../../../../../stores/I18nStore');

module.exports = [
    {
        name: I18nStore.i18n('eventtype.incursion.lvp.label'),
        component: LowVisibilityProcedureStep,
        id: 'lvp'
    },
    {
        name: I18nStore.i18n('eventtype.incursion.location.step-title'),
        component: IncursionLocationStep,
        id: 'location'
    },
    {
        name: I18nStore.i18n('eventtype.incursion.intruder.step-title'),
        component: RunwayIntruderStep,
        defaultNextDisabled: true,
        id: 'intruder'
    },
    {
        name: I18nStore.i18n('eventtype.incursion.wasconflicting.step-title'),
        component: WasConflictingAircraftStep,
        id: 'wasConflict'
    }
];
