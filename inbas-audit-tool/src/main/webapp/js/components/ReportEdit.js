/**
 * Created by ledvima1 on 27.5.15.
 */

var React = require('react');
var Button = require('react-bootstrap').Button;
var Input = require('react-bootstrap').Input;
var assign = require('object-assign');

var Actions = require('../actions/Actions');

var ReportEdit = React.createClass({
    getInitialState: function () {
        return {
            eventTime: Date.now(),
            author: this.props.user,
            description: ''
        };
    },
    onChange: function (e) {
        this.setState(assign({}, this.state, {
            description: e.target.value
        }));
    },
    onSubmit: function (e) {
        e.preventDefault();
        Actions.createReport({
            eventTime: this.state.eventTime,
            author: this.state.author,
            description: this.state.description
        });
    },
    render: function () {
        var date = new Date(this.state.eventTime);
        var formattedDate = date.toLocaleTimeString() + " " + date.toDateString();
        var author = this.state.author.firstName + " " + this.state.author.lastName;
        return (
            <form>
                <div style={{width: '200px'}}>
                    <Input type="text" value={formattedDate} disabled label="Date Created"/>
                </div>
                <div style={{width: '200px'}}>
                    <Input type="text" value={author} disabled label="Author"/>
                </div>
                <div>
                    <Input type="textarea" label="Description" placeholder="Event description"
                           value={this.state.description} onChange={this.onChange}/>
                </div>
                <Button onClick={this.onSubmit}>Submit</Button>
            </form>
        );
    }
});

module.exports = ReportEdit;
