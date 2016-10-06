'use strict';

import React from "react";
import {Button, Label, Panel, Table} from "react-bootstrap";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";
import ReportType from "../../../model/ReportType";
import Routes from "../../../utils/Routes";

/**
 * Displays a table of reports on which a safety issue is based.
 */
var BasedOn = (props) => {
    var basedOn = props.report.safetyIssue.basedOn,
        i18n = props.i18n;
    if (!basedOn || basedOn.length === 0) {
        return <Panel header={<h5>{i18n('safetyissue.based-on')}</h5>} bsStyle='info'>
            <div className='italics'>{i18n('safety-issue.base.no-bases')}</div>
        </Panel>;
    }
    var rows = [];
    for (var i = 0, len = basedOn.length; i < len; i++) {
        rows.push(<Row key={'based_on-' + basedOn[i].key} report={basedOn[i]} onRemove={props.onRemove}/>);
    }
    return <Panel header={<h5>{i18n('safetyissue.based-on')}</h5>} bsStyle='info'>
        <Table striped bordered condensed hover>
            <thead>
            <tr>
                <th className='col-xs-2 content-center'>{i18n('headline')}</th>
                <th className='col-xs-8 content-center'>{i18n('reports.table-moreinfo')}</th>
                <th className='col-xs-1 content-center'>{i18n('reports.table-type')}</th>
                <th className='col-xs-1 content-center'>{i18n('table-actions')}</th>
            </tr>
            </thead>
            <tbody>
            {rows}
            </tbody>
        </Table>
    </Panel>;
};

BasedOn.propTypes = {
    report: React.PropTypes.object.isRequired,
    onRemove: React.PropTypes.func.isRequired
};

var Row = (props) => {
    var i18n = props.i18n,
        report = ReportType.getReport(props.report);
    return <tr>
        <td className='report-row'><a href={'#/' + Routes.reports.path + '/' + report.key}
                                      title={i18n('reports.open-tooltip')}>{report.identification}</a></td>
        <td className='report-row'>{report.renderMoreInfo()}</td>
        <td className='report-row content-center'>
            <Label title={i18n(report.toString())}>{i18n(report.getLabel())}</Label>
        </td>
        <td className='report-row actions'>
            <Button bsStyle='warning' onClick={() => props.onRemove(props.report)} bsSize='small'
                    title={i18n('safety-issue.base.remove-tooltip')}>{i18n('remove')}</Button>
        </td>
    </tr>
};

Row.propTypes = {
    report: React.PropTypes.object.isRequired,
    onRemove: React.PropTypes.func.isRequired
};

Row = injectIntl(I18nWrapper(Row));

export default injectIntl(I18nWrapper(BasedOn));
