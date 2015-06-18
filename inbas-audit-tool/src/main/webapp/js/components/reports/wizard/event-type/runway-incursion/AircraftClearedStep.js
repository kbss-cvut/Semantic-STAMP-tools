/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;

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
    render: function () {
        var title = (<h3>Aircraft Cleared to Use Runway</h3>);
        var statement = this.state.statement;
        return (
            <Panel header={title}>
                <div className="form-group">
                    <Input type="text" label="Aircraft Registration" name="aircraftRegistration"
                           onChange={this.onChange} value={statement.aircraftRegistration}/>
                    <Input type="text" label="State of Registration" name="registrationState" onChange={this.onChange}
                           value={statement.registrationState}/>
                </div>
                <Panel header="Aviation Operation">
                    <Input type="text" label="Flight Number" name="flightNumber" onChange={this.onChange}
                           value={statement.flightNumber}/>
                    <Input type="select" label="Phase" title="What is the aircraft doing?">
                        <option value="takeoff">Take-off</option>
                        <option value="approach">Approach</option>
                        <option value="landing">Landing</option>
                    </Input>
                    <Input type="select" label="Operation Type">
                        <option value="passenger">Passenger Flight</option>
                        <option value="cargo">Cargo flight</option>
                    </Input>
                    <Input type="text" label="Last Departure Point" name="lastDeparturePoint" onChange={this.onChange}
                           value={statement.lastDeparturePoint}/>
                    <Input type="text" label="Planned Destination" name="plannedDestination" onChange={this.onChange}
                           value={statement.plannedDestination}/>
                </Panel>
            </Panel>
        );
    }
});

module.exports = AircraftClearedStep;
