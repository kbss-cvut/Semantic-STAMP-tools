'use strict';

var React = require('react');
var en = require('../../js/i18n/en');

/**
 * Test application. Used to initialize Intl context by loading the default English localization.
 */
var TestApp = React.createClass({

    childContextTypes: {
        locales: React.PropTypes.array,
        messages: React.PropTypes.object
    },

    getChildContext: function () {
        return {
            locales: en.locales,
            messages: en.messages
        }
    },

    render: function () {
        return <div>
            {this.props.children}
        </div>
    }
});

module.exports = TestApp;
