/**
 * Created by ledvima1 on 30.6.15.
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Input = require('react-bootstrap').Input;

var EventDescription = React.createClass({
    getInitialState: function () {
        return {
            description: '',
            descriptionValid: true
        };
    },

    onChange: function (e) {
        var value = e.target.value;
        var statement = this.props.data.statement;
        statement.description = value;
        if (value !== '') {
            this.props.enableNext();
        } else {
            this.props.disableNext();
        }
        this.setState({description: value, descriptionValid: value !== ''});
    },


    render: function () {
        var title = (<h3>Event Description</h3>);
        return (
            <Panel header={title} className="wizard-step-content">
                <Input type="textarea" rows="8" label="Description*" placeholder="Event description"
                       bsStyle={this.state.descriptionValid ? null : 'error'}
                       title={this.state.descriptionValid ? 'Event description' : 'Description is required'}
                       value={this.state.description} onChange={this.onChange}/>
            </Panel>
        );
    }
});

module.exports = EventDescription;
