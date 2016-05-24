/**
 * @jsx
 */
'use strict';

var React = require('react');
var assign = require('object-assign');
var classNames = require('classnames');
var Modal = require('react-bootstrap').Modal;
var Button = require('react-bootstrap').Button;
var Glyphicon = require('react-bootstrap').Glyphicon;
// require().default is needed for default-exported components using the ES6 syntax
var DateTimePicker = require('kbss-react-bootstrap-datetimepicker').default;
var injectIntl = require('../../utils/injectIntl');
var FormattedMessage = require('react-intl').FormattedMessage;

var EventTypeTypeahead = require('../typeahead/EventTypeTypeahead');
var Input = require('../Input');
var Mask = require('../Mask').default;
var Utils = require('../../utils/Utils');
var FactorStyleInfo = require('../../utils/FactorStyleInfo');
var ExternalLink = require('../misc/ExternalLink').default;

var WizardGenerator = require('../wizard/generator/WizardGenerator');
var WizardWindow = require('../wizard/WizardWindow');
var I18nMixin = require('../../i18n/I18nMixin');
var EventTypeFactory = require('../../model/EventTypeFactory');
var QuestionAnswerProcessor = require('../../model/QuestionAnswerProcessor').default;


function convertDurationToCurrentUnit(factor) {
    var targetUnit = gantt.config.duration_unit;
    return Utils.convertTime(factor.durationUnit, targetUnit, factor.duration);
}

