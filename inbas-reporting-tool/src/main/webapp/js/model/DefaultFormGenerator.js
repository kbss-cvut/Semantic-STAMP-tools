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
        return require('./defaultForm.json');
    }
};
