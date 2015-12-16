/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var injectIntl = require('../../../../../utils/injectIntl');

var ConflictingAircraftStep = require('./ConflictingAircraftStep');
var Constants = require('../../../../../constants/Constants');
var Input = require('../../../../Input');
var I18nMixin = require('../../../../../i18n/I18nMixin');

var WasConflictingAircraft = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
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
        var title = (<h3>{this.i18n('eventtype.incursion.wasconflicting.panel-title')}</h3>);
        return (
            <Panel title={title}>
                <h5>{this.i18n('eventtype.incursion.wasconflicting.text')}</h5>

                <div>
                    <Input type='radio' label={this.i18n('yes')} value='yes'
                           checked={this.state.hasConflictingAircraft} onChange={this.onChange}/>
                    <Input type='radio' label={this.i18n('no')} value='no'
                           checked={!this.state.hasConflictingAircraft}
                           onChange={this.onChange}/>
                </div>
            </Panel>
        );
    }
});

module.exports = injectIntl(WasConflictingAircraft);
