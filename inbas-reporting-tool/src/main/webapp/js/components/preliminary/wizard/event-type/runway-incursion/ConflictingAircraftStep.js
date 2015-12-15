/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var injectIntl = require('react-intl').injectIntl;

var AircraftRegistration = require('../../AircraftRegistration');
var FlightInfo = require('../../FlightInfo');
var FlightOperationType = require('../../FlightOperationType');
var Select = require('../../../../Select');
var Input = require('../../../../Input');
var I18nMixin = require('../../../../../i18n/I18nMixin');


var ConflictingAircraft = React.createClass({
    mixins: [I18nMixin],

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
        return (
            <div>
                <AircraftRegistration registration={aircraft.registration}
                                      stateOfRegistry={aircraft.stateOfRegistry}
                                      onChange={this.onChange}/>
                <Panel header={this.i18n('eventtype.incursion.intruder.aircraft.operation')}>
                    <div className='row'>
                        <div className='col-xs-6'>
                            <Input type='text' label={this.i18n('eventtype.incursion.intruder.aircraft.callsign')}
                                   name='callSign' value={aircraft.callSign}
                                   onChange={this.onChange}/>
                        </div>
                        <div className='col-xs-6'>
                            <Input type='text' label={this.i18n('eventtype.incursion.intruder.aircraft.flightno')}
                                   name='flightNumber' onChange={this.onChange}
                                   value={aircraft.flightNumber}/>
                        </div>
                    </div>
                    <div className='row'>
                        <div className='col-xs-6'>
                            <Select label={this.i18n('eventtype.incursion.intruder.aircraft.phase')} name='flightPhase'
                                    value={aircraft.flightPhase} onChange={this.onChange}
                                    title='Phase of flight when the event occurred' options={this.getPhaseOptions()}/>
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
    },

    getPhaseOptions: function () {
        return [
            {
                value: 'takeoff',
                label: this.i18n('eventtype.incursion.conflicting.phase.takeoff'),
                title: this.i18n('eventtype.incursion.conflicting.phase.takeoff-tooltip')
            },
            {
                value: 'approach',
                label: this.i18n('eventtype.incursion.conflicting.phase.approach'),
                title: this.i18n('eventtype.incursion.conflicting.phase.approach-tooltip')
            },
            {
                value: 'landing',
                label: this.i18n('eventtype.incursion.conflicting.phase.landing'),
                title: this.i18n('eventtype.incursion.conflicting.phase.landing-tooltip')
            }
        ];
    }
});

module.exports = injectIntl(ConflictingAircraft);
