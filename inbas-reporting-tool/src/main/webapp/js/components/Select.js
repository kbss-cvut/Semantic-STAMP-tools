/**
 * @jsx
 */

'use strict';

var React = require('react');
var injectIntl = require('../utils/injectIntl');

var Input = require('./Input');
var I18nMixin = require('../i18n/I18nMixin');

var Select = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        options: React.PropTypes.array,
        name: React.PropTypes.string,
        title: React.PropTypes.string,
        label: React.PropTypes.string,
        onChange: React.PropTypes.func
    },

    focus: function () {
        this.refs.select.focus();
    },

    generateOptions: function () {
        var options = [];
        var len = this.props.options.length;
        for (var i = 0; i < len; i++) {
            var option = this.props.options[i];
            options.push(<option key={'opt_' + option.value} value={option.value}
                                 title={option.title}>{option.label}</option>);
        }
        return options;
    },

    render: function () {
        var options = this.generateOptions();
        return (
            <Input ref='select' type='select' name={this.props.name} title={this.props.title} label={this.props.label}
                   value={this.props.value} onChange={this.props.onChange}>
                <option key='opt_default' value='' disabled defaultValue
                        style={{display: 'none'}}>{this.i18n('select.default')}</option>
                {options}
            </Input>
        );
    }
});

module.exports = injectIntl(Select);
