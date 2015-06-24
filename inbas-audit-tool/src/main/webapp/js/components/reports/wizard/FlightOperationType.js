/**
 * Created by ledvima1 on 24.6.15.
 */

'use strict';

var React = require('react');
var Input = require('react-bootstrap').Input;

var FlightOperationType = React.createClass({
    render: function () {
        return (
            <Input type='select' label='Operation type' name='operationType' value={this.props.operationType}
                   onChange={this.props.onChange}>
                <option value='passenger' title='A flight carrying one or more revenue passengers. This includes flights which
                                     carry, in addition to passengers, mail or cargo.'>Passenger Flight
                </option>
                <option value='cargo' title='This is to be used for all-freight services only. Cargo includes freight,
                                    unaccompanied baggage and mail.'>Cargo flight
                </option>
            </Input>
        );
    }
});

module.exports = FlightOperationType;
