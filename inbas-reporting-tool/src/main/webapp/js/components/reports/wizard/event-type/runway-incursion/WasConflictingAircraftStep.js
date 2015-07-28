/**
 * @author ledvima1
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Input = require('react-bootstrap').Input;

var ConflictingAircraftStep = require('./ConflictingAircraftStep');
var Constants = require('../../../../../constants/Constants');

var WasConflictingAircraft = React.createClass({
    getInitialState: function() {
        return {
            hasConflictingAircraft: this.props.data.statement.conflictingAircraft != null
        }
    },
    onChange: function (e) {
        if (e.target.value === 'yes') {
            this.props.data.statement.conflictingAircraft = {};
            this.setState({hasConflictingAircraft: true});
            this.props.insertStepAfterCurrent({
                name: 'Conflicting Aircraft',
                component: ConflictingAircraftStep,
                id: Constants.CONFLICTING_AIRCRAFT_STEP_ID
            });
            this.props.next();
        } else {
            if (this.props.data.statement.conflictingAircraft) {
                delete this.props.data.statement.conflictingAircraft;
            }
            this.props.removeStep(Constants.CONFLICTING_AIRCRAFT_STEP_ID);
            this.setState({hasConflictingAircraft: false});
        }

    },

    render: function () {
        var title = (<h3>Was Conflicting Aircraft</h3>);
        return (
            <Panel title={title}>
                <h5>Was there a conflicting aircraft?</h5>

                <div>
                    <Input type='radio' label='Yes' value='yes'
                           checked={this.state.hasConflictingAircraft} onChange={this.onChange}/>
                    <Input type='radio' label='No' value='no' checked={!this.state.hasConflictingAircraft}
                           onChange={this.onChange}/>
                </div>
            </Panel>
        );
    }
});

module.exports = WasConflictingAircraft;
