/**
 * Created by ledvima1 on 22.6.15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Panel = require('react-bootstrap').Panel;

var AircraftRegistration = require('../../AircraftRegistration');
var FlightInfo = require('../../FlightInfo');
var FlightOperationType = require('../../FlightOperationType');
var Select = require('../../../../Select');
var Input = require('../../../../Input');
var OperatorTypeahead = require('../../../../typeahead/OperatorTypeahead');

var AircraftIntruder = React.createClass({
    getInitialState: function () {
        var statement = this.props.statement;
        if (!statement.intruder.aircraftEventType) {
            statement.intruder.aircraftEventType = statement.intruder.flightPhase === 'taxiing' ? 'nonflight' : 'flight';
        }
        return {
            statement: statement
        };
    },

    onAircraftEventTypeSelect: function (e) {
        var change = {
            aircraftEventType: e.target.value
        };
        if (change.aircraftEventType === 'nonflight') {
            change.flightPhase = 'taxiing';
            this.eraseFlightAttributes();
        }
        assign(this.state.statement.intruder, change);
        this.setState({statement: this.state.statement});
    },

    eraseFlightAttributes: function () {
        var intruder = this.state.statement.intruder;
        delete intruder.flightNumber;
        delete intruder.operationType;
        delete intruder.lastDeparturePoint;
        delete intruder.plannedDestination;
    },

    onPhaseChange: function (e) {
        this.setState(assign(this.state.statement.intruder, {flightPhase: e.target.value}));
    },

    onOperatorChange: function (option) {
        this.setState(assign(this.state.statement.intruder, {
            operator: {
                code: option.code,
                name: option.name,
                uri: option.uri
            }
        }));
    },


    render: function () {
        var intruder = this.state.statement.intruder,
            operator = intruder.operator ? intruder.operator.name + ' (' + intruder.operator.code + ')' : null;
        return (
            <div>
                <div className='form-group'>
                    <AircraftRegistration registration={intruder.registration}
                                          stateOfRegistry={intruder.stateOfRegistry}
                                          onChange={this.props.onChange}/>
                </div>
                <Panel header='Aircraft Event'>
                    <div className='row'>
                        <div className='col-xs-6'>
                            <Input type='text' label='Call Sign' name='callSign' value={intruder.callSign}
                                   onChange={this.props.onChange}/>
                        </div>
                        <div className='col-xs-6'>
                            <OperatorTypeahead name='operator' value={operator}
                                               onChange={this.onOperatorChange}/>
                        </div>
                    </div>
                    <div>
                        <Input type='radio' label='Flight' value='flight'
                               checked={intruder.aircraftEventType === 'flight'}
                               onChange={this.onAircraftEventTypeSelect} wrapperClassName='col-xs-2'/>
                        <Input type='radio' label='Non Flight' value='nonflight'
                               checked={intruder.aircraftEventType === 'nonflight'}
                               onChange={this.onAircraftEventTypeSelect} wrapperClassName='col-xs-2'/>
                    </div>
                    <div style={{clear: 'both'}}/>
                    {this.renderAircraftEventPane(intruder.aircraftEventType)}
                </Panel>
            </div>
        );
    },

    renderAircraftEventPane: function (aircraftEventType) {
        switch (aircraftEventType) {
            case 'flight':
                return this.renderFlightEventPane();
            case 'nonflight':
                return this.renderNonFlightEventPane();
            default:
                return null;
        }
    },

    renderFlightEventPane: function () {
        var intruder = this.state.statement.intruder;
        var phaseOptions = [
            {
                value: 'taxi_to_runway',
                label: 'Taxi to Runway',
                title: 'Commences when the aircraft begins to move under its own power leaving the gate, ramp and ' +
                'terminates upon reaching the runway'
            },
            {
                value: 'taxi_takeoff',
                label: 'Taxi to Take-off Position',
                title: 'From entering the runway until reaching the take-off position'
            },
            {
                value: 'taxi_from_runway',
                label: 'Taxi from Runway',
                title: 'Begins upon exiting the landing runway and terminates upon arrival at the gate, ramp, apron, ' +
                'or parking area, when the aircraft ceases to move under its own power'
            },
            {value: 'maintain_position', label: 'Maintaining Position', title: 'Maintaining position at holding point'}
        ];
        return (
            <Panel header='Aviation Operation'>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Input type='text' label='Flight Number' name='flightNumber'
                               value={intruder.flightNumber} onChange={this.props.onChange}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Select label='Phase' value={intruder.flightPhase} onChange={this.onPhaseChange}
                                title='What was the aircraft doing?' options={phaseOptions}/>
                    </div>
                    <div className='col-xs-6'>
                        <FlightOperationType operationType={intruder.operationType}
                                             onChange={this.props.onChange}/>
                    </div>
                </div>
                <FlightInfo lastDeparturePoint={intruder.lastDeparturePoint}
                            plannedDestination={intruder.plannedDestination} onChange={this.props.onChange}/>
            </Panel>
        );
    },
    renderNonFlightEventPane: function () {
        return (
            <Panel header='Other Aircraft Event'>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Input type='select' label='Phase' value={this.state.statement.intruder.flightPhase} disabled>
                            <option value='taxiing'>Taxiing: other</option>
                        </Input>
                    </div>
                </div>
            </Panel>
        );
    }
});

module.exports = AircraftIntruder;
