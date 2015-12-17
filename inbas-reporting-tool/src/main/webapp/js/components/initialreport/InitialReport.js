/**
 * @jsx
 */

'use strict';

var React = require('react');
var injectIntl = require('../../utils/injectIntl');

var Input = require('../Input');
var I18nMixin = require('../../i18n/I18nMixin');

var InitialReport = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
        var text = this.props.data.initialReport.text;
        return {
            text: text ? text : ''
        };
    },

    onChange: function (e) {
        var value = e.target.value,
            initialReport = this.props.data.initialReport;
        initialReport.text = value;
        this.setState({text: value});
    },

    render: function () {
        return (
            <div>
                <Input type='textarea' rows='15' label={this.i18n('initial.label')}
                       placeholder={this.i18n('initial.label')} value={this.state.text} onChange={this.onChange}/>
            </div>
        );
    }
});

module.exports = injectIntl(InitialReport);
