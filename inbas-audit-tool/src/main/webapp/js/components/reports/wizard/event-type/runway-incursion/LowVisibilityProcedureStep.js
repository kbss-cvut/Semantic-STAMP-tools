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
        var statement = this.props.data.statement;
        if (statement.lvp == null) {
            statement.lvp = 'none';
        }
        return {
            statement: statement
        };
    },
    onLvpChange: function (e) {
        this.setState(assign(this.state.statement, {lvp: e.target.value}));
    },


    render: function () {
        var title = (<h3>Low Visibility Procedure</h3>);
        // TODO Perhaps it would be better to load the visibility procedure options from the server, as part of the
        // taxonomy
        return (
            <Panel header={title}>
                <div className='form-group'>
                    <Input type='select' label='Low Visibility Procedure' onChange={this.onLvpChange}
                           value={this.state.statement.lvp}>
                        <option value='none'>None</option>
                        <option value='cati'>CAT I</option>
                        <option value='catii'>CAT II</option>
                        <option value='catiii'>CAT III</option>
                    </Input>
                </div>
            </Panel>
        );
    }
});

module.exports = LowVisibilityProcedureStep;
