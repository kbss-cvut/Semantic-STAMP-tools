'use strict';

var React = require('react');

var Input = require('../Input');

var InitialReport = React.createClass({
    getInitialState: function () {
        var text = this.props.data.initialReport.text;
        return {
            text: text ? text : ''
        };
    },
    onChange: function (e) {
        var value = e.target.value,
            initialReport = this.props.data.initialReport;
        initialReport.text = value;
        this.setState({text: value});
    },
    render: function () {
        return (
            <div>
                <Input type='textarea' rows='15' label='Initial Report' placeholder='Initial report'
                       value={this.state.text} onChange={this.onChange}/>
            </div>
        );
    }
});

module.exports = InitialReport;
