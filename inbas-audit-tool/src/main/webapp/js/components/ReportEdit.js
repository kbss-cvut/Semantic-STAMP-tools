/**
 * Created by ledvima1 on 27.5.15.
 */

var React = require('react');
var Button = require('react-bootstrap').Button;
var Input = require('react-bootstrap').Input;
var assign = require('object-assign');
var DateTimePicker = require('react-bootstrap-datetimepicker');

var Actions = require('../actions/Actions');

var ReportEdit = React.createClass({
    getInitialState: function () {
        return {
            eventTime: Date.now(),
            description: ''
        };
    },
    onChange: function (e) {
        this.setState(assign({}, this.state, {
            description: e.target.value
        }));
        if (e.target.value !== '') {
            this.refs.submit.getDOMNode().disabled = '';
        } else {
            this.refs.submit.getDOMNode().disabled = 'disabled';
        }

    },
    onDateChange: function(value) {
        this.setState(assign({}, this.state, {eventTime: new Date(Number(value))}));
    },
    onSubmit: function (e) {
        e.preventDefault();
        Actions.createReport({
            eventTime: this.state.eventTime,
            author: this.props.user,
            description: this.state.description
        });
    },
    render: function () {
        var author = this.props.user.firstName + " " + this.props.user.lastName;
        return (
            <form>
                <div className="picker-container" style={{width: '250px'}}>
                    <DateTimePicker inputFormat="DD-MM-YY hh:mm" dateTime={this.state.eventTime.toString()} onChange={this.onDateChange} />
                </div>
                <div style={{width: '250px'}}>
                    <Input type="text" value={author} disabled label="Author"/>
                </div>
                <div>
                    <Input type="textarea" label="Description" placeholder="Event description"
                           value={this.state.description} onChange={this.onChange}/>
                </div>
                <Button disabled ref="submit" onClick={this.onSubmit}>Submit</Button>
            </form>
        );
    }
});

module.exports = ReportEdit;
