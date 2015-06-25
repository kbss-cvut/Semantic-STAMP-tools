/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;

var AircraftRegistration = require('../../AircraftRegistration');
var FlightInfo = require('../../FlightInfo');
var FlightOperationType = require('../../FlightOperationType');
var Select = require('../../../../Select');

var AircraftClearedStep = React.createClass({
    getInitialState: function () {
        var statement = this.props.data.statement;
        if (!statement.clearedAircraft) {
            statement.clearedAircraft = {};
        }
        return {
            statement: statement
        };
    },
    onChange: function (e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        this.state.statement.clearedAircraft[attributeName] = value;
        this.setState({statement: this.state.statement});
    },


    render: function () {
        var title = (<h3>Aircraft Cleared to Use Runway</h3>);
        var statement = this.state.statement;
        var phaseOptions = [
            {value: 'takeoff', label: 'Take-off', title: 'Plane taking off'},
            {value: 'approach', label: 'Approach', title: 'Plane approaching the runway'},
            {value: 'landing', label: 'Landing', title: 'Plane landing'}
        ];
        return (
            <Panel header={title}>
                <AircraftRegistration registration={statement.clearedAircraft.registration}
                                      stateOfRegistry={statement.clearedAircraft.stateOfRegistry}
                                      onChange={this.onChange}/>
                <Panel header='Aviation Operation'>
                    <div className='report-detail'>
                        <Input type='text' label='Flight Number' name='flightNumber' onChange={this.onChange}
                               value={statement.clearedAircraft.flightNumber}/>
                    </div>
                    <div style={{overflow: 'hidden'}}>
                        <div className='report-detail-float'>
                            <Select label='Phase' name='flightPhase' value={statement.clearedAircraft.flightPhase} onChange={this.onChange}
                                    title='Phase of flight when the event occurred' options={phaseOptions}/>
                        </div>
                        <div className='report-detail-float-right'>
                            <FlightOperationType operationType={statement.clearedAircraft.operationType}
                                                 onChange={this.onChange}/>
                        </div>
                    </div>
                    <FlightInfo lastDeparturePoint={statement.clearedAircraft.lastDeparturePoint}
                                plannedDestination={statement.clearedAircraft.plannedDestination} onChange={this.onChange}/>
                </Panel>
            </Panel>
        );
    }
});

module.exports = AircraftClearedStep;
