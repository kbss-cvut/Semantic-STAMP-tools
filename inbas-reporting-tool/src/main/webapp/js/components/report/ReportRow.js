/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Label = require('react-bootstrap').Label;

var injectIntl = require('../../utils/injectIntl');

var Utils = require('../../utils/Utils.js');
var CollapsibleText = require('../CollapsibleText');
var ReportType = require('../../model/ReportType');
var DeleteReportDialog = require('./DeleteReportDialog');
var I18nMixin = require('../../i18n/I18nMixin');

var ReportRow = React.createClass({
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
        var report = this.props.report,
            date = new Date(report.occurrenceStart),
            formattedDate = Utils.formatDate(date),
        // Have to set style directly, class style is overridden by the bootstrap styling
            verticalAlign = {verticalAlign: 'middle'};
        return (
            <tr onDoubleClick={this.onDoubleClick}>
                <td style={verticalAlign}><a href='javascript:void(0);' onClick={this.onEditClick}
                                             title={this.i18n('reports.open-tooltip')}>{report.occurrence.name}</a>
                </td>
                <td style={verticalAlign} className='content-center'>{formattedDate}</td>
                <td style={verticalAlign}><CollapsibleText text={report.summary}/></td>
                <td style={verticalAlign}>{report.occurrence.eventType}</td>
                <td style={verticalAlign} className='content-center'><Label>{ReportType.asString(report)}</Label></td>
                <td style={verticalAlign} className='actions'>
                    <Button bsStyle='primary' bsSize='small' title={this.i18n('reports.edit-tooltip')}
                            onClick={this.onEditClick}>{this.i18n('table-edit')}</Button>
                    <Button bsStyle='warning' bsSize='small' title={this.i18n('reports.delete-tooltip')}
                            onClick={this.onDeleteClick}>{this.i18n('delete')}</Button>

                    <DeleteReportDialog show={this.state.modalOpen} onClose={this.onCloseModal}
                                        onSubmit={this.removeReport}/>
                </td>
            </tr>
        );
    }
});

module.exports = injectIntl(ReportRow);
