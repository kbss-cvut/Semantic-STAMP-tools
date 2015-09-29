/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;

var ReportStatementRow = React.createClass({
    propTypes: {
        onRemove: React.PropTypes.func
    },

    onRemove: function () {
        this.props.onRemove(this.props.statementIndex);
    },

    onEdit: function () {
        this.props.onEdit(this.props.statementIndex);
    },

    render: function () {
        var cells = [];
        for (var att in this.props.data) {
            var value = this.props.data[att];
            cells.push(<td key={att} style={{whiteSpace: 'pre-wrap'}}>{value}</td>);
        }
        cells.push(
            <td key='cell_actions' style={{verticalAlign: 'middle'}} className='actions'>
                <Button onClick={this.onEdit} title='Edit statement' bsSize='small' bsStyle='primary'>
                    Edit
                </Button>
                <Button onClick={this.onRemove} title='Remove statement' bsSize='small' bsStyle='danger'>
                    Delete
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
