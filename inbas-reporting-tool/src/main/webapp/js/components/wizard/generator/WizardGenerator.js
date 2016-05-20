'use strict';

var jsonld = require('jsonld');

var Ajax = require('../../../utils/Ajax');
var Constants = require('../../../constants/Constants');
var Logger = require('../../../utils/Logger');
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
        Ajax.post(uri, report).end(function
            (data) {
            this._createWizard(data, wizardTitle, renderCallback);
        }.bind(this));
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
            item,
            steps = [],
            i, len;

        for (i = 0, len = form.length; i < len; i++) {
            item = form[i];
            if (item['@type'] && this._isForm(item)) {
                form = item[Constants.HAS_SECTION];
                break;
            }
        }
        for (i = 0, len = form.length; i < len; i++) {
            item = form[i];
            if (!this._isFormElement(item)) {
                continue;
            }
            if (this._isWizardStep(item)) {
                steps.push({
                    name: item[Vocabulary.RDFS_LABEL],
                    component: GeneratedStep,
                    data: {
                        structure: item
                    }
                });
            }
        }
        return steps;
    },

    _isFormElement: function (item) {
        return item['@type'] && (this._isSection(item) || this._isQuestion(item));
    },

    _isForm: function (item) {
        return item['@type'].indexOf(Constants.FORM) !== -1;
    },

    _isWizardStep: function (item) {
        //TODO layout-class is not string (add type)
        return this._isSection(item) && item[Constants.LAYOUT_CLASS];
    },

    _isSection: function (item) {
        return item['@type'].indexOf(Constants.SECTION) !== -1;
    },

    _isQuestion: function (item) {
        return item['@type'].indexOf(Constants.QUESTION) !== -1;
    }
};

module.exports = WizardGenerator;
