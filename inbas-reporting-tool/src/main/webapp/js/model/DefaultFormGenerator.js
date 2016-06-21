'use strict';

var Constants = require('../constants/Constants');
var I18nStore = require('../stores/I18nStore');
var Vocabulary = require('../constants/Vocabulary');

var en = require('../i18n/en');
var cs = require('../i18n/cs');

module.exports = {

    /**
     * Generates default form for the wizard framework.
     *
     * The form consists of a single step, which contains one text area for the description.
     */
    generateForm() {
        var form = {}, step = {}, question = {}, answer = {};
        form[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.FORM];
        form[Constants.FORM.HAS_SUBQUESTION] = [step];
        step[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.WIZARD_STEP, Constants.FORM.LAYOUT.QUESTION_SECTION];
        step[Vocabulary.RDFS_LABEL] = [{
            '@language': 'en',
            '@value': en.messages['form.default.step-label']
        }, {
            '@language': 'cs',
            '@value': cs.messages['form.default.step-label']
        }];
        step[Constants.FORM.HAS_SUBQUESTION] = [question];
        question[Vocabulary.RDFS_LABEL] = [{
            '@language': 'en',
            '@value': en.messages['form.default.question']
        }, {
            '@language': 'cs',
            '@value': cs.messages['form.default.question']
        }];
        question[Constants.FORM.LAYOUT_CLASS] = [Constants.FORM.LAYOUT.TEXTAREA];
        question[Constants.FORM.HAS_ANSWER] = answer;
        answer[Constants.FORM.HAS_DATA_VALUE] = '';

        return {'@graph': [form]};
    }
};
