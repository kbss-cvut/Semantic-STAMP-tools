/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var Input = require('../../../Input');

var Description = React.createClass({
    getInitialState: function () {
        var description = this.props.data.statement.description;
        return {
            description: description ? description : ''
        };
    },
    onChange: function (e) {
        var value = e.target.value,
            statement = this.props.data.statement;
        statement.description = value;
        this.setState({description: value});
    },
    render: function () {
        return (
            <div>
                <Input type='textarea' rows='8' label='Description' placeholder='Corrective measure description'
                       value={this.state.description} onChange={this.onChange}/>
            </div>
        );
    }
});

module.exports = Description;
