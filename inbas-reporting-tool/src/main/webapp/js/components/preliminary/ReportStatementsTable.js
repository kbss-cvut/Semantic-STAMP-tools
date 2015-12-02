/**
 * @jsx
 */

'use strict';

var React = require('react');
var Table = require('react-bootstrap').Table;

var ReportStatementRow = require('./ReportStatementRow');


/**
 * When setting header flex, don't forget that maximum flex is 12 and 1 goes to the Actions column, so effectively 11 is
 * available to the sum of passed flex values.
 */
var ReportStatementsTable = React.createClass({

    propTypes: {
        data: React.PropTypes.array,
        header: React.PropTypes.array,
        type: React.PropTypes.string,
        handlers: React.PropTypes.object
    },

    render: function () {
        var data = this.props.data;
        var handlers = this.props.handlers;
        var len = data.length;
        var rows = [];
        for (var i = 0; i < len; i++) {
            var toShow = {};
            var columnCount = this.props.header.length;
            for (var j = 0; j < columnCount; j++) {
                var attName = this.props.header[j].attribute;
                toShow[attName] = data[i][attName];
            }
            rows.push(<ReportStatementRow key={this.props.keyBase + i} statementIndex={i} data={toShow}
                                          onRemove={handlers.onRemove} onEdit={handlers.onEdit}/>);
        }
        return (
            <Table striped bordered condensed hover>
                <thead>
                {this.renderHeader()}
                </thead>
                <tbody>
                {rows}
                </tbody>
            </Table>
        );
    },
    renderHeader: function () {
        var header = this.props.header;
        var len = header.length;
        var tableHeader = [];
        for (var i = 0; i < len; i++) {
            var cls = header[i].flex ? 'col-xs-' + header[i].flex : null;
            tableHeader.push(<th key={'col_' + header[i].name} className={cls}>{header[i].name}</th>);
        }
        tableHeader.push(<th key='col_actions' className='col-xs-1'>Actions</th>);
        return (
            <tr key={this.props.keyBase + 'header'}>
                {tableHeader}
            </tr>
        );
    }
});

module.exports = ReportStatementsTable;
