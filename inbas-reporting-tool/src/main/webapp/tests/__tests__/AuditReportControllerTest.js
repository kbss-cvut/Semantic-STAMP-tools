'use strict';

describe('Audit report controller', () => {

    const React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,

        Actions = require('../../js/actions/Actions'),
        AuditReport = require('../../js/components/report/audit/AuditReport'),
        AuditReportController = require('../../js/components/report/audit/AuditReportController'),
        Vocabulary = require('../../js/constants/Vocabulary');

    let report;

    beforeEach(() => {
        report = Generator.generateAuditReport();
        spyOn(Actions, 'loadOptions');
        Environment.mockCurrentUser();
    });

    it('renders detail as read only when report is a SAFA report', () => {
        report.types.push(Vocabulary.SAFA_REPORT);
        report.isSafaReport = function () {
            return true;
        };
        report.readOnly = true;
        const component = Environment.render(<AuditReportController report={report}/>),
            detail = TestUtils.findRenderedComponentWithType(component, AuditReport);
        expect(detail.props.readOnly).toBeTruthy();
        expect(detail.props.readOnlyMessage).toEqual('audit.safa.readonly.message');
    });
});
