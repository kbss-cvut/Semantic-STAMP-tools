/**
 * Created by ledvima1 on 25.6.15.
 */

'use strict';

var React = require('react');
var Input = require('react-bootstrap').Input;

var Select = React.createClass({
    render: function () {
        var options = this.generateOptions();
        return (
            <Input type='select' name={this.props.name} title={this.props.title} label={this.props.label}
                   defaultValue='' value={this.props.value} onChange={this.props.onChange}>
                <option key='opt_default' disabled style={{display: 'none'}}> -- Select --</option>
                {options}
            </Input>
        );
    },

    generateOptions: function () {
        var options = [];
        var len = this.props.options.length;
        for (var i = 0; i < len; i++) {
            var option = this.props.options[i];
            options.push(<option key={'opt_' + option.value} value={option.value}
                                 title={option.title}>{option.label}</option>);
        }
        return options;
    }
});

module.exports = Select;
