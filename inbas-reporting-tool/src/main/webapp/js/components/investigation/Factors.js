/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Modal = require('react-bootstrap').Modal;
var Panel = require('react-bootstrap').Panel;
var Input = require('../Input');
var Select = require('../Select');

var FactorDetail = require('./FactorDetail');
var FactorRenderer = require('./FactorRenderer');
var GanttController = require('./GanttController');
var FactorJsonSerializer = require('../../utils/FactorJsonSerializer');

var Factors = React.createClass({

    propTypes: {
        investigation: React.PropTypes.object.isRequired
    },

    ganttController: null,

    getInitialState: function () {
        return {
            scale: 'minute',
            showLinkTypeDialog: false,
            currentLink: null,
            currentLinkSource: null,
            currentLinkTarget: null,
            showFactorDialog: false,
            currentFactor: null,
            showDeleteLinkDialog: false
        }
    },

    componentDidUpdate: function () {
        this.ganttController.updateOccurrenceEvent(this.props.investigation.occurrence);
    },

    componentDidMount: function () {
        this.ganttController = GanttController;
        this.ganttController.init({
            onLinkAdded: this.onLinkAdded,
            onCreateFactor: this.onCreateFactor,
            onEditFactor: this.onEditFactor,
            updateOccurrence: this.onUpdateOccurrence,
            onDeleteLink: this.onDeleteLink
        });
        this.ganttController.setScale(this.state.scale);
        this.addEvents();
    },

    addEvents: function () {
        FactorRenderer.renderFactors(this.props.investigation);
        this.ganttController.expandSubtree(this.ganttController.occurrenceEventId);
    },

    onLinkAdded: function (link) {
        this.setState({currentLink: link, showLinkTypeDialog: true});
    },

    onLinkTypeSelect: function (e) {
        var link = this.state.currentLink;
        link.factorType = e.target.value;
        this.ganttController.addLink(link);
        this.onCloseLinkTypeDialog();
    },

    onCloseLinkTypeDialog: function () {
        this.setState({currentLink: null, showLinkTypeDialog: false});
    },

    onCreateFactor: function (factor) {
        this.setState({showFactorDialog: true, currentFactor: factor});
    },

    onEditFactor: function (factor) {
        this.setState({currentFactor: factor, showFactorDialog: true});
    },

    onSaveFactor: function () {
        var factor = this.state.currentFactor;
        if (factor.isNew) {
            delete factor.isNew;
            this.addChildFactorToParent(factor);
            this.ganttController.addFactor(factor);
        } else {
            this.ganttController.updateFactor(factor);
        }
        this.onCloseFactorDialog();
    },

    addChildFactorToParent: function(child) {
        var parent = this.ganttController.getFactor(child.parent);
        if (!parent.statement.children) {
            parent.statement.children = [];
        }
        parent.statement.children.push(child.statement);
    },

    onDeleteFactor: function () {
        var factor = this.state.currentFactor,
            parentId = factor.parent;
        this.ganttController.deleteFactor(factor.id);
        this.removeChildFactorFromParent(factor, parentId);
        this.onCloseFactorDialog();
    },

    removeChildFactorFromParent: function(child, parentId) {
        var parent = this.ganttController.getFactor(parentId),
            parentFactor = parent.statement,
            factor = child.statement;
        var childIndex = parentFactor.children.indexOf(factor);
        parentFactor.children.splice(childIndex, 1);
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
        var scale = e.target.value;
        this.setState({scale: scale});
        this.ganttController.setScale(scale);
    },

    onUpdateOccurrence: function (startTime, endTime) {
        var occurrence = this.props.investigation.occurrence;
        occurrence.startTime = startTime;
        occurrence.endTime = endTime;
        this.props.onAttributeChange('occurrence', occurrence);
    },

    getFactorHierarchy: function() {
        FactorJsonSerializer.setGanttController(this.ganttController);
        return FactorJsonSerializer.getFactorHierarchy();
    },


    render: function () {
        return (
            <Panel header={<h5>Factors</h5>} bsStyle='info'>
                {this.renderFactorDetailDialog()}
                {this.renderLinkTypeDialog()}
                {this.renderDeleteLinkDialog()}
                <div id='factors_gantt' className='factors-gantt'/>
                <div className='gantt-zoom'>
                    <div className='col-xs-5'>
                        <div className='col-xs-2 gantt-zoom-label bold'>Scale:</div>
                        <div className='col-xs-2'>
                            <Input type='radio' label='Seconds' value='second' title='Click to select scale in seconds'
                                   checked={this.state.scale === 'second'}
                                   onChange={this.onScaleChange}/>
                        </div>
                        <div className='col-xs-2'>
                            <Input type='radio' label='Minutes' value='minute' title='Click to select scale in minutes'
                                   checked={this.state.scale === 'minute'}
                                   onChange={this.onScaleChange}/>
                        </div>
                        <div className='col-xs-2'>
                            <Input type='radio' label='Hours' value='hour' title='Click to select scale in hours'
                                   checked={this.state.scale === 'hour'} onChange={this.onScaleChange}/>
                        </div>
                        <div className='col-xs-2'>
                            <Input type='radio' label='Relative' value='relative' title='Click to select relative scale'
                                   checked={this.state.scale === 'relative'} onChange={this.onScaleChange}/>
                        </div>
                    </div>

                    <div className='col-xs-5'>&nbsp;</div>

                    <div className='col-xs-2 gantt-zoom-label'>
                        <div className='col-xs-6' style={{verticalAlign: 'middle'}}>
                            <div className='gantt-link-causes'
                                 style={{height: '4px', width: '2em', float: 'left', margin: '8px'}}/>
                            <div style={{float: 'left'}}>Causes</div>
                        </div>
                        <div className='col-xs-6'>
                            <div className='gantt-link-mitigates'
                                 style={{height: '4px', width: '2em', float: 'left', margin: '8px'}}/>
                            <div style={{float: 'left'}}>Mitigates</div>
                        </div>
                    </div>
                </div>
            </Panel>);
    },

    renderFactorDetailDialog: function () {
        if (!this.state.showFactorDialog) {
            return null;
        }
        return (<FactorDetail show={this.state.showFactorDialog} factor={this.state.currentFactor}
                              onClose={this.onCloseFactorDialog} onSave={this.onSaveFactor}
                              onDelete={this.onDeleteFactor} scale={this.state.scale}/>);
    },

    renderLinkTypeDialog: function () {
        var options = [
            {value: 'cause', label: 'Causes'},
            {value: 'mitigate', label: 'Mitigates'}
        ];
        return (
            <Modal show={this.state.showLinkTypeDialog} bsSize='small' onHide={this.onCloseLinkTypeDialog}>
                <Modal.Header closeButton>
                    <Modal.Title>Connection type?</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Select ref='linkType' title='Select link type' onChange={this.onLinkTypeSelect} options={options}/>
                </Modal.Body>
            </Modal>
        );
    },

    renderDeleteLinkDialog: function () {
        var source = this.state.currentLinkSource ? this.state.currentLinkSource.text : '',
            target = this.state.currentLinkTarget ? this.state.currentLinkTarget.text : '';
        return (
            <Modal show={this.state.showDeleteLinkDialog} bsSize='small' onHide={this.onCloseDeleteLinkDialog}>
                <Modal.Header closeButton>
                    <Modal.Title>Delete link?</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to delete the link from&nbsp;
                    <span className='bold'>{source}</span>
                    &nbsp;event to&nbsp;
                    <span className='bold'>{target}</span>
                    &nbsp;event?
                </Modal.Body>
                <Modal.Footer>
                    <Button bsStyle='warning' bsSize='small' onClick={this.deleteLink}>Delete</Button>
                    <Button bsSize='small' onClick={this.onCloseDeleteLinkDialog}>Cancel</Button>
                </Modal.Footer>
            </Modal>
        );
    }
});

module.exports = Factors;
