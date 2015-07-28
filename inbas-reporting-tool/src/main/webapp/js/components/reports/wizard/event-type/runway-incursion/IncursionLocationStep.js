/**
 * @author ledvima1
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Input = require('react-bootstrap').Input;

var IncursionLocation = React.createClass({
    getInitialState: function () {
        var incursionLocation = this.props.data.statement.location;
        return {
            location: incursionLocation ? incursionLocation.location : ''
        };
    },

    onChange: function (e) {
        var value = e.target.value;
        var statement = this.props.data.statement;
        if (value === '' && statement.location) {
            delete statement.location;
        } else {
            statement.location = {
                location: value
            }
        }
        this.setState({location: value});
    },

    // Rendering

    render: function () {
        var title = (<h3>Incursion location</h3>);
        return (
            <Panel header={title}>
                <div>
                    <Input type='text' label='Incursion location' value={this.state.location} onChange={this.onChange}
                           title='Where the incursion occurred'/>
                </div>
            </Panel>
        );
    }
});

module.exports = IncursionLocation;
