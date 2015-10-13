/**
 * @jsx
 */

'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var Panel = require('react-bootstrap').Panel;
var Input = require('../Input');
var Select = require('../Select');

var FactorDetail = require('./FactorDetail');
var GanttController = require('./GanttController');

var Factors = React.createClass({

    propTypes: {
        occurrence: React.PropTypes.object.isRequired
    },

    ganttController: null,

    getInitialState: function () {
        return {
            scale: 'minute',
            showLinkTypeDialog: false,
            currentLink: null,
            showFactorDialog: false,
            currentFactor: null
        }
    },

    componentDidUpdate: function () {
        this.ganttController.updateOccurrenceEvent(this.props.occurrence);
    },

    componentDidMount: function () {
        this.ganttController = GanttController;
        this.ganttController.init({
            onLinkAdded: this.onLinkAdded,
            onCreateFactor: this.onCreateFactor,
            onEditFactor: this.onEditFactor,
            updateOccurrence: this.onUpdateOccurrence
        });
        this.ganttController.setScale(this.state.scale);
        this.addEvents();
    },

    addEvents: function () {
        this.addOccurrenceEvent();
        var eventAssessments = this.props.occurrence.typeAssessments,
            occEventId = this.ganttController.occurrenceEventId,
            startDate = this.ganttController.getFactor(occEventId).start_date;
        if (!eventAssessments) {
            return;
        }
        for (var i = 0, len = eventAssessments.length; i < len; i++) {
            var evt = eventAssessments[i];
            this.ganttController.addFactor({
                text: evt.eventType.name,
                start_date: startDate,
                duration: 1,
                parent: occEventId,
                statement: evt
            }, occEventId);
        }
        this.ganttController.expandSubtree(occEventId);
    },

    addOccurrenceEvent: function () {
        var occurrence = this.props.occurrence,
            id = Date.now();
        this.ganttController.setOccurrenceEventId(id);
        this.ganttController.addFactor({
            id: id,
            text: occurrence.name,
            start_date: new Date(occurrence.occurrenceTime),
            duration: 1,
            readonly: true
        }, null);
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
            this.ganttController.addFactor(this.state.currentFactor);
        } else {
            this.ganttController.updateTask(factor.id);
        }
        this.onCloseFactorDialog();
    },

    onDeleteFactor: function () {
        var factor = this.state.currentFactor;
        gantt.deleteTask(factor.id);
        this.onCloseFactorDialog();
    },

    onCloseFactorDialog: function () {
        this.setState({currentFactor: null, showFactorDialog: false});
    },

    onScaleChange: function (e) {
        var scale = e.target.value;
        this.setState({scale: scale});
        this.ganttController.setScale(scale);
    },

    onUpdateOccurrence: function(startTime, endTime) {
        // End time is not supported by occurrences, yet
        this.props.onAttributeChange('occurrenceTime', startTime);
    },


    render: function () {
        return (
            <Panel header={<h5>Factors</h5>} bsStyle='info'>
                {this.renderFactorDetailDialog()}
                {this.renderLinkTypeDialog()}
                <div id='factors_gantt' className='factors-gantt'/>
                <div className='gantt-zoom'>
                    <div className='col-xs-4'>
                        <div className='col-xs-3 gantt-zoom-label bold'>Scale:</div>
                        <div className='col-xs-3'>
                            <Input type='radio' label='Seconds' value='second' title='Click to select scale in seconds'
                                   checked={this.state.scale == 'second'}
                                   onChange={this.onScaleChange}/>
                        </div>
                        <div className='col-xs-3'>
                            <Input type='radio' label='Minutes' value='minute' title='Click to select scale in minutes'
                                   checked={this.state.scale == 'minute'}
                                   onChange={this.onScaleChange}/>
                        </div>
                        <div className='col-xs-3'>
                            <Input type='radio' label='Hours' value='hour' title='Click to select scale in hours'
                                   checked={this.state.scale == 'hour'} onChange={this.onScaleChange}/>
                        </div>
                    </div>

                    <div className='col-xs-6'>&nbsp;</div>

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
                    <Select title='Select link type' onChange={this.onLinkTypeSelect} options={options}/>
                </Modal.Body>
            </Modal>
        );
    }
});

module.exports = Factors;
