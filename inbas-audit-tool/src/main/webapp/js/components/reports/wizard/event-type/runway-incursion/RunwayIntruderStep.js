/**
 * Created by kidney on 6/18/15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;

var AircraftRegistration = require('../../AircraftRegistration');
var AircraftIntruder = require('./AircraftIntruder');

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
                           checked={this.state.intruderType === 'aircraft'} onChange={this.onIntruderTypeSelect}
                           wrapperClassName='col-xs-2'/>
                    <Input type='radio' label='Vehicle' value='vehicle' checked={this.state.intruderType === 'vehicle'}
                           onChange={this.onIntruderTypeSelect} wrapperClassName='col-xs-2'/>
                    <Input type='radio' label='Person' value='person' checked={this.state.intruderType === 'person'}
                           onChange={this.onIntruderTypeSelect} wrapperClassName='col-xs-2'/>
                </div>
                <div style={{clear: 'both'}}/>
                {pane}
            </Panel>
        );
    },
    renderPane: function () {
        switch (this.state.intruderType) {
            case 'aircraft':
                return (<AircraftIntruder statement={this.state.statement} onChange={this.onChange}/>);
            default:
                return null;
        }
    },
    onChange: function (e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        // TODO The assignment throws errors because the value is often null (the attribute hasn't been defined yet on
        // intruder)
        this.setState(assign(this.state.statement.intruder[attributeName], value));
    },
    onIntruderTypeSelect: function (e) {
        this.setState(assign(this.state, {intruderType: e.target.value}));
    }
});

module.exports = RunwayIntruderStep;
