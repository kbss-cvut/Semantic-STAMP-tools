/**
 Main entry point for the ReactJS frontend
 */

'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

const I18nStore = require('./stores/I18nStore');
const addLocaleData = require('react-intl').addLocaleData;
const intlShim = require('./utils/intlShim');

let intlData = null;

function selectLocalization() {
    // Load react-intl locales
    if ('ReactIntlLocaleData' in window) {
        Object.keys(ReactIntlLocaleData).forEach(function (lang) {
            addLocaleData(ReactIntlLocaleData[lang]);
        });
    }
    const lang = navigator.language;
    if (lang && lang === 'cs' || lang === 'cs-CZ' || lang === 'sk' || lang === 'sk-SK') {
        intlData = require('./i18n/cs');
    } else {
        intlData = require('./i18n/en');
    }
}

function main() {
    const Main = require('./Main').default;
    ReactDOM.render(<Main intlData={intlData}/>, document.getElementById('content'));
}

intlShim(() => {
    selectLocalization();
    I18nStore.setMessages(intlData.messages);
    main();
});
