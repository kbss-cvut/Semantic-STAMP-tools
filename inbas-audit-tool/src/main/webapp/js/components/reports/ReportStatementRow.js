/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;

var ReportStatementRow = React.createClass({
    render: function () {
        var cells = [];
        var len = this.props.attributes.length;
        for (var i = 0; i < len; i++) {
            var attName = this.props.attributes[i];
            cells.push(<td key={attName}>{this.props.statement[attName]}</td>);
        }
        return (
            <tr>
                {cells}
            </tr>
        );
    }
});

module.exports = ReportStatementRow;
