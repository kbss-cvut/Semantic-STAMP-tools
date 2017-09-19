'use strict';

/**
 * Internationalization store for access from non-react components and objects.
 */

let _messages = [];
let _intl = {};

module.exports = {

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
