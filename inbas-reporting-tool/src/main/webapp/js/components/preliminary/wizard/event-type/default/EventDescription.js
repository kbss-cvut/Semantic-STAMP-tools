/**
 * @jsx
 */

'use strict';

var React = require('react');
var injectIntl = require('react-intl').injectIntl;
var Input = require('../../../../Input');
var I18nMixin = require('../../../../../i18n/I18nMixin');

var EventDescription = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
        var description = this.props.data.statement.description;
        return {
            description: description ? description : '',
            descriptionValid: true
        };
    },
    componentDidMount: function () {
        if (this.state.description !== '') {
            this.props.enableNext();
        }
    },

    onChange: function (e) {
        var value = e.target.value;
        var statement = this.props.data.statement;
        statement.description = value;
        if (value !== '') {
            this.props.enableNext();
        } else {
            this.props.disableNext();
        }
        this.setState({description: value, descriptionValid: value !== ''});
    },


    render: function () {
        return (
            <div>
                <Input type='textarea' rows='8' label={this.i18n('eventtype.default.description') + '*'}
                       placeholder={this.i18n('eventtype.default.description-placeholder')}
                       bsStyle={this.state.descriptionValid ? null : 'error'}
                       title={this.state.descriptionValid ? this.i18n('eventtype.default.description-placeholder') : this.i18n('eventtype.default.description-missing')}
                       value={this.state.description} onChange={this.onChange}/>
            </div>
        );
    }
});

module.exports = injectIntl(EventDescription);
