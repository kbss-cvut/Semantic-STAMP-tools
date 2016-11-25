'use strict';

describe('AuditReport', () => {

    const React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Actions = require('../../js/actions/Actions'),
        messages = require('../../js/i18n/en').messages,
        AuditReport = rewire('../../js/components/report/audit/AuditReport'),
        ReportFactory = require('../../js/model/ReportFactory');
    let handlers,
        report;

    beforeEach(function () {
        spyOn(Actions, 'updateReport');
        spyOn(Actions, 'loadOptions');
        handlers = jasmine.createSpyObj('handlers', ['onCancel', 'onSuccess', 'onChange']);
        report = ReportFactory.createAuditReport();
        report.isSafaReport = function () {
            return false;
        }
    });

    it('does not display \'Create new revision\' button for new reports', () => {
        report.isNew = true;
        const component = Environment.render(<AuditReport report={report} handlers={handlers}/>);
        expect(Environment.getComponentByTagAndText(component, 'button', messages['detail.submit'])).toBeNull();
    });

    it('renders info about audit being SAFA in the detail header', () => {
        report.isSafaReport = () => true;
        const component = Environment.render(<AuditReport report={report} handlers={handlers}/>),
            header = TestUtils.findRenderedDOMComponentWithTag(component, 'h2');
        expect(header.textContent.indexOf(messages['report.safa.label'])).not.toEqual(-1);
    });
});
