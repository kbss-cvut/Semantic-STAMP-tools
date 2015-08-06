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
        var title = (<h3>Conflicting Aircraft</h3>);
        var statement = this.state.statement;
        var phaseOptions = [
            {value: 'takeoff', label: 'Take-off', title: 'Plane taking off'},
            {value: 'approach', label: 'Approach', title: 'Plane approaching the runway'},
            {value: 'landing', label: 'Landing', title: 'Plane landing'}
        ];
        return (
            <Panel header={title}>
                <AircraftRegistration registration={statement.conflictingAircraft.registration}
                                      stateOfRegistry={statement.conflictingAircraft.stateOfRegistry}
                                      onChange={this.onChange}/>
                <Panel header='Aviation Operation'>
                    <div className='report-detail'>
                        <Input type='text' label='Flight Number' name='flightNumber' onChange={this.onChange}
                               value={statement.conflictingAircraft.flightNumber}/>
                    </div>
                    <div style={{overflow: 'hidden'}}>
                        <div className='report-detail-float'>
                            <Select label='Phase' name='flightPhase' value={statement.conflictingAircraft.flightPhase}
                                    onChange={this.onChange}
                                    title='Phase of flight when the event occurred' options={phaseOptions}/>
                        </div>
                        <div className='report-detail-float-right'>
                            <FlightOperationType operationType={statement.conflictingAircraft.operationType}
                                                 onChange={this.onChange}/>
                        </div>
                    </div>
                    <FlightInfo lastDeparturePoint={statement.conflictingAircraft.lastDeparturePoint}
                                plannedDestination={statement.conflictingAircraft.plannedDestination}
                                onChange={this.onChange}/>
                </Panel>
            </Panel>
        );
    }
});

module.exports = ConflictingAircraft;
