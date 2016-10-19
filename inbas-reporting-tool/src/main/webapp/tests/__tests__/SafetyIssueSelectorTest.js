'use strict';

describe('Safety issue selector', () => {

    var React = require('react'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Actions = require('../../js/actions/Actions'),
        ReportStore = require('../../js/stores/ReportStore'),
        SafetyIssueSelector = require('../../js/components/report/safetyissue/SafetyIssueSelector').default,
        Vocabulary = require('../../js/constants/Vocabulary'),
        report;

    beforeEach(() => {
        spyOn(Actions, 'loadAllReports');
        report = Generator.generateOccurrenceReport();
    });

    it('uses only safety issues as options for the typeahead', () => {
        var component = Environment.render(<SafetyIssueSelector event={report.occurrence}
                                                                report={report}/>).getWrappedComponent(),
            reports = Generator.generateReports(),
            expectedOptions = getSafetyIssueReports(reports),
            options;

        component._onReportsLoaded({action: Actions.loadAllReports, reports: reports});
        options = component.state.reports;
        expect(options).toEqual(expectedOptions);
    });

    function getSafetyIssueReports(reports) {
        return reports.filter((item) => {
            return item.types.indexOf(Vocabulary.SAFETY_ISSUE_REPORT) !== -1;
        });
    }
});
