/**
 * @jsx
 */

'use strict';

var React = require('react');

var Input = require('../../Input');

var FlightInfo = React.createClass({
    render: function () {
        return (
            <div className='row'>
                <div className='col-xs-6'>
                    <Input type='text' label='Last Departure Point' name='lastDeparturePoint'
                           onChange={this.props.onChange}
                           value={this.props.lastDeparturePoint}/>
                </div>
                <div className='col-xs-6'>
                    <Input type='text' label='Planned Destination' name='plannedDestination'
                           onChange={this.props.onChange}
                           value={this.props.plannedDestination}/>
                </div>
            </div>
        );
    }
});

module.exports = FlightInfo;
