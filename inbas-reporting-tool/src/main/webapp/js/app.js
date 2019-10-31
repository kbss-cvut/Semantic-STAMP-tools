/**
 Main entry point for the ReactJS frontend
 */

import React from "react";
import ReactDOM from "react-dom";
import Cookies from "js-cookie";
import {addLocaleData} from "react-intl";
import Constants from "./constants/Constants";
import I18nStore from "./stores/I18nStore";

let intlData = null;

function selectLocalization() {
    // Load react-intl locales
    if ('ReactIntlLocaleData' in window) {
        Object.keys(ReactIntlLocaleData).forEach(function (lang) {
            addLocaleData(ReactIntlLocaleData[lang]);
        });
    }
    const prefLang = Cookies.get(Constants.LANG_COOKIE);
    const lang = prefLang ? prefLang : navigator.language;
    if (lang && lang === 'cs' || lang === 'cs-CZ' || lang === 'sk' || lang === 'sk-SK') {
        intlData = require('./i18n/cs');
        I18nStore.setActiveLanguage(Constants.LANG.CS);
    } else {
        intlData = require('./i18n/en');
        I18nStore.setActiveLanguage(Constants.LANG.EN);
    }
}

function main() {
    const Main = require('./Main').default;
    ReactDOM.render(<Main intlData={intlData}/>, document.getElementById('content'));
}

selectLocalization();
I18nStore.setMessages(intlData.messages);
main();
