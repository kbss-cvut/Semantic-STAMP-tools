/**
 * @jsx
 */

'use strict';

var React = require('react');
var injectIntl = require('../../../utils/injectIntl');

var Select = require('../../Select');
var I18nMixin = require('../../../i18n/I18nMixin');

var FlightOperationType = React.createClass({
    mixins: [I18nMixin],

    render: function () {
        var options = [
            {
                value: 'passenger',
                label: this.i18n('flight.operation-type.passenger'),
                title: this.i18n('flight.operation-type.passenger.tooltip')
            },
            {
                value: 'cargo',
                label: this.i18n('flight.operation-type.cargo'),
                title: this.i18n('flight.operation-type.cargo.tooltip')
            }
        ];
        return (
            <Select label={this.i18n('flight.operation-type')} name='operationType' value={this.props.operationType}
                    onChange={this.props.onChange} options={options}/>
        );
    }
});

module.exports = injectIntl(FlightOperationType);
