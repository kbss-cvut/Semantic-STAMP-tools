/**
 * @jsx
 */

'use strict';

var React = require('react');

var Select = require('../../Select');

var FlightOperationType = React.createClass({
    render: function () {
        var options = [
            {
                value: 'passenger',
                label: 'Passenger Flight',
                title: 'A flight carrying one or more revenue passengers. This includes flights which carry, in addition' +
                ' to passengers, mail or cargo.'
            },
            {
                value: 'cargo',
                label: 'Cargo Flight',
                title: 'This is to be used for all-freight services only. Cargo includes freight, unaccompanied baggage ' +
                'and mail.'
            }
        ];
        return (
            <Select label='Operation Type' name='operationType' value={this.props.operationType}
                    onChange={this.props.onChange} options={options}/>
        );
    }
});

module.exports = FlightOperationType;