var FactorDetail = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        onSave: React.PropTypes.func.isRequired,
        onClose: React.PropTypes.func.isRequired,
        onDelete: React.PropTypes.func.isRequired,
        scale: React.PropTypes.string.isRequired,
        factor: React.PropTypes.object.isRequired,
        getReport: React.PropTypes.func.isRequired
    },

    getInitialState: function () {
        var factor = this.props.factor;
        return {
            showDeleteDialog: false,
            eventType: factor.statement ? Utils.jsonLdToTypeaheadOption(EventTypeFactory.resolveEventType(factor.statement.eventType)) : null,
            startDate: factor.start_date.getTime(),
            duration: convertDurationToCurrentUnit(factor),
            statement: factor.statement ? factor.statement : null,

            isWizardOpen: false,
            wizardProperties: null,
            showMask: false
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

    onDateChange: function (date) {
        this.setState({startDate: Number(date)});
    },

    onOpenDetails: function () {
        this.setState({showMask: true});
        WizardGenerator.generateWizard(this.props.getReport(), {'eventType': this.state.eventType.id}, this.props.factor.text, this.openDetailsWizard);
    },

    openDetailsWizard: function (wizardProperties) {
        wizardProperties.onFinish = this.onUpdateFactorDetails;
        this.setState({
            showMask: false,
            isWizardOpen: true,
            wizardProperties: wizardProperties
        });
    },

    onCloseDetails: function () {
        this.setState({isWizardOpen: false});
    },

    onUpdateFactorDetails: function (data, closeCallback) {
        var statement = assign({}, this.state.statement);
        statement.form = {
            subQuestions: []
        };
        if (data.stepData) {
            for (var i = 0, len = data.stepData.length; i < len; i++) {
                statement.form.subQuestions[i] = QuestionAnswerProcessor.processQuestionAnswerHierarchy(data.stepData[i].question);
            }
        }
        this.setState({statement: statement});
        closeCallback();
    },

    onSave: function () {
        var factor = this.props.factor;
        factor.statement = this.state.statement ? this.state.statement : {};
        factor.statement.eventType = this.state.eventType.id;
        factor.text = this.state.eventType.name;
        factor.start_date = new Date(this.state.startDate);
        factor.end_date = gantt.calculateEndDate(factor.start_date, this.state.duration, gantt.config.duration_unit);
        factor.statement.startTime = this.state.startDate;
        factor.statement.endTime = factor.end_date.getTime();
        this.props.onSave();
    },


    render: function () {
        var eventTypeLabel = this.props.factor.text,
            durationMinus = <Button bsSize='small' disabled={this.state.duration === 0}
                                    onClick={this.onDurationMinus}><Glyphicon glyph='minus'/></Button>,
            durationPlus = <Button bsSize='small' onClick={this.onDurationPlus}><Glyphicon glyph='plus'/></Button>,
            eventTypeIcon = this.renderFactorTypeIcon(),
            eventTypeClassNames = classNames({
                'col-xs-12': true,
                'col-xs-11': this.state.eventType,
                'col-xs-10': this.state.eventType && eventTypeIcon
            });

        return (
            <div>
                <WizardWindow {...this.state.wizardProperties} show={this.state.isWizardOpen}
                                                               onHide={this.onCloseDetails} enableForwardSkip={true}/>
                <Modal show={this.props.show} onHide={this.props.onClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.i18n('factors.detail.title')}</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>
                        {this._renderMask()}
                        {this.renderDeleteDialog()}
                        <div className='row'>
                            <div className='col-xs-12'>
                                <label className='control-label'>{this.i18n('factors.detail.type')}</label>
                            </div>
                        </div>
                        <div className='form-group row'>
                            {eventTypeIcon}
                            <div className={eventTypeClassNames}>
                                <EventTypeTypeahead placeholder={this.i18n('factors.detail.type-placeholder')}
                                                    value={eventTypeLabel}
                                                    onSelect={this.onEventTypeChange} focus={true}/>
                            </div>
                            {this._renderEventTypeLink()}
                        </div>
                        <div>
                            <div>
                                <label className='control-label'>{this.i18n('factors.detail.time-period')}</label>
                            </div>
                            <div className='row'>
                                <div className='col-xs-2 bold'
                                     style={{padding: '7px 0 7px 15px'}}>{this.i18n('factors.detail.start')}</div>
                                <div className='col-xs-4 picker-container form-group-sm'
                                     style={{padding: '0 15px 0 0'}}>
                                    <DateTimePicker inputFormat='DD-MM-YY HH:mm'
                                                    dateTime={this.state.startDate.toString()}
                                                    onChange={this.onDateChange}
                                                    inputProps={{title: this.i18n('occurrence.start-time-tooltip'), bsSize: 'small'}}/>
                                </div>
                                <div className='col-xs-2 bold'
                                     style={{padding: '7px 0 7px 15px'}}>{this.i18n('factors.detail.duration')}</div>
                                <div className='col-xs-4' style={{padding: '0 15px 0 0'}}>
                                    <div className='col-xs-7' style={{padding: '0'}}>
                                        <Input type='text' buttonBefore={durationMinus} buttonAfter={durationPlus}
                                               value={this.state.duration} onChange={this.onDurationSet}/>
                                    </div>
                                    <div className='col-xs-5' style={{padding: '7px 15px'}}>
                                        {this.renderDuration()}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Modal.Body>

                    <Modal.Footer>
                        <Button bsSize='small' bsStyle='success' onClick={this.onSave}
                                disabled={!this.state.eventType}>{this.i18n('save')}</Button>
                        <Button bsSize='small' onClick={this.props.onClose}>{this.i18n('cancel')}</Button>
                        {this.renderDeleteButton()}
                        {this.renderWizardButton()}
                    </Modal.Footer>
                </Modal>
            </div>
        )
    },

    _renderMask: function() {
        return this.state.showMask ? <Mask text={this.i18n('factors.detail.wizard-loading')} /> : null;
    },

    renderFactorTypeIcon: function () {
        var type = this.state.eventType,
            styleInfo;
        if (!type) {
            return null;
        }
        styleInfo = FactorStyleInfo.getStyleInfo(type.type);
        return styleInfo.icon ? <div className='col-xs-1'>
            <img src={styleInfo.icon} className={styleInfo.cls} title={styleInfo.title}/>
        </div> : null;
    },

    _renderEventTypeLink: function () {
        var et = this.state.eventType;
        return et ?
            <div className='col-xs-1'>
                <ExternalLink url={et.id} title={et.name + '\n' + et.id} className='external-link-factor-detail'/>
            </div> : null;
    },

    renderDuration: function () {
        var durations = {
            'second': <FormattedMessage id='factors.duration.second' values={{duration: this.state.duration}}/>,
            'minute': <FormattedMessage id='factors.duration.minute' values={{duration: this.state.duration}}/>,
            'hour': <FormattedMessage id='factors.duration.hour' values={{duration: this.state.duration}}/>
        };
        return durations[this.props.scale];
    },

    renderDeleteButton: function () {
        return this.props.factor.isNew ? null : (
            <Button bsSize='small' bsStyle='warning' onClick={this.onDeleteClick}>{this.i18n('delete')}</Button>);
    },

    renderWizardButton: function () {
        return (
            <div style={{float: 'left'}}>
                <Button bsStyle='primary' bsSize='small' onClick={this.onOpenDetails}
                        disabled={!this.state.eventType}>{this.i18n('factors.detail.details')}</Button>
            </div>
        )
    },

    renderDeleteDialog: function () {
        return (
            <Modal show={this.state.showDeleteDialog} onHide={this.onCancelDelete}>
                <Modal.Header>
                    <Modal.Title>{this.i18n('factors.detail.delete.title')}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {this.i18n('factors.detail.delete.text')}
                </Modal.Body>
                <Modal.Footer>
                    <Button bsSize='small' bsStyle='warning'
                            onClick={this.onDeleteFactor}>{this.i18n('delete')}</Button>
                    <Button bsSize='small' onClick={this.onCancelDelete}>{this.i18n('cancel')}</Button>
                </Modal.Footer>
            </Modal>
        );
    }
});

module.exports = injectIntl(FactorDetail);
