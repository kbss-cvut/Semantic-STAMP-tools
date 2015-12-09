'use strict';

var React = require('react');
var en = require('../../js/i18n/en');

/**
 * Test application. Used to initialize Intl context by loading the default English localization.
 */
var TestApp = React.createClass({

    render: function () {
        return <IntlProvider {...en}>
            {this.props.children}
        </IntlProvider>
    }
});

module.exports = TestApp;
