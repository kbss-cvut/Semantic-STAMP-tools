/**
 * Created by ledvima1 on 27.5.15.
 */

var React = require('react');

var ReportRow = React.createClass({
    render: function () {
        var report = this.props.report;
        var date = new Date(report.eventTime);
        var formattedDate = date.toLocaleTimeString() + " " + date.toDateString();
        var authorName = report.author.firstName + " " + report.author.lastName;
        return (
            <tr>
                <td>{formattedDate}</td>
                <td>{authorName}</td>
                <td>{report.description}</td>
            </tr>
        );
    }
});

module.exports = ReportRow;
