/**
 * @jsx
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Panel = require('react-bootstrap').Panel;
var injectIntl = require('../../../../../utils/injectIntl');

var AircraftRegistration = require('../../AircraftRegistration');
var FlightInfo = require('../../FlightInfo');
var FlightOperationType = require('../../FlightOperationType');
var Select = require('../../../../Select');
var Input = require('../../../../Input');
var OperatorTypeahead = require('../../../../typeahead/OperatorTypeahead');
var I18nMixin = require('../../../../../i18n/I18nMixin');

var AircraftIntruder = React.createClass({
    mixins: [I18nMixin],

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
                <Panel header={this.i18n('eventtype.incursion.intruder.aircraft.panel-title')}>
                    <div className='row'>
                        <div className='col-xs-6'>
                            <Input type='text' label={this.i18n('eventtype.incursion.intruder.aircraft.callsign')}
                                   name='callSign' tabIndex='3' value={intruder.callSign}
                                   onChange={this.props.onChange}/>
                        </div>
                        <div className='col-xs-6'>
                            <OperatorTypeahead name='operator' value={operator} tabIndex='4'
                                               onChange={this.onOperatorChange}/>
                        </div>
                    </div>
                    <div>
                        <Input type='radio' label={this.i18n('eventtype.incursion.intruder.aircraft.flight')}
                               value='flight' tabIndex='5'
                               checked={intruder.aircraftEventType === 'flight'}
                               onChange={this.onAircraftEventTypeSelect} wrapperClassName='col-xs-2'/>
                        <Input type='radio' label={this.i18n('eventtype.incursion.intruder.aircraft.nonflight')}
                               value='nonflight' tabIndex='6'
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
        return (
            <Panel header={this.i18n('eventtype.incursion.intruder.aircraft.operation')}>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Input type='text' label={this.i18n('eventtype.incursion.intruder.aircraft.flightno')}
                               name='flightNumber' tabIndex='7'
                               value={intruder.flightNumber} onChange={this.props.onChange}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Select label={this.i18n('eventtype.incursion.intruder.aircraft.phase')}
                                value={intruder.flightPhase} onChange={this.onPhaseChange} tabIndex='8'
                                title={this.i18n('eventtype.incursion.intruder.aircraft.phase-tooltip')}
                                options={this.getFlightPhaseOptions()}/>
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

    getFlightPhaseOptions: function () {
        return [
            {
                value: 'taxi_to_runway',
                label: this.i18n('eventtype.incursion.intruder.aircraft.phase.taxi-to-rwy'),
                title: this.i18n('eventtype.incursion.intruder.aircraft.phase.taxi-to-rwy-tooltip')
            },
            {
                value: 'taxi_takeoff',
                label: this.i18n('eventtype.incursion.intruder.aircraft.phase.taxi-to-takeoff'),
                title: this.i18n('eventtype.incursion.intruder.aircraft.phase.taxi-to-takeoff-tooltip')
            },
            {
                value: 'taxi_from_runway',
                label: this.i18n('eventtype.incursion.intruder.aircraft.phase.taxi-from-rwy'),
                title: this.i18n('eventtype.incursion.intruder.aircraft.phase.taxi-from-rwy-tooltip')
            },
            {
                value: 'maintain_position',
                label: this.i18n('eventtype.incursion.intruder.aircraft.phase.maintain-position'),
                title: this.i18n('eventtype.incursion.intruder.aircraft.phase.maintain-position-tooltip')
            }
        ];
    },

    renderNonFlightEventPane: function () {
        return (
            <Panel header={this.i18n('eventtype.incursion.intruder.aircraft.nonflight.operation')}>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Input type='select' label='Phase' value={this.state.statement.intruder.flightPhase} disabled>
                            <option
                                value='taxiing'>{this.i18n('eventtype.incursion.intruder.aircraft.phase.nonflight')}</option>
                        </Input>
                    </div>
                </div>
            </Panel>
        );
    }
});

module.exports = injectIntl(AircraftIntruder);
