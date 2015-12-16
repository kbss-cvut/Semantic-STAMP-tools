'use strict';

describe('I18nStore', function () {

    var React = require('react'),
        Environment = require('../environment/Environment'),
        InitialReports = require('../../js/components/initialreport/InitialReports'),// Just to have something to render
        messages = require('../../js/i18n/en').messages,
        store = require('../../js/stores/I18nStore');

    it('Provides access to the localized messages', function () {
        Environment.render(<InitialReports report={{}} onAttributeChange={function() {}}/>);

        var msg = store.i18n('save');
        expect(msg).toBeDefined();
        expect(msg).toEqual(messages['save']);
    });
});