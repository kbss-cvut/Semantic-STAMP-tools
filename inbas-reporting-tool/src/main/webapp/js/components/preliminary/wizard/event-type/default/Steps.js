'use strict';

var EventDescription = require('./EventDescription');
var I18nStore = require('../../../../../stores/I18nStore');

module.exports = [
    {
        name: I18nStore.i18n('eventtype.default.description-placeholder'),
        component: EventDescription
    }
];
