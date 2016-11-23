'use strict';

describe('AuditReport', () => {

    let React = require('react'),
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Actions = require('../../js/actions/Actions'),
        messages = require('../../js/i18n/en').messages,
        AuditReport = rewire('../../js/components/report/audit/AuditReport'),
        ReportFactory = require('../../js/model/ReportFactory'),
        handlers,
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
});
