'use strict';

var Configuration = require('semforms').Configuration;
var WizardGenerator = require('semforms').WizardGenerator;

var Ajax = require('../../../utils/Ajax');
var Actions = require('../../../actions/Actions');
var FormGenStore = require('../../../stores/FormGenStore');
var I18nStore = require('../../../stores/I18nStore');
var Logger = require('../../../utils/Logger');
var Input = require('../../Input').default;
var TypeaheadResultList = require('../../typeahead/TypeaheadResultList');
var Utils = require('../../../utils/Utils');
var WizardStore = require('../../../stores/WizardStore');

const EVENT_PARAM = 'event';
const EVENT_TYPE_PARAM = 'eventType';
const FORM_GEN_URL = 'rest/formGen';

module.exports = {

    generateSummaryWizard: function(report, wizardTitle, renderCallback) {
        Ajax.post(FORM_GEN_URL, report).end((data) => {
            Configuration.actions = Actions;
            Configuration.wizardStore = WizardStore;
            Configuration.optionsStore = FormGenStore;
            Configuration.intl = I18nStore.getIntl();
            Configuration.typeaheadResultList = TypeaheadResultList;
            Configuration.inputComponent = Input;
            WizardGenerator.createWizard(data, {}, wizardTitle, renderCallback);
        }, () => {
            Logger.log('Received no valid wizard. Using the default one.');
            WizardGenerator.createDefaultWizard({}, wizardTitle, renderCallback);
        });
    },

    generateWizard: function (report, event, wizardTitle, renderCallback) {
        var url = this._initUrlWithParameters(event);
        Ajax.post(url, report).end((data) => {
            Configuration.actions = Actions;
            Configuration.wizardStore = WizardStore;
            Configuration.optionsStore = FormGenStore;
            Configuration.intl = I18nStore.getIntl();
            Configuration.typeaheadResultList = TypeaheadResultList;
            Configuration.inputComponent = Input;
            WizardGenerator.createWizard(data, event.question, wizardTitle, renderCallback);
        }, () => {
            Logger.log('Received no valid wizard. Using the default one.');
            WizardGenerator.createDefaultWizard(event.question, wizardTitle, renderCallback);
        });
    },

    _initUrlWithParameters: function (event) {
        var params = {};
        params[EVENT_TYPE_PARAM] = encodeURIComponent(event.eventType);
        params[EVENT_PARAM] = event.referenceId;
        return Utils.addParametersToUrl(FORM_GEN_URL, params);
    }
};
