/**
 * Created by ledvima1 on 26.5.15.
 */

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Button = require('react-bootstrap').Button;

var ReportsTable = require('./ReportsTable');
var ReportEdit = require('./ReportEdit');

var Reports = React.createClass({
    getInitialState: function () {
        return {
            creating: false
        };
    },
    createReport: function () {
        this.setState({
            creating: true
        });
    },
    render: function () {
        var edit = null;
        if (this.state.creating) {
            edit = <ReportEdit user={this.props.user}/>
        }
        var reports = this.props.reports;
        if (reports.length === 0) {
            return (
                <div>
                    <Jumbotron>
                        <h2>INBAS Reporting</h2>

                        <p>There are no reports, yet.</p>
                        <Button bsStyle="primary" onClick={this.createReport}>Create Report</Button>
                    </Jumbotron>
                    {edit}
                </div>
            );
        } else {
            return (
                <div>
                    <ReportsTable reports={reports}/>
                    {edit}
                </div>
            );
        }


    }
});

module.exports = Reports;
