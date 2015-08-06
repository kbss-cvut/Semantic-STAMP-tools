/**
 * Created by ledvima1 on 22.6.15.
 */

'use strict';

var React = require('react');

var Input = require('../../Input');

var FlightInfo = React.createClass({
    render: function () {
        return (
            <div className='float-container'>
                <div className='report-detail-float'>
                    <Input type='text' label='Last Departure Point' name='lastDeparturePoint'
                           onChange={this.props.onChange}
                           value={this.props.lastDeparturePoint}/>
                </div>
                <div className='report-detail-float-right'>
                    <Input type='text' label='Planned Destination' name='plannedDestination'
                           onChange={this.props.onChange}
                           value={this.props.plannedDestination}/>
                </div>
            </div>
        );
    }
});

module.exports = FlightInfo;
