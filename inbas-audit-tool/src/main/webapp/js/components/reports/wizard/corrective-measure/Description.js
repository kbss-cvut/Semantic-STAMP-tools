/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Input = require('react-bootstrap').Input;

var Description = React.createClass({
    getInitialState: function() {
        return {
            description: ''
        };
    },
    onChange: function(e) {
        var value = e.target.value;
        this.props.data.description = value;
        this.setState({description: value});
    },
    render: function () {
        var title = (<h3>Corrective Measure</h3>);
        return (
            <Panel header={title} className="wizard-step-content">
                <Input type="textarea" rows="8" label="Description" placeholder="Corrective measure description"
                       value={this.state.description} onChange={this.onChange}/>
            </Panel>
        );
    }
});

module.exports = Description;
