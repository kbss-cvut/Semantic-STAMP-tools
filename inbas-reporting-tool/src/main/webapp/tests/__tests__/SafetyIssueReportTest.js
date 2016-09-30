'use strict';

describe('SafetyIssueReport', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
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

    it('removes safety issue base when row remove is clicked', () => {
        var bases = [], base;
        for (var i = 0, cnt = Generator.getRandomPositiveInt(5, 10); i < cnt; i++) {
            base = Generator.generateOccurrenceReport();
            base.uri = Generator.getRandomUri();
            bases.push(base);
        }
        report.safetyIssue.basedOn = bases;
        var component = Environment.render(<SafetyIssueReport report={report} handlers={handlers}/>),

            basedOn = TestUtils.findRenderedComponentWithType(component, require('../../js/components/report/safetyissue/BasedOn').default),
            buttons = TestUtils.scryRenderedDOMComponentsWithTag(basedOn, 'button');
        expect(buttons.length).toEqual(bases.length);
        var indexToRemove = Generator.getRandomInt(bases.length);
        TestUtils.Simulate.click(buttons[indexToRemove]);
        expect(handlers.onChange).toHaveBeenCalled();
        var change = handlers.onChange.calls.argsFor(0)[0];
        expect(change.safetyIssue.basedOn.length).toEqual(bases.length - 1);
        expect(change.safetyIssue.basedOn.indexOf(bases[indexToRemove])).toEqual(-1);
    });

    it('shows no bases info message when safety issue has no bases', () => {
        report.safetyIssue.basedOn = [];
        var component = Environment.render(<SafetyIssueReport report={report} handlers={handlers}/>),

            notice = Environment.getComponentByTagAndText(component, 'div', messages['safety-issue.base.no-bases']);
        expect(notice).not.toBeNull();
    });
});
