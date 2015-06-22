/**
 * Created by ledvima1 on 22.6.15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;

var AircraftRegistration = require('../../AircraftRegistration');
var FlightInfo = require('../../FlightInfo');

var AircraftIntruder = React.createClass({
    getInitialState: function () {
        return {
            statement: this.props.statement,
            aircraftEventType: null
        };
    },

    render: function () {
        var statement = this.state.statement;
        var registrationData = {
            registration: statement.intruder.aircraftRegistration,
            registryState: statement.intruder.aircraftRegistryState
        };
        return (
            <div>
                <div className='form-group'>
                    <AircraftRegistration data={registrationData} onChange={this.onRegistrationChange}/>
                </div>
                <Panel header='Aircraft Event'>
                    <div className='float-container'>
                        <div className='report-detail-float'>
                            <Input type='text' label='Call Sign' name='callSign' value={statement.intruder.callSign}
                                   onChange={this.props.onChange}/>
                        </div>
                        <div className='report-detail-float-right'>
                            <Input type='text' label='Operator' name='operator' value={statement.intruder.operator}
                                   onChange={this.props.onChange}/>
                        </div>
                    </div>
                    <div>
                        <Input type='radio' label='Flight' value='flight'
                               checked={this.state.aircraftEventType === 'flight'}
                               onChange={this.onAircraftEventTypeSelect} wrapperClassName='col-xs-2'/>
                        <Input type='radio' label='Non Flight' value='nonflight'
                               checked={this.state.aircraftEventType === 'nonflight'}
                               onChange={this.onAircraftEventTypeSelect} wrapperClassName='col-xs-2'/>

                        <div style={{clear: 'both'}}/>
                    </div>
                    {this.renderAircraftEventPane()}
                </Panel>

            </div>
        );
    },

    onRegistrationChange: function (data) {
        this.setState(assign(this.state.statement.intruder, {
            aircraftRegistration: data.registration, aircraftRegistryState: data.registryState
        }));
    },

    onAircraftEventTypeSelect: function (e) {
        this.setState(assign(this.state, {aircraftEventType: e.target.value}));
    },

    renderAircraftEventPane: function () {
        switch (this.state.aircraftEventType) {
            case 'flight':
                return this.renderFlightEventPane();
            case 'nonflight':
                return this.renderNonFlightEventPane();
            default:
                return null;
        }
    },

    renderFlightEventPane: function () {
        var statement = this.state.statement;
        return (
            <Panel header='Aviation Operation'>
                <div className='report-detail'>
                    <Input type='text' label='Flight Number' name='flightNumber'
                           value={statement.intruder.flightNumber} onChange={this.props.onChange}/>
                </div>
                <div className='float-container'>
                    <div className='report-detail-float'>
                        <Input type='select' label='Phase' value={statement.intruder.phase}
                               title='What was the aircraft doing?'>
                            <option value='taxi_to_runway'
                                    title='Commences when the aircraft begins to move under its own
                                power leaving the gate, ramp and terminates upon reaching the runway'>
                                Taxi to runway
                            </option>
                            <option value='taxi_takeoff'
                                    title='From entering the runway until reaching the take-off position.'>Taxi to
                                take-off
                                position
                            </option>
                            <option value='taxi_from_runway'
                                    title='Begins upon exiting the landing runway and terminates upon arrival at the gate,
                                ramp, apron, or parking area, when the aircraft ceases to move under its own power.'>
                                Taxi from runway
                            </option>
                            <option value='maintain_position' title='Maintaining position at holding point.'>Maintaining
                                position
                            </option>
                        </Input>
                    </div>
                    <div className='report-detail-float-right'>
                        <Input type='select' label='Operation type'
                               value={statement.intruder.operationType}>
                            <option value='passenger'
                                    title='A flight carrying one or more revenue passengers. This includes flights which
                                     carry, in addition to passengers, mail or cargo.'>
                                Passenger flight
                            </option>
                            <option value='cargo'
                                    title='This is to be used for all-freight services only. Cargo includes freight,
                                    unaccompanied baggage and mail.'>
                                Cargo flight
                            </option>
                        </Input>
                    </div>
                </div>
                <FlightInfo lastDeparturePoint={statement.intruder.lastDeparturePoint}
                            plannedDestination={statement.intruder.plannedDestination} onChange={this.onChange}/>
            </Panel>
        );
    },
    renderNonFlightEventPane: function () {
        return (
            <Panel header='Other Aircraft Event'>
                <div className='report-detail'>
                    <Input type='select' value={this.state.statement.intruder.phase} disabled>
                        <option value='taxiing'>Taxiing: other</option>
                    </Input>
                </div>
            </Panel>
        );
    }
});

module.exports = AircraftIntruder;
