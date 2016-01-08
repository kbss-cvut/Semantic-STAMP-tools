/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var injectIntl = require('../../utils/injectIntl');

var Utils = require('../../utils/Utils.js');
var CollapsibleText = require('../CollapsibleText');
var DeleteReportDialog = require('../reports/DeleteReportDialog');
var I18nMixin = require('../../i18n/I18nMixin');

var InvestigationRow = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
        return {
            modalOpen: false
        };
    },
    onDoubleClick: function (e) {
        e.preventDefault();
        this.onEditClick();
    },
    onEditClick: function () {
        this.props.actions.onEdit(this.props.report);
    },
    onDeleteClick: function () {
        this.setState({modalOpen: true});
    },
    onCloseModal: function () {
        this.setState({modalOpen: false});
    },
    removeReport: function () {
        this.props.actions.onRemove(this.props.report);
        this.onCloseModal();
    },


    render: function () {
        var investigation = this.props.report;
        var date = new Date(investigation.occurrenceStart);
        var formattedDate = Utils.formatDate(date);
        // Have to set style directly, class style is overridden by the bootstrap styling
        var verticalAlign = {verticalAlign: 'middle'};
        return (
            <tr onDoubleClick={this.onDoubleClick}>
                <td style={verticalAlign}><a href='javascript:void(0);' onClick={this.onEditClick}
                                             title={this.i18n('investigation.open-tooltip')}>{investigation.occurrence.name}</a>
                </td>
                <td style={verticalAlign}>{formattedDate}</td>
                <td style={verticalAlign}><CollapsibleText text={investigation.summary}/></td>
                <td style={verticalAlign} className='actions'>
                    <Button bsStyle='primary' bsSize='small' title={this.i18n('investigation.edit-tooltip')}
                            onClick={this.onEditClick}>{this.i18n('table-edit')}</Button>
                    <Button bsStyle='warning' bsSize='small' title={this.i18n('investigation.delete-tooltip')}
                            onClick={this.onDeleteClick}>{this.i18n('delete')}</Button>
                    <DeleteReportDialog show={this.state.modalOpen} onClose={this.onCloseModal}
                                        onSubmit={this.removeReport} reportType={'Investigation'}/>
                </td>
            </tr>
        );
    }
});

module.exports = injectIntl(InvestigationRow);
