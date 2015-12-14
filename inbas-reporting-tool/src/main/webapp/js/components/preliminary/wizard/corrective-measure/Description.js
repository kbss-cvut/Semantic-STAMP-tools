/**
 * @jsx
 */

'use strict';

var React = require('react');
var injectIntl = require('react-intl').injectIntl;

var Input = require('../../../Input');
var I18nMixin = require('../../../../i18n/I18nMixin');

var Description = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
        var description = this.props.data.statement.description;
        return {
            description: description ? description : ''
        };
    },
    onChange: function (e) {
        var value = e.target.value,
            statement = this.props.data.statement;
        statement.description = value;
        this.setState({description: value});
    },
    render: function () {
        return (
            <div>
                <Input type='textarea' rows='8' label={this.i18n('description')}
                       placeholder={this.i18n('preliminary.detail.corrective.description-placeholder')}
                       value={this.state.description} onChange={this.onChange}/>
            </div>
        );
    }
});

module.exports = injectIntl(Description);
