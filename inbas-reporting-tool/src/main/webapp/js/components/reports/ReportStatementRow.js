/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Glyphicon = require('react-bootstrap').Glyphicon;

var ReportStatementRow = React.createClass({
    propTypes: {
        onRemove: React.PropTypes.func
    },

    onRemove: function () {
        this.props.onRemove(this.props.statementIndex);
    },
    render: function () {
        var cells = [];
        for (var att in this.props.data) {
            var value = this.props.data[att];
            cells.push(<td key={att} style={{whiteSpace: 'pre-wrap'}}>{value}</td>);
        }
        cells.push(
            <td key='cell_actions' className='actions'>
                <Button onClick={this.onRemove} title='Remove statement' bsSize='xsmall' bsStyle='danger'>
                    <Glyphicon glyph='remove'/>
                </Button>
            </td>);
        return (
            <tr>
                {cells}
            </tr>
        );
    }
});

module.exports = ReportStatementRow;
