/**
 * Created by ledvima1 on 17.6.15.
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Table = require('react-bootstrap').Table;
var Panel = require('react-bootstrap').Panel;
var Glyphicon = require('react-bootstrap').Glyphicon;
var OverlayMixin = require('react-bootstrap').OverlayMixin;
var ModalTrigger = require('react-bootstrap').ModalTrigger;

var ReportStatementsTable = require('./ReportStatementsTable');
var WizardWindow = require('../wizard/WizardWindow');
var EventTypeDialog = require('./wizard/event-type/EventTypeDialog');
var RunwayIncursionSteps = require('./wizard/event-type/runway-incursion/Steps');
var CorrectiveMeasureWizardSteps = require('./wizard/corrective-measure/Steps');
var SeverityAssessmentWizardSteps = require('./wizard/severity-assessment/Steps');
var EventTypeWizardSelector = require('./wizard/event-type/EventTypeWizardSelector');
var EventTypeFactory = require('../../model/EventTypeFactory');

var ReportStatements = React.createClass({
    mixins: [OverlayMixin],
    getInitialState: function () {
        return {
            isWizardOpen: false,
            wizardProperties: null
        }
    },

    openWizard: function (wizardProperties) {
        this.setState({
            isWizardOpen: true,
            wizardProperties: wizardProperties
        });
    },

    closeWizard: function () {
        this.setState({isWizardOpen: false});
    },

    onEventTypeSelect: function (eventType) {
        // TODO This is temporary, the wizard type should be decidable from the event type
        if (eventType.name.toLowerCase().indexOf('incursion') !== -1) {
            eventType.wizard = 'runway_incursion';
        }
        var wizardProperties = EventTypeWizardSelector.getWizardSettings(eventType);
        wizardProperties.onFinish = this.addEventTypeAssessment;
        this.openWizard(wizardProperties);
    },
    addEventTypeAssessment: function (data, closeCallback) {
        var statement = data.statement;
        var eventTypes = this.props.report.typeAssessments != null ? this.props.report.typeAssessments : [];
        eventTypes.push(statement);
        this.props.onUpdateReport({typeAssessments: eventTypes});
        closeCallback();
    },

    openCorrectiveMeasureWizard: function () {
        var properties = {
            steps: CorrectiveMeasureWizardSteps,
            title: 'Corrective Measure Wizard',
            onFinish: this.addCorrectiveMeasure
        };
        this.openWizard(properties);
    },
    addCorrectiveMeasure: function (data, closeCallback) {
        var measure = {
            description: data.description
        };
        var measures = this.props.report.correctiveMeasures != null ? this.props.report.correctiveMeasures : [];
        measures.push(measure);
        this.props.onUpdateReport({correctiveMeasures: measures});
        closeCallback();
    },

    openSeverityAssessmentWizard: function () {
        var properties = {
            steps: SeverityAssessmentWizardSteps,
            title: 'Severity Assessment Wizard',
            onFinish: this.addSeverityAssessment
        };
        this.openWizard(properties);
    },
    addSeverityAssessment: function (statement, closeCallback) {
        // TODO
        closeCallback();
    },

    render: function () {
        var typeAssessments = this.renderTypeAssessments();
        var correctiveMeasures = this.renderCorrectiveMeasures();
        var severityAssessments = this.renderSeverityAssessments();
        return (
            <div>
                <Panel header='Event Type Assessments'>
                    {typeAssessments}
                </Panel>
                <Panel header='Corrective Measures'>
                    {correctiveMeasures}
                </Panel>
                <Panel header='Event Severity Assessments'>
                    {severityAssessments}
                </Panel>
            </div>
        );
    },

    renderTypeAssessments: function () {
        var data = this.props.report.typeAssessments;
        var component;
        if (data == null || data.length === 0) {
            component = null;
        } else {
            var toShow = [];
            for (var i = 0; i < data.length; i++) {
                toShow.push({
                    eventType: data[i].eventType.name,
                    summary: EventTypeFactory.create(data[i]).toString()
                });
            }
            var header = [{
                attribute: 'eventType',
                name: 'Event Type',
                flex: 3
            }, {
                attribute: 'summary',
                name: 'Summary',
                flex: 8
            }];
            component = (<ReportStatementsTable data={toShow} header={header} keyBase='eventType'
                                                handlers={{onRemove: this.onRemoveEventTypeAssessment}}/>);
        }
        var typeWizard = (<EventTypeDialog onTypeSelect={this.onEventTypeSelect}/>);
        return (
            <div>
                {component}
                <ModalTrigger modal={typeWizard}>
                    <Button bsStyle='primary' title='Add new Event Type Assessment'>
                        <Glyphicon glyph='plus'/>
                    </Button>
                </ModalTrigger>
            </div>
        );
    },
    onRemoveEventTypeAssessment: function (index) {
        var types = this.props.report.typeAssessments;
        types.splice(index, 1);
        this.props.onUpdateReport({typeAssessments: types});
    },

    renderCorrectiveMeasures: function () {
        var data = this.props.report.correctiveMeasures;
        var component;
        if (data == null || data.length === 0) {
            component = null;
        } else {
            var header = [{
                flex: 11,
                attribute: 'description',
                name: 'Description'
            }];
            component = (<ReportStatementsTable data={data} header={header} keyBase='corrective'
                                                handlers={{onRemove: this.onRemoveCorrectiveMeasure}}/>);
        }
        return (
            <div>
                {component}
                <Button bsStyle='primary' title='Add new Corrective Measure' onClick={this.openCorrectiveMeasureWizard}>
                    <Glyphicon glyph='plus'/>
                </Button>
            </div>
        );
    },

    onRemoveCorrectiveMeasure: function (index) {
        var measures = this.props.report.correctiveMeasures;
        measures.splice(index, 1);
        this.props.onUpdateReport({correctiveMeasures: measures});
    },

    renderSeverityAssessments: function () {
        var data = this.props.report.severityAssessments;
        var component;
        if (data == null || data.length === 0) {
            component = null;
        } else {
            var header = [{
                flex: 11,
                attribute: 'level',
                name: 'Severity Level'
            }];
            component = (<ReportStatementsTable data={data} header={header} keyBase='severity'
                                                handlers={{onRemove: this.onRemoveSeverityAssessment}}/>);
        }
        return (
            <div>
                {component}
                <Button bsStyle='primary' title='Add new Severity Assessment'
                        onClick={this.openSeverityAssessmentWizard} disabled>
                    <Glyphicon glyph='plus'/>
                </Button>
            </div>
        );
    },

    onRemoveSeverityAssessment: function (index) {
        var assessments = this.props.report.severityAssessments;
        assessments.splice(index, 1);
        this.props.onUpdateReport({severityAssessments: assessments});
    },

    renderOverlay: function () {
        if (!this.state.isWizardOpen) {
            return null;
        }
        return (
            <WizardWindow {...this.state.wizardProperties} onRequestHide={this.closeWizard}/>
        );
    }
});

module.exports = ReportStatements;
