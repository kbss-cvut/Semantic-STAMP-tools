/**
 * @jsx
 */

'use strict';

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;

var ReportsTable = require('./ReportsTable');
var Mask = require('./../Mask');

var Reports = React.createClass({

    propTypes: {
        reports: React.PropTypes.array,
        rowComponent: React.PropTypes.func,     // A react component
        onEdit: React.PropTypes.func,
        onRemove: React.PropTypes.func
    },

    render: function () {
        var reports = this.props.reports;
        if (reports === null) {
            return (
                <Mask text='Loading reports...'/>
            );
        }
        if (reports.length === 0) {
            return (
                <div>
                    <Jumbotron>
                        <h2>INBAS Reporting</h2>

                        <p>There are no reports here, yet.</p>
                    </Jumbotron>
                </div>
            );
        } else {
            return (<ReportsTable {...this.props}/>);
        }

    }
});

module.exports = Reports;
