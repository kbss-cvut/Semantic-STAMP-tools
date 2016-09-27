'use strict';

describe('SafetyIssueReport', function () {

    var React = require('react'),
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Actions = require('../../js/actions/Actions'),
        Constants = require('../../js/constants/Constants'),
        messages = require('../../js/i18n/en').messages,
        SafetyIssueReport = rewire('../../js/components/report/safetyissue/SafetyIssueReport'),
        handlers,
        report;

    beforeEach(function () {
        spyOn(Actions, 'updateReport');
        spyOn(Actions, 'loadOptions');
        spyOn(Actions, 'loadOccurrenceCategories');
        spyOn(Actions, 'loadEventTypes');
        handlers = jasmine.createSpyObj('handlers', ['onCancel', 'onSuccess', 'onChange']);
        Environment.mockFactors(SafetyIssueReport);
        report = Generator.generateSafetyIssueReport();
    });

    it('Gets factor graph on submit', () => {
        var component = Environment.render(<SafetyIssueReport report={report} handlers={handlers}/>),
            saveEvent = {
                preventDefault: function () {
                }
            },
            FactorJsonSerializer = SafetyIssueReport.__get__('Factors').__get__('FactorJsonSerializer');
        component.onSave(saveEvent);
        expect(FactorJsonSerializer.getFactorGraph).toHaveBeenCalled();
    });

    it('does not display \'Create new revision\' button for new reports', () => {
        report.isNew = true;
        var component = Environment.render(<SafetyIssueReport report={report} handlers={handlers}/>);
        expect(Environment.getComponentByTagAndText(component, 'button', messages['detail.submit'])).toBeNull();
    });

    it('shows report panel title in standard font if the safety issue is open', () => {
        report.safetyIssue.state = Constants.SAFETY_ISSUE_STATE.OPEN;
        var component = Environment.render(<SafetyIssueReport report={report} handlers={handlers}/>),
            title = Environment.getComponentByTagAndText(component, 'h2', messages['safetyissuereport.title']);
        expect(title.className.indexOf('italics')).toEqual(-1);
    });

    it('shows report panel title in italics if the safety issue is closed', () => {
        report.safetyIssue.state = Constants.SAFETY_ISSUE_STATE.CLOSED;
        var component = Environment.render(<SafetyIssueReport report={report} handlers={handlers}/>),
            title = Environment.getComponentByTagAndText(component, 'h2', messages['safetyissuereport.title']);
        expect(title.className.indexOf('italics')).not.toEqual(-1);
    });

    it('does not show the activate/deactivate button for new (unsaved) safety issues', () => {
        report.isNew = true;
        var component = Environment.render(<SafetyIssueReport report={report} handlers={handlers}/>);

        var button = Environment.getComponentByTagAndText(component, 'button', messages['safety-issue.deactivate']);
        expect(button).toBeNull();
        button = Environment.getComponentByTagAndText(component, 'button', messages['safety-issue.activate']);
        expect(button).toBeNull();
    });
});