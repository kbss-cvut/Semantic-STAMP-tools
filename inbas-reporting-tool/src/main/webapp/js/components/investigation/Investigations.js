/**
 * @jsx
 */

'use strict';

var React = require('react');

var ReportsTable = require('../reports/ReportsTable');
var InvestigationRow = require('./InvestigationRow');

var Investigations = React.createClass({
    render: function () {
        if (this.props.investigations.length === 0) {
            return <div className='italic' {...this.props}>There are currently no investigations.</div>;
        } else {
            return <ReportsTable reports={this.props.investigations} title={'Investigation reports'}
                                 rowComponent={InvestigationRow} onEditReport={this.props.onEditInvestigation}/>;
        }
    }
});

module.exports = Investigations;
