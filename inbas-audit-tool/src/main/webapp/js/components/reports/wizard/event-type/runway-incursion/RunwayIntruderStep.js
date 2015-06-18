/**
 * Created by kidney on 6/18/15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;

var RunwayIntruderStep = React.createClass({
    getInitialState: function () {
        var statement = this.props.data.statement;
        if (statement.intruder == null) {
            statement.intruder = {};
        }
        return {
            intruderType: null,
            statement: statement
        };
    },
    render: function () {
        var pane = this.renderPane();
        var title = (<h3>Runway Intruding Object</h3>);
        return (
            <Panel header={title}>
                <div>
                    <Input type='radio' label='Aircraft' value='aircraft'
                           checked={this.state.intruderType === 'aircraft'} onChange={this.onIntruderTypeSelect}/>
                    <Input type='radio' label='Vehicle' value='vehicle' checked={this.state.intruderType === 'vehicle'}
                           onChange={this.onIntruderTypeSelect}/>
                    <Input type='radio' label='Person' value='person' checked={this.state.intruderType === 'person'}
                           onChange={this.onIntruderTypeSelect}/>
                </div>
                {pane}
            </Panel>
        );
    },
    renderPane: function () {
        switch (this.state.intruderType) {
            case 'aircraft':
                return this.renderAircraftPane();
            default:
                return null;
        }
    },
    renderAircraftPane: function() {
        var statement = this.state.statement;
        return (
            <div>
            <div className='form-group'>
                <Input type='text' label='Aircraft Registration' name='aircraftRegistration' value={statement.intruder.aicraftRegistration} onChange={this.onChange}/>
                <Input type='text' label='State of Registration' name='registrationState' value={statement.intruder.registrationState} onChange={this.onChange}/>
                </div>
                <Panel header='Aircraft Event'>
                    <Input type='text' label='Call Sign' name='callSign' value={statement.intruder.callSign} onChange={this.onChange}/>
                    <Input type='text' label='Operator' name='operator' value={statement.intruder.operator} onChange={this.onChange}/>
                    <div>
                        <Input type='radio' label='Flight' value='flight' checked={this.state.aircraftEventType === 'flight'} onChange={this.onAircraftEventTypeSelect}/>
                        <Input type='radio' label='Non Flight' value='nonflight' checked={this.state.aircraftEventType === 'nonflight'} onChange={this.onAircraftEventTypeSelect}/>
                        </div>
                    </Panel>

            </div>
        )
    },
    onChange: function(e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        // TODO The assignment throws errors because the value is often null (the attribute hasn't been defined yet on intruder)
        this.setState(assign(this.state.statement.intruder[attributeName], value));
    },
    onAircraftEventTypeSelect: function(e) {
        this.setState(assign(this.state, {aircraftEventType: e.target.value}));
    },
    onIntruderTypeSelect: function (e) {
        this.setState(assign(this.state, {intruderType: e.target.value}));
    }
});

module.exports = RunwayIntruderStep;
