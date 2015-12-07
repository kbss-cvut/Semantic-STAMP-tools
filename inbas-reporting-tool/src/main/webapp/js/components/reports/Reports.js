/**
 * @jsx
 */

'use strict';

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;

var IntlMixin = require('react-intl').IntlMixin;

var ReportsTable = require('./ReportsTable');
var Mask = require('./../Mask');

var Reports = React.createClass({
    mixins: [IntlMixin],

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
                <Mask text={this.getIntlMessage('reports.loading-mask')}/>
            );
        }
        if (reports.length === 0) {
            return (
                <div>
                    <Jumbotron>
                        <h2>INBAS Reporting</h2>

                        <p>{this.getIntlMessage('reports.no-reports')}</p>
                    </Jumbotron>
                </div>
            );
        } else {
            return (<ReportsTable {...this.props}/>);
        }

    }
});

module.exports = Reports;
