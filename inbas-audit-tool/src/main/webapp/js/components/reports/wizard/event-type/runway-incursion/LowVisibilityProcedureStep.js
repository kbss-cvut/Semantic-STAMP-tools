/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;

var LowVisibilityProcedureStep = React.createClass({
    getInitialState: function () {
        return {
            statement: this.props.data.statement
        };
    },
    onLvpChange: function (e) {
        this.setState(assign(this.state.statement, {lvp: e.target.value}));
    },
    render: function () {
        var title = (<h3>Low Visibility Procedure</h3>);
        return (
            <Panel header={title}>
                <div className="form-group">
                    <Input type="text" label="Low Visibility Procedure" onChange={this.onLvpChange}
                           value={this.state.statement.lvp}/>
                </div>
            </Panel>
        )
    }
});

module.exports = LowVisibilityProcedureStep;
