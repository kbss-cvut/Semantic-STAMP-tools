/**
 * @jsx
 */

'use strict';

var React = require('react');
var IntlMixin = require('react-intl').IntlMixin;

var Input = require('../../../Input');

var Description = React.createClass({
    mixins: [IntlMixin],

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
                <Input type='textarea' rows='8' label={this.getIntlMessage('description')}
                       placeholder={this.getIntlMessage('preliminary.detail.corrective.description-placeholder')}
                       value={this.state.description} onChange={this.onChange}/>
            </div>
        );
    }
});

module.exports = Description;
