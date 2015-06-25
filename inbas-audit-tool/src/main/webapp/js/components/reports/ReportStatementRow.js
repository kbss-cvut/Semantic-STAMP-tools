/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;

var ReportStatementRow = React.createClass({
    render: function () {
        var cells = [];
        var len = this.props.data.length;
        for (var att in this.props.data) {
            var value = this.props.data[att];
            cells.push(<td key={att}>{value}</td>);
        }
        return (
            <tr>
                {cells}
            </tr>
        );
    }
});

module.exports = ReportStatementRow;
