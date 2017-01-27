'use strict';

import React from "react";
import {Button, Glyphicon, Panel, Table} from "react-bootstrap";
import assign from "object-assign";
import JsonLdUtils from "jsonld-utils";
import {QuestionAnswerProcessor} from "semforms";
import Actions from "../../../actions/Actions";
import Constants from "../../../constants/Constants";
import DeleteFactorDialog from "../DeleteFactorDialog";
import EventValidator from "../../../validation/EventValidator";
import FactorEditRow from "./FactorEditRow";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import LoadingWrapper from "../../misc/hoc/LoadingWrapper";
import MessageWrapper from "../../misc/hoc/MessageWrapper";
import ObjectTypeResolver from "../../../utils/ObjectTypeResolver";
import OptionsStore from "../../../stores/OptionsStore";
import ReportFactory from "../../../model/ReportFactory";
import Utils from "../../../utils/Utils";
import Vocabulary from "../../../constants/Vocabulary";
import WizardGenerator from "../../wizard/generator/WizardGenerator";
import WizardWindow from "../../wizard/WizardWindow";

class SmallScreenFactors extends React.Component {

    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired,
        rootAttribute: React.PropTypes.string.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = SmallScreenFactors._getInitialState(props.report);
    }

    static _getInitialState(report) {
        return {
            showDeleteDialog: false,
            editRow: false,
            currentFactor: null,
            factorGraph: {
                nodes: report.factorGraph ? report.factorGraph.nodes.slice() : [],
                edges: report.factorGraph ? report.factorGraph.edges : []
            },
            wizardOpen: false,
            wizardProperties: null
        };
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

    componentDidUpdate(prevProps) {
        if (this.props.report !== prevProps.report) {
            this.setState(SmallScreenFactors._getInitialState(this.props.report));
        }
    }

    _onDeleteClick = (factor) => {
        this.setState({showDeleteDialog: true, currentFactor: factor});
    };

    _onDeleteCancel = () => {
        this.setState({showDeleteDialog: false, currentFactor: null});
    };

    _onDeleteSubmit = () => {
        const newFactorGraph = assign({}, this.state.factorGraph);
        newFactorGraph.nodes.splice(newFactorGraph.nodes.indexOf(this.state.currentFactor), 1);
        this.setState({factorGraph: newFactorGraph, currentFactor: null, showDeleteDialog: false});
    };

    _onEditClick = (factor) => {
        this.setState({editRow: true, currentFactor: factor});
    };

    _onEditCancel = () => {
        this.setState({editRow: false, currentFactor: null});
    };

    _onEditFinish = (factor) => {
        const validation = EventValidator.validate(factor);
        if (!validation.valid) {
            this.props.showWarnMessage(this.props.formatMessage(validation.message));
            return;
        }
        const update = this._updateFactor(factor);
        update.editRow = false;
        this.setState(update);
    };

    _updateFactor(factor) {
        const newFactorGraph = assign({}, this.state.factorGraph);
        newFactorGraph.nodes.splice(newFactorGraph.nodes.indexOf(this.state.currentFactor), 1, factor);
        return {factorGraph: newFactorGraph, currentFactor: null};
    }

    _onAdd = () => {
        const rootNode = this.props.report[this.props.rootAttribute],
            newFactor = ReportFactory.createFactor(rootNode),
            newFactorGraph = {
                nodes: this.state.factorGraph.nodes.slice(),
                edges: this.state.factorGraph.edges.slice()
            };
        newFactor.referenceId = Utils.generateNewReferenceId(newFactorGraph.nodes);
        newFactorGraph.nodes.push(newFactor);
        const newLink = {
            from: rootNode,
            to: newFactor,
            linkType: Vocabulary.HAS_PART
        };
        newFactorGraph.edges.push(newLink);
        this.setState({factorGraph: newFactorGraph, currentFactor: newFactor, editRow: true});
    };

    getFactorGraph() {
        return this.state.factorGraph;
    }

    _onOpenDetails = (factor, label) => {
        const report = assign({}, this.props.report);
        report.factorGraph = this.state.factorGraph;
        this.props.loadingOn();
        this.setState({currentFactor: factor});
        WizardGenerator.generateWizard(report, factor, label, this._openDetailsWizard);
    };

    _openDetailsWizard = (wizardProperties) => {
        this.props.loadingOff();
        wizardProperties.onFinish = this._onUpdateFactorDetails;
        this.setState({
            wizardOpen: true,
            wizardProperties: wizardProperties
        });
    };

    _onCloseDetails = () => {
        this.setState({wizardOpen: false});
    };

    _onUpdateFactorDetails = (data, closeCallback) => {
        const factor = assign({}, this.state.currentFactor);

        factor.question = QuestionAnswerProcessor.buildQuestionAnswerModel(data.data, data.stepData);
        const update = this._updateFactor(factor);
        this.setState({update});
        closeCallback();
    };

    render() {
        if (!this.props.report.factorGraph) {
            return null;
        }
        const table = this._renderTable();
        return <Panel header={<h5>{this.i18n('factors.panel-title')}</h5>} bsStyle='info'>
            <DeleteFactorDialog onSubmit={this._onDeleteSubmit} onCancel={this._onDeleteCancel}
                                show={this.state.showDeleteDialog}/>
            <WizardWindow {...this.state.wizardProperties} show={this.state.wizardOpen} onHide={this._onCloseDetails}
                          enableForwardSkip={true}/>
            {table}
            <div className={table ? 'float-right' : ''}>
                <Button bsStyle='primary' bsSize='small' onClick={this._onAdd}
                        title={this.props.i18n('factors.smallscreen.add-tooltip')}>
                    <Glyphicon glyph='plus' className='add-glyph'/>
                    {this.props.i18n('add')}
                </Button>
            </div>
        </Panel>;
    }

    _renderTable() {
        return this.state.factorGraph.nodes.length <= 1 ? null : <Table striped bordered condensed hover>
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
            </Table>;
    }

    _renderFactors() {
        const factorGraph = this.state.factorGraph,
            rows = [],
            eventTypes = OptionsStore.getOptions(Constants.OPTIONS.EVENT_TYPE),
            handlers = {
                onDelete: this._onDeleteClick,
                onEdit: this._onEditClick,
                onEditCancel: this._onEditCancel,
                onSave: this._onEditFinish,
                onDetails: this._onOpenDetails
            };
        let node;
        // Skip node 0 - the root node
        for (let i = 1, len = factorGraph.nodes.length; i < len; i++) {
            node = factorGraph.nodes[i];
            if (this.state.editRow && node === this.state.currentFactor) {
                rows.push(<FactorEditRow key={node.uri ? node.uri : 'node_' + i} factor={node} handlers={handlers}/>);
            } else {
                rows.push(<FactorRow key={node.uri ? node.uri : 'node_' + i} node={node} eventTypes={eventTypes}
                                     handlers={handlers}/>);
            }
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
            <Button bsStyle='primary' bsSize='small'
                    onClick={(e) => props.handlers.onDetails(node, text)}>{props.i18n('factors.detail.details')}</Button>
            <Button bsStyle='primary' bsSize='small'
                    onClick={(e) => props.handlers.onEdit(node)}>{props.i18n('table-edit')}</Button>
            <Button bsStyle='warning' bsSize='small'
                    onClick={(e) => props.handlers.onDelete(node)}>{props.i18n('delete')}</Button>
        </td>
    </tr>;
};

FactorRow.propTypes = {
    node: React.PropTypes.object.isRequired,
    eventTypes: React.PropTypes.array.isRequired,
    handlers: React.PropTypes.object.isRequired
};

FactorRow = injectIntl(I18nWrapper(FactorRow));

export default injectIntl(I18nWrapper(MessageWrapper(LoadingWrapper(SmallScreenFactors))), {withRef: true});
