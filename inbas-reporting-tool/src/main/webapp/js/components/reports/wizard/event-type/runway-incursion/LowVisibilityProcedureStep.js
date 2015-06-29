/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Panel = require('react-bootstrap').Panel;

var Select = require('../../../../Select');

var LowVisibilityProcedureStep = React.createClass({
    getInitialState: function () {
        var statement = this.props.data.statement;
        return {
            statement: statement
        };
    },
    onLvpChange: function (e) {
        this.setState(assign(this.state.statement, {lvp: e.target.value}));
    },


    render: function () {
        var title = (<h3>Low Visibility Procedure</h3>);
        // TODO It would be better to load the visibility procedure options from the server, as part of the taxonomy
        var options = [
            {value: 'none', label: 'None'},
            {value: 'cati', label: 'Cat I'},
            {value: 'catii', label: 'Cat II'},
            {value: 'catiii', label: 'Cat III'}
        ];
        return (
            <Panel header={title}>
                <div className='form-group'>
                    <Select label='Low Visibility Procedure' onChange={this.onLvpChange}
                            value={this.state.statement.lvp} options={options}/>
                </div>
            </Panel>
        );
    }
});

module.exports = LowVisibilityProcedureStep;
