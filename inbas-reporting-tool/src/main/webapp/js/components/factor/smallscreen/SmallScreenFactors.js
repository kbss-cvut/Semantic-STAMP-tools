'use strict';

import React from "react";
import {Button, Panel, Table} from "react-bootstrap";
import JsonLdUtils from "jsonld-utils";
import Actions from "../../../actions/Actions";
import Constants from "../../../constants/Constants";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import ObjectTypeResolver from "../../../utils/ObjectTypeResolver";
import OptionsStore from "../../../stores/OptionsStore";
import Utils from "../../../utils/Utils";
import Vocabulary from "../../../constants/Vocabulary";

class SmallScreenFactors extends React.Component {

    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
        Actions.loadOptions(Constants.OPTIONS.EVENT_TYPE);
    }

    _onOptionsLoaded = (type) => {
        if (type === Constants.OPTIONS.EVENT_TYPE) {
            this.forceUpdate();
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    render() {
        if (!this.props.report.factorGraph) {
            return null;
        }
        return <Panel header={<h5>{this.i18n('factors.panel-title')}</h5>} bsStyle='info'>
            <Table striped bordered condensed hover>
                <thead>
                <tr>
                    <th className='content-center col-xs-4'>{this.i18n('report.eventtype.table-type')}</th>
                    <th className='content-center col-xs-3'>{this.i18n('factors.smallscreen.start')}</th>
                    <th className='content-center col-xs-3'>{this.i18n('factors.smallscreen.end')}</th>
                    <th className='content-center col-xw-2'>{this.i18n('table-actions')}</th>
                </tr>
                </thead>
                <tbody>
                {this._renderFactors()}
                </tbody>
            </Table>
        </Panel>;
    }

    _renderFactors() {
        const factorGraph = this.props.report.factorGraph,
            rows = [],
            eventTypes = OptionsStore.getOptions(Constants.OPTIONS.EVENT_TYPE);
        let node;
        for (let i = 0, len = factorGraph.nodes.length; i < len; i++) {
            node = factorGraph.nodes[i];
            if (node === this.props.report.occurrence) {
                continue;
            }
            rows.push(<FactorRow key={node.uri ? node.uri : 'node_' + i} node={node} eventTypes={eventTypes}/>);
        }
        return rows;
    }
}

let FactorRow = (props) => {
    const node = props.node,
        eventType = ObjectTypeResolver.resolveType(node.eventType, props.eventTypes),
        text = eventType ? JsonLdUtils.getJsonAttValue(eventType, Vocabulary.RDFS_LABEL) : node.eventType;
    return <tr>
        <td className='report-row'>{text}</td>
        <td className='report-row content-center'>{Utils.formatDate(node.startTime)}</td>
        <td className='report-row content-center'>{Utils.formatDate(node.endTime)}</td>
        <td className='report-row actions'>
            <Button bsStyle='primary' bsSize='small'>{props.i18n('factors.detail.details')}</Button>
            <Button bsStyle='primary' bsSize='small'>{props.i18n('table-edit')}</Button>
            <Button bsStyle='warning' bsSize='small'>{props.i18n('delete')}</Button>
        </td>
    </tr>;
};

FactorRow.propTypes = {
    node: React.PropTypes.object.isRequired,
    eventTypes: React.PropTypes.array.isRequired
};

FactorRow = injectIntl(I18nWrapper(FactorRow));

export default injectIntl(I18nWrapper(SmallScreenFactors));
