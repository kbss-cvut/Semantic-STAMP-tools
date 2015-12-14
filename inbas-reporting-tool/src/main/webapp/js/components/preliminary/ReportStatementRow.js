/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var injectIntl = require('react-intl').injectIntl;

var I18nMixin = require('../../i18n/I18nMixin');

var ReportStatementRow = React.createClass({
    mixins: [I18nMixin],

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
                <Button onClick={this.onEdit} title={this.i18n('preliminary.detail.table-edit-tooltip')}
                        bsSize='small' bsStyle='primary'>
                    {this.i18n('table-edit')}
                </Button>
                <Button onClick={this.onRemove} title={this.i18n('preliminary.detail.table-delete-tooltip')}
                        bsSize='small' bsStyle='warning'>
                    {this.i18n('delete')}
                </Button>
            </td>);
        return (
            <tr>
                {cells}
            </tr>
        );
    }
});

module.exports = injectIntl(ReportStatementRow);
