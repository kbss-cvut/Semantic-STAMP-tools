'use strict';

describe('OccurrenceReport', function () {

    var React = require('react'),
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Actions = require('../../js/actions/Actions'),
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
});