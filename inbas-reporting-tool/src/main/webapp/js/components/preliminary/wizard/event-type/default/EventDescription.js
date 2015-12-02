/**
 * @jsx
 */

'use strict';

var React = require('react');
var Input = require('../../../../Input');

var EventDescription = React.createClass({
    getInitialState: function () {
        var description = this.props.data.statement.description;
        return {
            description: description ? description : '',
            descriptionValid: true
        };
    },
    componentDidMount: function () {
        if (this.state.description !== '') {
            this.props.enableNext();
        }
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
        return (
            <div>
                <Input type="textarea" rows="8" label="Description*" placeholder="Event description"
                       bsStyle={this.state.descriptionValid ? null : 'error'}
                       title={this.state.descriptionValid ? 'Event description' : 'Description is required'}
                       value={this.state.description} onChange={this.onChange}/>
            </div>
        );
    }
});

module.exports = EventDescription;
