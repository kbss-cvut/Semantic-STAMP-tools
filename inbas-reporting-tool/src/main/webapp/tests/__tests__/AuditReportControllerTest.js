'use strict';

describe('Audit report controller', () => {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,

        Actions = require('../../js/actions/Actions'),
        AuditReport = require('../../js/components/report/audit/AuditReport'),
        AuditReportController = require('../../js/components/report/audit/AuditReportController'),
        Vocabulary = require('../../js/constants/Vocabulary'),

        report;

    beforeEach(() => {
        report = Generator.generateAuditReport();
        spyOn(Actions, 'loadOptions');
    });

    it('adds isSafa function to audit report', () => {
        let component = Environment.render(<AuditReportController report={report}/>),
            detail = TestUtils.findRenderedComponentWithType(component, AuditReport);
        expect(detail.props.report.isSafa).toBeDefined();
    });

    it('renders detail as read only when report is a SAFA report', () => {
        report.types.push(Vocabulary.SAFA_REPORT);
        let component = Environment.render(<AuditReportController report={report}/>),
            detail = TestUtils.findRenderedComponentWithType(component, AuditReport);
        expect(detail.props.readOnly).toBeTruthy();
        expect(detail.props.readOnlyMessage).toEqual('audit.safa.readonly.message');
    });
});
