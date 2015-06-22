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
var RunwayIncursionSteps = require('./wizard/event-type/runway-incursion/Steps');
var CorrectiveMeasureWizardSteps = require('./wizard/corrective-measure/Steps');
var SeverityAssessmentWizardSteps = require('./wizard/severity-assessment/Steps');

var ReportStatements = React.createClass({
    render: function () {
        var typeAssessments = this.renderTypeAssessments();
        var correctiveMeasures = this.renderCorrectiveMeasures();
        var severityAssessments = this.renderSeverityAssessments();
        return (
            <div>
                <Panel header="Event Type Assessments">
                    {typeAssessments}
                </Panel>
                <Panel header="Corrective Measures">
                    {correctiveMeasures}
                </Panel>
                <Panel header="Event Severity Assessments">
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
        var typeWizard = (
            <WizardWindow steps={RunwayIncursionSteps} title="Event Type Assessment Wizard"
                          onFinish={this.addEventTypeAssessment} statement={{}} />
        );
        return (
            <div>
                {component}
                <ModalTrigger modal={typeWizard}>
                    <Button bsStyle="primary" title="Add new Event Type Assessment">
                        <Glyphicon glyph='plus'/>
                    </Button>
                </ModalTrigger>
            </div>
        );
    },
    addEventTypeAssessment: function (data, closeCallback) {
        var statement = data.statement;
        var eventTypes = this.props.report.typeAssessments != null ? this.props.report.typeAssessments : [];
        eventTypes.push(statement);
        this.props.onUpdateReport({typeAssessments: eventTypes});
        closeCallback();
    },

    renderCorrectiveMeasures: function () {
        var data = this.props.report.correctiveMeasures;
        var component;
        if (data == null || data.length === 0) {
            component = null;
        } else {
            component = this.renderTable(data, ['description'], (<th>Description</th>), "corrective");
        }
        var typeWizard = (
            <WizardWindow steps={CorrectiveMeasureWizardSteps} title="Corrective Measure Wizard"
                          onFinish={this.addCorrectiveMeasure}/>
        );
        return (
            <div>
                {component}
                <ModalTrigger modal={typeWizard}>
                    <Button bsStyle="primary" title="Add new Corrective Measure">
                        <Glyphicon glyph='plus'/>
                    </Button>
                </ModalTrigger>
            </div>
        );
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

    renderSeverityAssessments: function () {
        var data = this.props.report.severityAssessments;
        var component;
        if (data == null || data.length === 0) {
            component = null;
        } else {
            component = this.renderTable(data, ['level'], (<th>Level</th>), 'severity');
        }
        var typeWizard = (
            <WizardWindow steps={SeverityAssessmentWizardSteps} title="Severity Assessment Wizard"
                          onFinish={this.addSeverityAssessment}/>
        );
        return (
            <div>
                {component}
                <ModalTrigger modal={typeWizard}>
                    <Button bsStyle="primary" title="Add new Severity Assessment">
                        <Glyphicon glyph='plus'/>
                    </Button>
                </ModalTrigger>
            </div>
        );
    },
    addSeverityAssessment: function (statement, closeCallback) {
        // TODO
        closeCallback();
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
    }
});

module.exports = ReportStatements;
