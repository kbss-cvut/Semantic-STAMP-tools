'use strict';

const Cookies = require('js-cookie');
const Constants = require('../constants/Constants');

/**
 * Internationalization store for access from non-react components and objects.
 */

let _messages = [];
let _intl = {};
let _lang = {};

module.exports = {

    setActiveLanguage: function (lang) {
        _lang = lang;
        Cookies.set(Constants.LANG_COOKIE, lang);
    },

    getActiveLanguage: function () {
        return _lang;
    },

    setMessages: function (messages) {
        _messages = messages;
    },

    setIntl: function (intl) {
        _intl = intl;
    },

    i18n: function (messageId) {
        return _messages[messageId];
    },

    getIntl: function () {
        return _intl;
    }
};
