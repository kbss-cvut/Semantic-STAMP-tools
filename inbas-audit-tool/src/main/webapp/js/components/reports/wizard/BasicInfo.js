/**
 * Created by ledvima1 on 11.6.15.
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;
var assign = require('object-assign');
var DateTimePicker = require('react-bootstrap-datetimepicker');

var Actions = require('../../../actions/Actions');
var Utils = require('../../../utils/Utils');

var BasicInfo = React.createClass({
    getInitialState: function () {
            return {
                report: this.props.data.report,
                submitting: false,
                error: null
            };
    },
    isReportNew: function () {
        return !this.props.data.report.uri;
    },
    onChange: function (e) {
        var change;
        if (e.target.name === 'description') {
            change = {description: e.target.value};
        } else {
            change = {name: e.target.value};
        }
        this.setState(assign(this.state.report, change));
    },
    onDateChange: function (value) {
        this.setState(assign(this.state.report, {eventTime: new Date(Number(value))}));
    },
    handleAlertDismiss: function () {
        this.setState(assign({}, this.state, {error: null}));
    },
    render: function () {
        var author = this.state.report.author.firstName + " " + this.state.report.author.lastName;
        var lastEdited = this.renderLastEdited();
        return (
            <Panel header="Basic Event Information">
                <form>
                    <div className="form-group report-detail">
                        <Input type="text" name="name" value={this.state.report.name} onChange={this.onChange}
                               label="Report Name" title="Short descriptive name for this report"/>
                    </div>
                    <div className="form-group report-detail">
                        <Input type="text" value={author} label="Author" title="Report author" disabled/>
                    </div>
                    <div className="picker-container form-group report-detail">
                        <label className="control-label">Event Time</label>
                        <DateTimePicker inputFormat="DD-MM-YY hh:mm A" dateTime={this.state.report.eventTime.toString()}
                                        onChange={this.onDateChange}/>
                    </div>
                    {lastEdited}
                    <div className="form-group">
                        <Input type="textarea" rows="8" label="Description" name="description" placeholder="Event description"
                               value={this.state.report.description} onChange={this.onChange} title="Event description"/>
                    </div>
                </form>
            </Panel>
        );
    },
    renderError: function () {
        return this.state.error ? (
            <div className="form-group">
                <Alert bsStyle="danger" onDismiss={this.handleAlertDismiss} dismissAfter={10000}>
                    <p>Unable to submit report. Server responded with message: {this.state.error.message}</p>
                </Alert>
            </div>
        ) : null;
    },
    renderLastEdited: function () {
        if (this.isReportNew()) {
            return null;
        }
        var formattedDate = Utils.formatDate(new Date(this.state.report.lastEdited));
        var lastEditor = this.state.report.lastEditedBy.firstName + ' ' + this.state.report.lastEditedBy.lastName;
        return (
            <div style={{overflow: 'hidden'}}>
                <div className="report-detail" style={{float: 'left'}}>
                    <Input type="text" label="Last Edited" value={formattedDate} title="Date of last editing of this report" disabled/>
                </div>
                <div className="report-detail" style={{float: 'left', margin: '0em 0em 0em 3em'}}>
                    <Input type="text" label="By" value={lastEditor} title="The user who edited this report last" disabled/>
                </div>
            </div>
        )
    }
});

module.exports = BasicInfo;
