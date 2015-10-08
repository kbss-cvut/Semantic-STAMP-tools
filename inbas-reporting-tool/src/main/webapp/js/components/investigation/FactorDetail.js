/**
 * @jsx
 */
'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var Button = require('react-bootstrap').Button;
var Glyphicon = require('react-bootstrap').Glyphicon;
var DateTimePicker = require('react-bootstrap-datetimepicker');

var Input = require('../Input');
var EventTypeTypeahead = require('../typeahead/EventTypeTypeahead');
var Utils = require('../../utils/Utils');

var scaleUnits = {
    'second': 'Seconds',
    'minute': 'Minutes',
    'hour': 'Hours'
};

function convertDurationToCurrentUnit(factor) {
    var targetUnit = gantt.config.duration_unit;
    return Utils.convertTime(factor.durationUnit, targetUnit, factor.duration);
}

var FactorDetail = React.createClass({
    propTypes: {
        onSave: React.PropTypes.func.isRequired,
        onClose: React.PropTypes.func.isRequired,
        onDelete: React.PropTypes.func.isRequired,
        scale: React.PropTypes.string.isRequired,
        factor: React.PropTypes.object.isRequired
    },

    getInitialState: function () {
        return {
            showDeleteDialog: false,
            eventType: this.props.factor.eventType,
            duration: convertDurationToCurrentUnit(this.props.factor)
        };
    },

    onDeleteClick: function () {
        this.setState({showDeleteDialog: true});
    },

    onDeleteFactor: function () {
        this.setState({showDeleteDialog: false});
        this.props.onDelete();
    },

    onCancelDelete: function () {
        this.setState({showDeleteDialog: false});
    },

    onDurationMinus: function () {
        this.setState({duration: this.state.duration - 1});
    },

    onDurationPlus: function () {
        this.setState({duration: this.state.duration + 1});
    },

    onDurationSet: function (e) {
        var duration = Number(e.target.value);
        if (isNaN(duration) || duration < 0) {
            return;
        }
        this.setState({duration: duration});
    },

    onEventTypeChange: function (option) {
        this.setState({eventType: option});
    },

    onSave: function () {
        var factor = this.props.factor;
        factor.eventType = this.state.eventType;
        factor.text = this.state.eventType.name;
        factor.end_date = gantt.calculateEndDate(factor.start_date, this.state.duration, gantt.config.duration_unit);
        this.props.onSave();
    },


    render: function () {
        var eventTypeLabel = this.props.factor.text,
            durationMinus = <Button bsSize='small' disabled={this.state.duration === 0}
                                    onClick={this.onDurationMinus}><Glyphicon glyph='minus'/></Button>,
            durationPlus = <Button bsSize='small' onClick={this.onDurationPlus}><Glyphicon glyph='plus'/></Button>;

        return (
            <Modal show={this.props.show} onHide={this.props.onClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Factor Detail</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    {this.renderDeleteDialog()}
                    <div className='form-group'>
                        <EventTypeTypeahead label='Event Type' value={eventTypeLabel}
                                            onSelect={this.onEventTypeChange} focus={true}/>
                    </div>
                    <div>
                        <div>
                            <label className='control-label'>Time period</label>
                        </div>
                        <div className='row'>
                            <div className='col-xs-2 bold' style={{padding: '7px 0 7px 15px'}}>Start time</div>
                            <div className='col-xs-4 picker-container form-group-sm' style={{padding: '0 15px 0 0'}}>
                                <DateTimePicker inputFormat='DD-MM-YY HH:mm'
                                                dateTime={this.props.factor.start_date.getTime().toString()}
                                                onChange={this.onDateChange}
                                                inputProps={{title: 'Date and time when the event occurred', bsSize: 'small'}}/>
                            </div>
                            <div className='col-xs-2 bold' style={{padding: '7px 0 7px 15px'}}>Duration</div>
                            <div className='col-xs-4' style={{padding: '0 15px 0 0'}}>
                                <div className='col-xs-7' style={{padding: '0'}}>
                                    <Input type='text' buttonBefore={durationMinus} buttonAfter={durationPlus}
                                           value={this.state.duration} onChange={this.onDurationSet}/>
                                </div>
                                <div className='col-xs-5' style={{padding: '7px 15px'}}>
                                    {scaleUnits[this.props.scale]}
                                </div>
                            </div>
                        </div>
                    </div>
                </Modal.Body>

                <Modal.Footer>
                    <Button bsSize='small' bsStyle='success' onClick={this.onSave}
                            disabled={!this.state.eventType}>Save</Button>
                    <Button bsSize='small' onClick={this.props.onClose}>Cancel</Button>
                    {this.renderDeleteButton()}
                </Modal.Footer>
            </Modal>
        )
    },

    renderDeleteButton: function () {
        return this.props.factor.isNew ? null : (
            <Button bsSize='small' bsStyle='warning' onClick={this.onDeleteClick}>Delete</Button>);
    },

    renderDeleteDialog: function () {
        return (
            <Modal show={this.state.showDeleteDialog} onHide={this.onCancelDelete}>
                <Modal.Header>
                    <Modal.Title>Delete factor?</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to remove this factor?
                </Modal.Body>
                <Modal.Footer>
                    <Button bsSize='small' bsStyle='warning' onClick={this.onDeleteFactor}>Delete</Button>
                    <Button bsSize='small' onClick={this.onCancelDelete}>Cancel</Button>
                </Modal.Footer>
            </Modal>
        );
    }
});

module.exports = FactorDetail;
