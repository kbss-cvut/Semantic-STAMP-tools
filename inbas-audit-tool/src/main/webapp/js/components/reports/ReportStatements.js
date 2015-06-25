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

var ReportStatementRow = require('./ReportStatementRow');
var WizardWindow = require('../wizard/WizardWindow');
var EventTypeDialog = require('./wizard/event-type/EventTypeDialog');
var RunwayIncursionSteps = require('./wizard/event-type/runway-incursion/Steps');
var CorrectiveMeasureWizardSteps = require('./wizard/corrective-measure/Steps');
var SeverityAssessmentWizardSteps = require('./wizard/severity-assessment/Steps');

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

    onEventTypeSelect(e) {
        var wizardProperties;
        switch (e.target.value) {
            case 'runway_incursion':
                wizardProperties = {
                    steps: RunwayIncursionSteps,
                    title: 'Runway Incursion Wizard',
                    onFinish: this.addEventTypeAssessment,
                    statement: {
                        eventType: 'runwayIncursion'
                    }
                };
                break;
        }
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
            component = this.renderTable(data, ['eventType'], (<th>Event Type</th>), 'eventType');
        }
        var typeWizard = (<EventTypeDialog onChange={this.onEventTypeSelect}/>);
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

    renderCorrectiveMeasures: function () {
        var data = this.props.report.correctiveMeasures;
        var component;
        if (data == null || data.length === 0) {
            component = null;
        } else {
            component = this.renderTable(data, ['description'], (<th>Description</th>), 'corrective');
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

    renderSeverityAssessments: function () {
        var data = this.props.report.severityAssessments;
        var component;
        if (data == null || data.length === 0) {
            component = null;
        } else {
            component = this.renderTable(data, ['level'], (<th>Level</th>), 'severity');
        }
        return (
            <div>
                {component}
                <Button bsStyle='primary' title='Add new Severity Assessment'
                        onClick={this.openSeverityAssessmentWizard}>
                    <Glyphicon glyph='plus'/>
                </Button>
            </div>
        );
    },

    renderTable: function (data, attributes, header, type) {
        var len = data.length;
        var rows = [];
        for (var i = 0; i < len; i++) {
            rows.push(<ReportStatementRow key={type + i} statement={data[i]} attributes={attributes}/>);
        }
        return (
            <Table striped bordered condensed hover>
                <thead>
                <tr>
                    {header}
                </tr>
                </thead>
                <tbody>
                {rows}
                </tbody>
            </Table>
        );
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
