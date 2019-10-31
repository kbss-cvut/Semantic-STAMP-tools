'use strict';

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
            this.renderFactors(OptionsStore.getOptions(Constants.OPTIONS.EVENT_TYPE));
        }
        // this.testAddingLossEvent();
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
        FactorRenderer.renderFactors(this.props.report, eventTypes);
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

    // testAddingLossEvent(){
    //     const lossEvent = {};
    //     this.lossEventChanged(lossEvent);
    // },
    // This function is called from outside to let the factors section add/update the loss event in the factors chain
    // findLossEvent: function(){
    //     const graph = this.props.report.
    // }

    testAddFactor: function (){
        if(!this.mytmp){
            this.mytmp = 1;
        }else{
            this.mytmp = this.mytmp + 1
        }
        return this.addFactorUriName("http://example.com/factor-" + this.mytmp, "factor " + this.mytmp);
    },

    addFactorUriName: function(url, name){
        return this.addFactor({id:url, name : name});
    },

    addFactor: function(eventType, parent = null){
        const factorGraph = this.getFactorGraph();
        const le = {};
        this.ganttController.createFactor(le);
        le.statement.referenceId = ++this.factorReferenceIdCounter;
        le.statement.eventType = eventType.id;
        le.parent = parent;
        // le.statement.eventType = lossEventType.name;
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
        this.ganttController.addFactor(le, le.parent);
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

    addNetwork(){
        const e1 = this.testAddFactor();
        const e2 = this.testAddFactor();
        this.addLink(e1.id, e2.id, Vocabulary.EVENT_FLOW_NEXT);
    },

    lossEventChanged: function(lossEventTypeOption){
        const lossEvent = this.props.report.occurrence.lossEvent;
        if(!lossEvent){
            if(this.lossEvent) {
                this.ganttController.deleteFactor(this.lossEvent.id);
                this.lossEvent = null;
            }
        }else {
            const lossEventType = lossEvent.eventType;
            if (lossEventType) {
                if (!this.lossEvent) {
                    const le = this.addFactor(lossEventTypeOption)
                    this.lossEvent = le;
                    Object.assign(le.statement, lossEvent);
                } else if (lossEventType.name != this.lossEvent.text) {
                    // Object.assign(le.statement, lossEvent);
                    this.lossEvent.text = lossEventType.name;
                    Object.assign(le.statement, lossEvent);
                    // this.lossEvent.statement.eventType = this.props.lossEventType;
                    this.ganttController.updateFactor(this.lossEvent);
                }
            }
        }
        // if (!this.state.lossEvent) {
        //     factor.statement.referenceId = ++this.factorReferenceIdCounter;
        //     this.setState({lossEvent: lossEvent});
        //     //
        //     // if (lossEvent.parent) {
        //     //     lossEvent.statement.index = this.ganttController.getChildCount(lossEvent.parent);
        //     // }
        //     // lossEvent.id = lossEvent.statement.referenceId;
        //     this.ganttController.addFactor(lossEvent, lossEvent.parent);
        // } else {
        //     this.ganttController.updateFactor(lossEvent);
        // }
    },

    // initLossEvent: function(){
    //     const lossEvent = this.state.report.occurrence.lossEvent;
    //     if(!lossEvent){
    //         const lossEvent = {};
    //         this.ganttController.createFactor(lossEvent);
    //         lossEvent.statement.referenceId = ++this.factorReferenceIdCounter;
    //         lossEvent.text = lossEventType.name;
    //
    //         this.setState({lossEvent: lossEvent});
    //         this.ganttController.addFactor(lossEvent, lossEvent.parent);
    //     }
    // },

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
        // this.lossEventChanged();
        return <Panel header={<h5>{this.i18n('factors.panel-title')}</h5>} bsStyle='info'>
            <Button bsSize='small' onClick={this.testAddFactor}>add factor</Button>
            <Button bsSize='small' onClick={this.addNetwork}>add network graph</Button>
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
        const report = this._getReportForDetail();
        return <FactorDetail show={this.state.showFactorDialog} report={report}
                             factor={this.state.currentFactor} onClose={this.onCloseFactorDialog}
                             onSave={this.onSaveFactor} onDelete={this.onDeleteFactor} scale={this.state.scale}
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
