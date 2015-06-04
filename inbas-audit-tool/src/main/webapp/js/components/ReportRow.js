/**
 * Created by ledvima1 on 27.5.15.
 */
"use strict";

var React = require('react');
var Button = require('react-bootstrap').Button;

var Actions = require('../actions/Actions');

var ReportRow = React.createClass({
    onDoubleClick: function (e) {
        e.preventDefault();
        this.onEditClick();
    },
    onEditClick: function () {
        this.props.edit(this.props.report);
    },
    onDeleteClick: function () {
        Actions.deleteReport({report: this.props.report});
    },
    render: function () {
        var report = this.props.report;
        var date = new Date(report.eventTime);
        var formattedDate = date.toLocaleTimeString() + " " + date.toDateString();
        var authorName = report.author.firstName + " " + report.author.lastName;
        return (
            <tr onDoubleClick={this.onDoubleClick}>
                <td>{formattedDate}</td>
                <td>{authorName}</td>
                <td>{report.description}</td>
                <td>
                    <span className="actions">
                        <Button bsStyle="primary" bsSize="small" onClick={this.onEditClick}>Edit</Button>
                    </span>
                    <span className="actions">
                    <   Button bsStyle="danger" bsSize="small" onClick={this.onDeleteClick}>Delete</Button>
                    </span>
                </td>
            </tr>
        );
    }
});

module.exports = ReportRow;
