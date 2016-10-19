'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Label = require('react-bootstrap').Label;
var Reflux = require('reflux');
var classNames = require('classnames');

var injectIntl = require('../../utils/injectIntl');

var Utils = require('../../utils/Utils.js');
var OptionsStore = require('../../stores/OptionsStore');
var ReportType = require('../../model/ReportType');
var Routes = require('../../utils/Routes');
var DeleteReportDialog = require('./DeleteReportDialog');
var I18nMixin = require('../../i18n/I18nMixin');

var ReportRow = React.createClass({
    mixins: [I18nMixin, Reflux.listenTo(OptionsStore, '_onOptionsLoaded')],

    getInitialState: function () {
        return {
            modalOpen: false
        };
    },

    _onOptionsLoaded(type) {
        if (type === 'reportingPhase' || type === 'sira') {
            this.forceUpdate();
        }
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
            statusClasses = classNames(['report-row', 'content-center'], report.getStatusCssClass());

        return <tr onDoubleClick={this.onDoubleClick}>
            <td className='report-row'><a href={'#/' + Routes.reports.path + '/' + report.key}
                                          title={this.i18n('reports.open-tooltip')}>{report.identification}</a>
            </td>
            <td className='report-row content-center'>{this._renderDate(report)}</td>
            <td className='report-row'>{report.renderMoreInfo()}</td>
            <td className={statusClasses}
                title={report.getStatusInfo(OptionsStore.getOptions('sira'), this.props.intl)}>
                <Label title={this.i18n(report.toString())}>{this.i18n(report.getLabel())}</Label>
            </td>
            <td className='report-row content-center'>
                {report.getPhase(OptionsStore.getOptions('reportingPhase'), this.props.intl)}
            </td>
            <td className='report-row actions'>
                <Button bsStyle='primary' bsSize='small' title={this.i18n('reports.open-tooltip')}
                        onClick={this.onEditClick}>{this.i18n('open')}</Button>
                <Button bsStyle='warning' bsSize='small' title={this.i18n('reports.delete-tooltip')}
                        onClick={this.onDeleteClick}>{this.i18n('delete')}</Button>

                <DeleteReportDialog show={this.state.modalOpen} onClose={this.onCloseModal}
                                    onSubmit={this.removeReport}/>
            </td>
        </tr>;
    },

    _renderDate: function (report) {
        return report.date ? Utils.formatDate(new Date(report.date)) : '';
    }
});

module.exports = injectIntl(ReportRow);
