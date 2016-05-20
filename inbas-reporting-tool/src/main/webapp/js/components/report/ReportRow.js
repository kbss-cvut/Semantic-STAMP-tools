/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Label = require('react-bootstrap').Label;

var injectIntl = require('../../utils/injectIntl');

var Utils = require('../../utils/Utils.js');
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
        var report = ReportType.getReport(this.props.report),
            formattedDate = '',
        // Have to set style directly, class style is overridden by the bootstrap styling
            verticalAlign = {verticalAlign: 'middle'};
        if (report.date) {
            formattedDate = Utils.formatDate(new Date(report.date));
        }
        return (
            <tr onDoubleClick={this.onDoubleClick}>
                <td style={verticalAlign}><a href='javascript:void(0);' onClick={this.onEditClick}
                                             title={this.i18n('reports.open-tooltip')}>{report.identification}</a>
                </td>
                <td style={verticalAlign} className='content-center'>{formattedDate}</td>
                <td style={verticalAlign}>{report.renderMoreInfo()}</td>
                <td style={verticalAlign} className='content-center'>
                    <Label title={this.i18n(report.toString())}>{this.i18n(report.getLabel())}</Label>
                </td>
                <td style={verticalAlign} className='actions'>
                    <Button bsStyle='primary' bsSize='small' title={this.i18n('reports.open-tooltip')}
                            onClick={this.onEditClick}>{this.i18n('open')}</Button>
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
