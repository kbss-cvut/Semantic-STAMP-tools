'use strict';

/**
 * Internationalization store for access from non-react components and objects.
 */

var _messages = [];

var I18nStore = {

    setMessages: function (messages) {
        _messages = messages;
    },

    i18n: function (messageId) {
        return _messages[messageId];
    }
};

module.exports = I18nStore;
