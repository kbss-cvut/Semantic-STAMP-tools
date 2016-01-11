'use strict';

describe('ReportDetail component', function () {

    var React = require('react'),
        Button = require('react-bootstrap').Button,
        Environment = require('../environment/Environment'),
        Actions = require('../../js/actions/Actions'),
        Constants = require('../../js/constants/Constants'),
        ReportDetail = require('../../js/components/preliminary/ReportDetail'),
        handlers, report;

    beforeEach(function () {
        spyOn(Actions, 'loadOccurrenceSeverityOptions');    // Prevents Ajax request to load occurrence severity options
        handlers = jasmine.createSpyObj('handlers', ['onCancel', 'onSuccess', 'onInvestigate', 'onChange']);
        report = {
            occurrence: {
                name: 'Test'
            },
            occurrenceStart: Date.now(),
            occurrenceEnd: Date.now()
        };
    });

    it('Shows all buttons for valid existing report', function () {
        var detail = Environment.render(<ReportDetail report={report} loading={false} handlers={handlers}/>),
            buttons = ['Save', 'Cancel', 'Submit to authority', 'Investigate'];
        for (var i = 0; i < buttons.length; i++) {
            var button = getButton(detail, buttons[i]);
            expect(button).not.toBeNull();
        }
    });

    it('Does not show Investigate button for new reports', function () {
        report.isNew = true;
        var detail = Environment.render(<ReportDetail report={report} loading={false} handlers={handlers}/>),
            investigateButton = getButton(detail, 'Investigate');
        expect(investigateButton).toBeNull();
    });

    function getButton(component, text) {
        return Environment.getComponentByText(component, Button, text);
    }

    it('Does not show Submit to authority button for new reports', function () {
        report.isNew = true;
        var detail = Environment.render(<ReportDetail report={report} loading={false} handlers={handlers}/>),
            investigateButton = getButton(detail, 'Submit to authority');
        expect(investigateButton).toBeNull();
    });

    it('Disables Save button for preliminary reports already being investigated', function () {
        report.occurrence.reportingPhase = Constants.INVESTIGATION_REPORT_PHASE;
        var detail = Environment.render(<ReportDetail report={report} loading={false} handlers={handlers}/>),
            investigateButton = getButton(detail, 'Save');
        expect(investigateButton.disabled).toBeTruthy();
    });

    it('Initiates new revision loading on report submit success.', function () {
        var detail = Environment.render(<ReportDetail report={report} loading={false} handlers={handlers}/>),
            key = '12345';
        spyOn(detail, 'showSuccessMessage').and.callThrough();
        detail.onSubmitSuccess(key);
        expect(handlers.onSuccess).toHaveBeenCalledWith(key);
        expect(detail.showSuccessMessage).toHaveBeenCalled();
    });

    it('Enables save button on submit success', function () {
        var detail = Environment.render(<ReportDetail report={report} loading={false} handlers={handlers}/>),
            key = '12345';
        spyOn(Actions, 'submitPreliminary');
        detail.onSubmit();
        detail.onSubmitSuccess(key);
        expect(detail.state.submitting).toBeFalsy();
    });
});