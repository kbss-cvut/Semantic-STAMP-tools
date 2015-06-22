/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;

var AircraftRegistration = require('../../AircraftRegistration');
var FlightInfo = require('../../FlightInfo');

var AircraftClearedStep = React.createClass({
    getInitialState: function () {
        return {
            statement: this.props.data.statement
        };
    },
    onChange: function (e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        this.setState(assign(this.state.statement[attributeName], value));
    },
    onRegistrationChange: function (data) {
        this.setState(assign(this.state.statement, {
            aircraftRegistration: data.registration, aircraftRegistryState: data.registryState
        }));
    },
    render: function () {
        var title = (<h3>Aircraft Cleared to Use Runway</h3>);
        var statement = this.state.statement;
        var registrationData = {
            registration: statement.aircraftRegistration,
            registryState: statement.aircraftRegistryState
        };
        return (
            <Panel header={title}>
                <AircraftRegistration data={registrationData} onChange={this.onRegistrationChange}/>
                <Panel header="Aviation Operation">
                    <div className='report-detail'>
                        <Input type="text" label="Flight Number" name="flightNumber" onChange={this.onChange}
                               value={statement.flightNumber}/>
                    </div>
                    <div style={{overflow: 'hidden'}}>
                        <div className='report-detail-float'>
                            <Input type="select" label="Phase" title="What is the aircraft doing?">
                                <option value="takeoff">Take-off</option>
                                <option value="approach">Approach</option>
                                <option value="landing">Landing</option>
                            </Input>
                        </div>
                        <div className='report-detail-float-right'>
                            <Input type="select" label="Operation Type">
                                <option value="passenger">Passenger Flight</option>
                                <option value="cargo">Cargo flight</option>
                            </Input>
                        </div>
                    </div>
                    <FlightInfo lastDeparturePoint={statement.lastDeparturePoint}
                                plannedDestination={statement.plannedDestination} onChange={this.onChange}/>
                </Panel>
            </Panel>
        );
    }
});

module.exports = AircraftClearedStep;
