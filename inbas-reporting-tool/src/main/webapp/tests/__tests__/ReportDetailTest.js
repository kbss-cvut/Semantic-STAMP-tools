'use strict';

describe('ReportDetail component', function () {

    var React = require('react'),
        ReactDOM = require('react-dom'),
        TestUtils = require('react-addons-test-utils'),
        Button = require('react-bootstrap').Button,
        Environment = require('../environment/Environment'),
        Actions = require('../../js/actions/Actions'),
        Constants = require('../../js/constants/Constants'),
        ReportDetail = require('../../js/components/preliminary/ReportDetail'),
        handlers, report;

    beforeEach(function () {
        handlers = jasmine.createSpyObj('handlers', ['onCancel', 'onSuccess', 'onInvestigate', 'onChange']);
        report = {
            occurrence: {
                name: 'Test'
            },
            occurrenceStart: Date.now(),
            occurrenceEnd: Date.now()
        };
    });

    it('Disables investigation for new reports', function () {
        report.isNew = true;
        var detail = Environment.render(<ReportDetail report={report} loading={false} handlers={handlers}/>),
            investigateButton = getButton(detail, 'Investigate');
        expect(investigateButton.disabled).toBeTruthy();
    });

    function getButton(component, text) {
        var buttons = TestUtils.scryRenderedComponentsWithType(component, Button);
        for (var i = 0, len = buttons.length; i < len; i++) {
            var node = ReactDOM.findDOMNode(buttons[i]);
            if (node.textContent === text) {
                return node;
            }
        }
        throw 'Investigate button not found.';
    }

    it('Disables Save button for preliminary reports already being investigated', function () {
        report.occurrence.reportingPhase = Constants.INVESTIGATION_REPORT_PHASE;
        var detail = Environment.render(<ReportDetail report={report} loading={false} handlers={handlers}/>),
            investigateButton = getButton(detail, 'Save');
        expect(investigateButton.disabled).toBeTruthy();
    });
});