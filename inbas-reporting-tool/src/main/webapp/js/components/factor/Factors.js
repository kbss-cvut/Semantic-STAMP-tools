'use strict';

import ObjectTypeResolver from "../../utils/ObjectTypeResolver";

const React = require('react');
const Reflux = require('reflux');
const assign = require('object-assign');
const Button = require('react-bootstrap').Button;
const Modal = require('react-bootstrap').Modal;
const Panel = require('react-bootstrap').Panel;
const injectIntl = require('../../utils/injectIntl');
const FormattedMessage = require('react-intl').FormattedMessage;
const JsonLdUtils = require('jsonld-utils').default;

const Input = require('../Input').default;
const Select = require('../Select');

const Actions = require('../../actions/Actions');
const Constants = require('../../constants/Constants');
const Vocabulary = require('../../constants/Vocabulary');
const FactorDetail = require('./FactorDetail').default;
const LossEventDetail = require('./LossEventDetail').default;
const FactorRenderer = require('./FactorRenderer');
const GanttController = require('./GanttController');
const FactorJsonSerializer = require('../../utils/FactorJsonSerializer');
const I18nMixin = require('../../i18n/I18nMixin');
const Utils = require('../../utils/Utils');

const OptionsStore = require('../../stores/OptionsStore');

const Factors = React.createClass({
    mixins: [I18nMixin, Reflux.listenTo(OptionsStore, '_onOptionsLoaded')],

    propTypes: {
        report: React.PropTypes.object.isRequired,
        rootAttribute: React.PropTypes.string.isRequired,
        onChange: React.PropTypes.func.isRequired,
        enableDetails: React.PropTypes.bool
    },

    ganttController: null,
    factorReferenceIdCounter: 0,

    getDefaultProps: function () {
        return {
            enableDetails: true
        };
    },

    getInitialState: function () {
        return {
            scale: Utils.determineTimeScale(this.props.report[this.props.rootAttribute]),
            showLinkTypeDialog: false,
            currentLink: null,
            currentLinkSource: null,
            currentLinkTarget: null,
            showFactorDialog: false,
            currentFactor: null,
            factorTypeOptions: JsonLdUtils.processSelectOptions(OptionsStore.getOptions('factorType')),
            showDeleteLinkDialog: false,
            lossEvent: null
        }
    },

    componentDidUpdate: function () {
        if (this.factorsRendered) {
            if (this._reportReloaded()) {
                this.factorsRendered = false;
                this.ganttController.clearAll();
                this.renderFactors(OptionsStore.getOptions(Constants.OPTIONS.EVENT_TYPE));
            } else
                this.ganttController.updateRootEvent(this.props.report[this.props.rootAttribute]);
        }
    },

    _reportReloaded: function () {
        return this.rootReferenceId !== this.props.report[this.props.rootAttribute].referenceId;
    },

    _initLossEventReferenceId: function() {
        return this.getLossEventReferenceId();
    },

    _getFactorEventType: function(){
        if(this.state.currentFactor && this.state.currentFactor.statement){
            const parentFactor = this.ganttController.getFactor(this.state.currentFactor.parent);
            if(parentFactor.referenceId !== this.rootReferenceId){
                return parentFactor.eventType;
            }
        }
        return null;
    },

    getLossEventReferenceId: function(){
        let nodes = this.ganttController.getAllFactors();
        if(!nodes) return;
        for (let i = 0, len = nodes.length; i < len; i++) {
            let node = nodes[i];
            if(!node) continue;
            if(node && node.statement &&
                node.statement.types &&
                node.statement.types.includes &&
                node.statement.types.includes(Vocabulary.LOSS_EVENT)){
                return node;
            }
        }
    },

    componentWillMount: function () {
        Actions.loadOptions(Constants.OPTIONS.EVENT_TYPE);
    },

    componentDidMount: function () {
        this.ganttController = GanttController;
        this.ganttController.init({
            onLinkAdded: this.onLinkAdded,
            onCreateFactor: this.onCreateFactor,
            onEditFactor: this.onEditFactor,
            updateGraphRoot: this.onGraphRootUpdate,
            onDeleteLink: this.onDeleteLink
        });
        this.ganttController.setScale(this.state.scale);
        if (OptionsStore.getOptions(Constants.OPTIONS.EVENT_TYPE).length !== 0) {
            this.renderFactors(
                [
                    Constants.OPTIONS.EVENT_TYPE,
                    Constants.OPTIONS.LOSS_EVENT_TYPE
                ]);
        }
    },

    _onOptionsLoaded: function (type, data) {
        if (type === Constants.OPTIONS.EVENT_TYPE) {
            this.renderFactors(data);
        } else if (type === Constants.OPTIONS.FACTOR_TYPE) {
            this.setState({factorTypeOptions: JsonLdUtils.processSelectOptions(data)});
        }
    },

    renderFactors: function (eventTypes) {
        if (this.factorsRendered) {
            return;
        }
        this.factorsRendered = true;
        this.rootReferenceId = this.props.report[this.props.rootAttribute].referenceId;
        FactorRenderer.renderFactors(this.props.report,
            {optionTypes : [
                Constants.OPTIONS.EVENT_TYPE,
                Constants.OPTIONS.LOSS_EVENT_TYPE
            ]});
        this.ganttController.expandSubtree(this.ganttController.rootEventId);
        this.factorReferenceIdCounter = FactorRenderer.greatestReferenceId;
    },

    onLinkAdded: function (link) {
        this.setState({currentLink: link, showLinkTypeDialog: true});
    },

    onLinkTypeSelect: function (e) {
        const link = this.state.currentLink;
        link.factorType = e.target.value;
        this.ganttController.addLink(link);
        this.onCloseLinkTypeDialog();
    },

    onCloseLinkTypeDialog: function () {
        this.setState({currentLink: null, showLinkTypeDialog: false});
    },

    onCreateFactor: function (factor) {
        factor.statement.referenceId = ++this.factorReferenceIdCounter;
        this.setState({showFactorDialog: true, currentFactor: factor});
    },

    onEditFactor: function (factor) {
        this.setState({currentFactor: factor, showFactorDialog: true});
    },

    onSaveFactor: function () {
        const factor = this.state.currentFactor;
        if (factor.isNew) {
            delete factor.isNew;
            if (factor.parent) {
                factor.statement.index = this.ganttController.getChildCount(factor.parent);
            }
            factor.id = factor.statement.referenceId;
            this.ganttController.addFactor(factor, factor.parent);
        } else {
            this.ganttController.updateFactor(factor);
        }
        this.onCloseFactorDialog();
    },

    addFactorUriName: function(url, name){
        return this.addFactor({id:url, name : name});
    },

    addFactor: function(eventType, parent = null, modifier = null){
        const le = {};
        this.ganttController.createFactor(le);
        le.statement.referenceId = ++this.factorReferenceIdCounter;
        le.statement.eventType = eventType.id;
        le.parent = parent;
        le.text = eventType.name;
        if (le.isNew) {
            delete le.isNew;
        }
        if (!le.parent) {
            le.parent = this.ganttController.rootEventId;
        }
        if(le.parent){
            le.statement.index = this.ganttController.getChildCount(le.parent);
        }
        le.id = le.statement.referenceId;
        if(modifier)
            modifier(le);
        this.ganttController.addFactor(le, le.parent);
        le['$no_end'] = false;
        le['$no_start'] = false;
        return le;
    },

    addLink: function(source, target, factorType) {
        const link = {
            source: source,
            target: target,
            factorType: factorType
        };
        this.ganttController.addLink(link);
    },

    insertFlow: function(flow, parent){

        const nodeLevels = this.calculateNodeLevels(flow);

        const pst = parent ?
            parent.startTime :
            this.ganttController.getFactor(this.rootReferenceId).startTime;

        const map = {};
        flow.nodes.forEach(function(n) {
            const et = JsonLdUtils.jsonLdToTypeaheadOption(ObjectTypeResolver.resolveType(n, OptionsStore.getOptions(Constants.OPTIONS.EVENT_TYPE)))
            const e = this.addFactor(et, parent.referenceId, function(le){
                le.statement.startTime = pst + 1000*(2*nodeLevels[n]);
                le.statement.endTime = pst + 1000*(2*nodeLevels[n] + 1);
                le.start_date = new Date(le.statement.startTime);
                le.end_date = new Date(le.statement.endTime);
            });
            map[n] = e.id;
        }.bind(this));

        flow.edges.forEach(function(e){
            const from = map[e.from];
            const to = map[e.to];
            if(from && to)
                this.addLink(from, to, Vocabulary.EVENT_FLOW_NEXT);
        }.bind(this));

        this.onCloseFactorDialog();
    },

    calculateNodeLevels: function(flow){
        if(this._calculatingNodeLevels)
            return;
        this._calculateNodeLevels = true;
        const ret = {};
        // Find roots
        const fromNodes = new Set(flow.edges.map((n) => n.from));
        const toNodes = new Set(flow.edges.map((n) => n.to));
        const rootNodeSet = new Set([...fromNodes].filter(x => !toNodes.has(x)));
        // const rootNodes = [...rootNodeSet];
        // calculate levels
        const mystack = [];
        [...rootNodeSet].forEach(x => mystack.push({id:x, level:0, index:0}));

        while(mystack.length > 0){
            let n = mystack[mystack.length-1];
            if(ret[n.id]) {
                mystack.pop();
            }else {
                if (!n.children) {
                    n.children = [];
                    flow.edges.filter(x => x.from === n.id).forEach(x =>
                        n.children.push({
                            id: x.to,
                            level: n.level + 1,
                            index: 0
                        })
                    );
                }
                if (n.index < n.children.length){
                    mystack.push(n.children[n.index]);
                    n.index = n.index + 1;
                }else{
                    ret[n.id] = n.level;
                    mystack.pop();
                }
            }
        }
        this._calculateNodeLevels = false;
        return ret;
    },

    lossEventChanged: function(lossEventTypeOption){
        let lossEvent = this.getLossEventReferenceId();
        if(!lossEventTypeOption){
            if(lossEvent) {
                this.props.report.occurrence.lossEventType = null;
                this.ganttController.deleteFactor(this.lossEventReferenceId);
            }
        }else {
            if (!lossEvent) {
                this.props.report.occurrence.lossEventType = lossEventTypeOption.id;
                lossEvent = this.addFactor(lossEventTypeOption);
                lossEvent.statement.types = [Vocabulary.LOSS_EVENT];
            } else {
                // var lossEvent = this.ganttController.getFactor(lossEvent.statement.);
                if (lossEventTypeOption.id != lossEvent.statement.eventType) {
                    this.props.report.occurrence.lossEventType = lossEventTypeOption.id;
                    lossEvent.text = lossEventTypeOption.name;
                    lossEvent.statement.eventType = lossEventTypeOption.id;
                    this.ganttController.updateFactor(lossEvent);
                }
            }
        }
    },

    onDeleteFactor: function () {
        const factor = this.state.currentFactor;
        this.ganttController.deleteFactor(factor.id);
        this.onCloseFactorDialog();
    },

    onCloseFactorDialog: function () {
        this.setState({currentFactor: null, showFactorDialog: false});
    },

    onDeleteLink: function (link, source, target) {
        this.setState({
            showDeleteLinkDialog: true,
            currentLink: link,
            currentLinkSource: source,
            currentLinkTarget: target
        })
    },

    onCloseDeleteLinkDialog: function () {
        this.setState({showDeleteLinkDialog: false, currentLink: null});
    },

    deleteLink: function () {
        this.ganttController.deleteLink(this.state.currentLink.id);
        this.onCloseDeleteLinkDialog();
    },

    onScaleChange: function (e) {
        const scale = e.target.value;
        this.setState({scale: scale});
        this.ganttController.setScale(scale);
    },

    onGraphRootUpdate: function (startTime, endTime) {
        const attName = this.props.rootAttribute,
            root = assign({}, this.props.report[attName]),
            change = {};
        root.startTime = startTime;
        root.endTime = endTime;
        change[attName] = root;
        this.props.onChange(change);
    },

    getFactorGraph: function () {
        FactorJsonSerializer.setGanttController(this.ganttController);
        return FactorJsonSerializer.getFactorGraph(this.props.report);
    },


    render: function () {
        return <Panel header={<h5>{this.i18n('factors.panel-title')}</h5>} bsStyle='info'>
            {this.renderFactorDetailDialog()}
            {this.renderLinkTypeDialog()}
            {this.renderDeleteLinkDialog()}
            <div id='factors_gantt' className='factors-gantt'/>
            <div className='gantt-zoom'>
                <div className='col-xs-5'>
                    <div className='col-xs-2 gantt-zoom-label bold'>{this.i18n('factors.scale')}:</div>
                    {this._renderScaleOptions()}
                </div>

                <div className='col-xs-2'>&nbsp;</div>

                <div className='col-xs-5 gantt-zoom-label'>
                    {this._renderLineColors()}
                </div>
            </div>
        </Panel>;
    },

    _renderScaleOptions: function () {
        const items = [];
        Object.getOwnPropertyNames(Constants.TIME_SCALES).forEach(scaleName => {
            const scale = Constants.TIME_SCALES[scaleName];
            items.push(<div className='col-xs-2' key={scale}>
                <Input type='radio' label={this.i18n('factors.scale.' + scale)} value={scale}
                       title={this.formatMessage('factors.scale-tooltip', {unit: this.i18n('factors.scale.' + scale)})}
                       checked={this.state.scale === scale}
                       onChange={this.onScaleChange}/>
            </div>);
        });
        return items;
    },

    renderFactorDetailDialog: function () {
        if (!this.state.showFactorDialog) {
            return null;
        }
        const
            report = this._getReportForDetail(),
            isLossEvent = this.state.currentFactor && this.state.currentFactor.statement && this.state.currentFactor.statement.types &&
                          this.state.currentFactor.statement.types.includes(Vocabulary.LOSS_EVENT),
            fromEventType = this._getFactorEventType(),
            disabledTypeSelection = this.ganttController.getChildCount(this.state.currentFactor.id) > 0;

        return isLossEvent
            ?<LossEventDetail show={this.state.showFactorDialog} report={report}
                           factor={this.state.currentFactor} onClose={this.onCloseFactorDialog}
                           onSave={this.onSaveFactor} onDelete={this.onDeleteFactor} scale={this.state.scale}
                           enableDetails={this.props.enableDetails}/>

            :<FactorDetail show={this.state.showFactorDialog} report={report}
                             factor={this.state.currentFactor} fromEventType={fromEventType}
                             onClose={this.onCloseFactorDialog}
                             onSave={this.onSaveFactor} onDelete={this.onDeleteFactor} scale={this.state.scale}
                             onInsertFlow={this.insertFlow}
                             disabledTypeSelection={disabledTypeSelection}
                             enableDetails={this.props.enableDetails}/>;
    },

    _getReportForDetail: function () {
        const report = assign({}, this.props.report);
        report.factorGraph = this.getFactorGraph();
        return report;
    },

    renderLinkTypeDialog: function () {
        return (
            <Modal show={this.state.showLinkTypeDialog} bsSize='small' onHide={this.onCloseLinkTypeDialog}>
                <Modal.Header closeButton>
                    <Modal.Title>{this.i18n('factors.link-type-select')}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Select ref='linkType' title={this.i18n('factors.link-type-select-tooltip')} addDefault={true}
                            onChange={this.onLinkTypeSelect} options={this.state.factorTypeOptions}/>
                </Modal.Body>
            </Modal>
        );
    },

    renderDeleteLinkDialog: function () {
        const source = this.state.currentLinkSource ? this.state.currentLinkSource.text : '',
            target = this.state.currentLinkTarget ? this.state.currentLinkTarget.text : '';
        return <Modal show={this.state.showDeleteLinkDialog} bsSize='small' onHide={this.onCloseDeleteLinkDialog}>
            <Modal.Header closeButton>
                <Modal.Title>{this.i18n('factors.link.delete.title')}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <FormattedMessage id='factors.link.delete.text'
                                  values={{
                                      source: <span className='bold'>{source}</span>,
                                      target: <span className='bold'>{target}</span>
                                  }}/>
            </Modal.Body>
            <Modal.Footer>
                <Button bsStyle='warning' bsSize='small' onClick={this.deleteLink}>{this.i18n('delete')}</Button>
                <Button bsSize='small' onClick={this.onCloseDeleteLinkDialog}>{this.i18n('cancel')}</Button>
            </Modal.Footer>
        </Modal>;
    },

    _renderLineColors: function () {
        const size = 12 / this.state.factorTypeOptions.length;
        return this.state.factorTypeOptions.map((item) => {
            const simpleName = Utils.getLastPathFragment(item.value);
            return <div className={'col-xs-' + size} key={item.value}>
                <div className={'gantt-link-' + simpleName}
                     style={{height: '4px', width: '2em', float: 'left', margin: '8px'}}/>
                <div style={{float: 'left'}}>{item.label}</div>
            </div>;
        });
    }
});

module.exports = injectIntl(Factors, {withRef: true});
