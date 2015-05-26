/**
 * Created by ledvima1 on 26.5.15.
 */

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Button = require('react-bootstrap').Button;

var Reports = React.createClass({
    render: function () {
        var reports = this.props.reports;
        if (reports.length === 0) {
            return (
                <Jumbotron>
                    <h2>INBAS Reporting</h2>
                    <p>There are no reports, yet.</p>
                    <Button bsStyle="primary">Create Report</Button>
                </Jumbotron>
            );
        } else {
            return null;
        }
    }
});

module.exports = Reports;
