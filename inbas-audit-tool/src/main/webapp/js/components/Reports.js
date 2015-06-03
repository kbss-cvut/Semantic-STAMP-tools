/**
 * Created by ledvima1 on 26.5.15.
 */

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Button = require('react-bootstrap').Button;

var ReportsTable = require('./ReportsTable');
var ReportEdit = require('./ReportEdit');

var Reports = React.createClass({
    render: function () {
        var edit = null;
        if (this.props.edit.editing) {
            edit = <ReportEdit user={this.props.user}/>
        }
        var reports = this.props.reports;
        if (reports.length === 0) {
            return (
                <div>
                    <Jumbotron>
                        <h2>INBAS Reporting</h2>

                        <p>There are no reports, yet.</p>
                        <Button bsStyle="primary" onClick={this.props.edit.callback}>Create Report</Button>
                    </Jumbotron>
                    {edit}
                </div>
            );
        } else {
            var table = <ReportsTable reports={reports}/>;
            var create = edit !== null ? null : <Button bsStyle="primary" onClick={this.props.edit.callback}>Create Report</Button>;
            return (
                <div>
                    {table}
                    {create}
                    {edit}
                </div>
            );
        }


    }
});

module.exports = Reports;
