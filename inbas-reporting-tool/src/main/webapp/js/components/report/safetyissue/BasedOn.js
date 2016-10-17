'use strict';

import React from "react";
import {Button, Label, Panel, Table} from "react-bootstrap";
import Actions from "../../../actions/Actions";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";
import OptionsStore from "../../../stores/OptionsStore";
import Routes from "../../../utils/Routes";
import SafetyIssueBase from "../../../model/SafetyIssueBase";
import TypeaheadStore from "../../../stores/TypeaheadStore";

/**
 * Displays a table of reports on which a safety issue is based.
 */
class BasedOn extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onRemove: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            category: TypeaheadStore.getOccurrenceCategories(),
            findingType: OptionsStore.getOptions('findingType'),
        }
    }

    componentDidMount() {
        Actions.loadOptions('findingType');
        Actions.loadOccurrenceCategories();
        this.unsubscribeOptions = OptionsStore.listen(this._onStoreTrigger);
        this.unsubscribeCategories = TypeaheadStore.listen(this._onStoreTrigger);
    }

    _onStoreTrigger = (type, data) => {
        if (type === 'findingType') {
            this.setState({findingType: data});
        }
        if (typeof type === 'object' && type.action === Actions.loadOccurrenceCategories) {
            this.setState({category: type.data});
        }
    };

    componentWillUnmount() {
        this.unsubscribeCategories();
        this.unsubscribeOptions();
    }

    render() {
        var basedOn = this.props.report.safetyIssue.basedOn;
        if (!basedOn || basedOn.length === 0) {
            return <Panel header={<h5>{this.i18n('safetyissue.based-on')}</h5>} bsStyle='info'>
                <div className='italics'>{this.i18n('safety-issue.base.no-bases')}</div>
            </Panel>;
        }
        var rows = [],
            options = {
                category: this.state.category,
                findingType: this.state.findingType
            };
        for (var i = 0, len = basedOn.length; i < len; i++) {
            rows.push(<BasedOnRow key={'based_on-' + basedOn[i].uri} base={basedOn[i]} onRemove={this.props.onRemove}
                                  options={options}/>);
        }
        return <Panel header={<h5>{this.i18n('safetyissue.based-on')}</h5>} bsStyle='info'>
            <Table striped bordered condensed hover>
                <thead>
                <tr>
                    <th className='col-xs-3 content-center'>{this.i18n('headline')}</th>
                    <th className='col-xs-7 content-center'>{this.i18n('reports.table-moreinfo')}</th>
                    <th className='col-xs-1 content-center'>{this.i18n('reports.table-type')}</th>
                    <th className='col-xs-1 content-center'>{this.i18n('table-actions')}</th>
                </tr>
                </thead>
                <tbody>
                {rows}
                </tbody>
            </Table>
        </Panel>;
    }
}

var BasedOnRow = (props) => {
    var i18n = props.i18n,
        base = SafetyIssueBase.create(props.base);
    return <tr>
        <td className='report-row'><a href={'#/' + Routes.reports.path + '/' + base.reportKey}
                                      title={i18n('reports.open-tooltip')}>{base.getName()}</a></td>
        <td className='report-row'>{base.renderMoreInfo(props.options)}</td>
        <td className='report-row content-center'>
            <Label title={i18n(base.getLabel())}>{i18n(base.getLabel())}</Label>
        </td>
        <td className='report-row actions'>
            <Button bsStyle='warning' onClick={() => props.onRemove(props.base)} bsSize='small'
                    title={i18n('safety-issue.base.remove-tooltip')}>{i18n('remove')}</Button>
        </td>
    </tr>
};

BasedOnRow.propTypes = {
    base: React.PropTypes.object.isRequired,
    onRemove: React.PropTypes.func.isRequired,
    options: React.PropTypes.object
};

BasedOnRow = injectIntl(I18nWrapper(BasedOnRow));

export default injectIntl(I18nWrapper(BasedOn));
