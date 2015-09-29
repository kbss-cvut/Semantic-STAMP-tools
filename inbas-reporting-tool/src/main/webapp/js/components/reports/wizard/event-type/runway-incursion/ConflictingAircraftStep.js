/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;

var AircraftRegistration = require('../../AircraftRegistration');
var FlightInfo = require('../../FlightInfo');
var FlightOperationType = require('../../FlightOperationType');
var Select = require('../../../../Select');
var Input = require('../../../../Input');


var ConflictingAircraft = React.createClass({
    getInitialState: function () {
        var statement = this.props.data.statement;
        if (!statement.conflictingAircraft) {
            statement.conflictingAircraft = {};
        }
        return {
            statement: statement
        };
    },

    onChange: function (e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        this.state.statement.conflictingAircraft[attributeName] = value;
        this.setState({statement: this.state.statement});
    },

    // Rendering

    render: function () {
        var aircraft = this.state.statement.conflictingAircraft;
        var phaseOptions = [
            {value: 'takeoff', label: 'Take-off', title: 'Plane taking off'},
            {value: 'approach', label: 'Approach', title: 'Plane approaching the runway'},
            {value: 'landing', label: 'Landing', title: 'Plane landing'}
        ];
        return (
            <div>
                <AircraftRegistration registration={aircraft.registration}
                                      stateOfRegistry={aircraft.stateOfRegistry}
                                      onChange={this.onChange}/>
                <Panel header='Aviation Operation'>
                    <div className='row'>
                        <div className='col-xs-6'>
                            <Input type='text' label='Call Sign' name='callSign' value={aircraft.callSign}
                                   onChange={this.onChange}/>
                        </div>
                        <div className='col-xs-6'>
                            <Input type='text' label='Flight Number' name='flightNumber' onChange={this.onChange}
                                   value={aircraft.flightNumber}/>
                        </div>
                    </div>
                    <div className='row'>
                        <div className='col-xs-6'>
                            <Select label='Phase' name='flightPhase' value={aircraft.flightPhase}
                                    onChange={this.onChange}
                                    title='Phase of flight when the event occurred' options={phaseOptions}/>
                        </div>
                        <div className='col-xs-6'>
                            <FlightOperationType operationType={aircraft.operationType} onChange={this.onChange}/>
                        </div>
                    </div>
                    <FlightInfo lastDeparturePoint={aircraft.lastDeparturePoint}
                                plannedDestination={aircraft.plannedDestination} onChange={this.onChange}/>
                </Panel>
            </div>
        );
    }
});

module.exports = ConflictingAircraft;
