'use strict';

describe('I18nStore', function () {

    const React = require('react'),
        Environment = require('../environment/Environment'),
        Login = require('../../js/components/login/Login').default,// Just to have something to render
        messages = require('../../js/i18n/en').messages,
        store = require('../../js/stores/I18nStore');

    it('Provides access to the localized messages', function () {
        Environment.render(<Login/>);

        const msg = store.i18n('save');
        expect(msg).toBeDefined();
        expect(msg).toEqual(messages['save']);
    });
});