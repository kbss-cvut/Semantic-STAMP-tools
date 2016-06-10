'use strict';

var jsonld = require('jsonld');

var Actions = require('../../../actions/Actions');
var Ajax = require('../../../utils/Ajax');
var Constants = require('../../../constants/Constants');
var FormUtils = require('./FormUtils').default;
var Logger = require('../../../utils/Logger');
var Utils = require('../../../utils/Utils');
var Vocabulary = require('../../../constants/Vocabulary');
var GeneratedStep = require('./GeneratedStep').default;

var WizardGenerator = {

    generateWizard: function (report, parameters, wizardTitle, renderCallback) {
        var uri = 'rest/formGen';
        if (parameters) {
            uri += '?';
            Object.getOwnPropertyNames(parameters).forEach(function (param) {
                uri += param + '=' + parameters[param] + '&';   // '&' at the end of request URI should not be a problem
            });
        }
        var data = require('../../../../sample-eccairs-form.json');
        if (!data) {
            Ajax.post(uri, report).end(function
                (data) {
                this._createWizard(data, wizardTitle, renderCallback);
            }.bind(this));
        } else {
            this._createWizard(data, wizardTitle, renderCallback);
        }
    },

    _createWizard: function (structure, title, renderCallback) {
        jsonld.frame(structure, {}, function (err, framed) {
            if (err) {
                Logger.error(err);
            }
            var wizardProperties = {
                steps: this._constructWizardSteps(framed),
                title: title
            };
            renderCallback(wizardProperties);
        }.bind(this));
    },

    _constructWizardSteps: function (structure) {
        var form = structure['@graph'],
            formElements,
            item,
            steps = [],
            i, len;

        for (i = 0, len = form.length; i < len; i++) {
            item = form[i];
            if (FormUtils.isForm(item)) {
                form = item;
                break;
            }
        }
        formElements = form[Constants.FORM.HAS_SUBQUESTION];
        if (!formElements) {
            Logger.error('Could not find any wizard steps in the received data.');
            return [];
        }
        for (i = 0, len = formElements.length; i < len; i++) {
            item = formElements[i];
            if (FormUtils.isWizardStep(item) && !FormUtils.isHidden(item)) {
                steps.push({
                    name: Utils.getJsonAttValue(item, Vocabulary.RDFS_LABEL),
                    component: GeneratedStep,
                    data: item
                });
            }
        }
        // TODO Temporary sorting
        steps.sort(function (a, b) {
            if (a.name < b.name) {
                return 1;
            } else if (a.name > b.name) {
                return -1;
            }
            return 0;
        });
        Actions.initWizard(null, steps.map((item) => {
            return item.data;
        }));
        return steps;
    }
};

module.exports = WizardGenerator;
