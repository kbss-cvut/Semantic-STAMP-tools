/**
 * Created by ledvima1 on 27.5.15.
 */

"use strict";

var React = require('react');
var Button = require('react-bootstrap').Button;
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;
var assign = require('object-assign');
var DateTimePicker = require('react-bootstrap-datetimepicker');

var Actions = require('../actions/Actions');

var ReportEdit = React.createClass({
    getInitialState: function () {
        if (!this.isReportNew()) {
            return {
                eventTime: this.props.report.eventTime,
                description: this.props.report.description,
                author: this.props.report.author,
                submitting: false
            }
        }
        return {
            eventTime: Date.now(),
            description: '',
            author: this.props.user,
            submitting: false
        };
    },
    onChange: function (e) {
        this.setState(assign({}, this.state, {
            description: e.target.value
        }));
    },
    onDateChange: function (value) {
        this.setState(assign({}, this.state, {eventTime: new Date(Number(value))}));
    },
    onSubmit: function (e) {
        e.preventDefault();
        this.setState(assign({}, this.state, {submitting: true}));
        var report = {
            eventTime: this.state.eventTime,
            author: this.state.author,
            lastEditedBy: this.props.user,
            description: this.state.description
        };
        if (this.isReportNew()) {
            Actions.createReport(report);
        }
        else {
            report = assign({}, this.props.report, report);
            Actions.updateReport(report);
        }
    },
    isReportNew: function() {
        return this.props.report == null;
    },
    render: function () {
        var author = this.state.author.firstName + " " + this.state.author.lastName;
        var loading = this.state.submitting;
        return (
            <Panel header="Edit Event Report">
                <form>
                    <div className="form-group" style={{width: '250px'}}>
                        <Input type="text" value={author} disabled label="Author"/>
                    </div>
                    <div className="picker-container form-group" style={{width: '250px'}}>
                        <label className="control-label">Event Time</label>
                        <DateTimePicker inputFormat="DD-MM-YY hh:mm" dateTime={this.state.eventTime.toString()}
                                        onChange={this.onDateChange}/>
                    </div>
                    <div className="form-group">
                        <Input type="textarea" label="Description" placeholder="Event description"
                               value={this.state.description} onChange={this.onChange}/>
                    </div>
                    <Button bsStyle="success" disabled={loading || this.state.description === ''} ref="submit"
                            onClick={this.onSubmit}>{loading ? 'Submitting...' : 'Submit'}</Button>
                    <Button bsStyle="link" onClick={this.props.cancelEdit}>Cancel</Button>
                </form>
            </Panel>
        );
    }
});

module.exports = ReportEdit;
