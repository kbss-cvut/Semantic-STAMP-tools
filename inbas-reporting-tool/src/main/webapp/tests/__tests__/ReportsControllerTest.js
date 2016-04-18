'use strict';

describe('ReportsController', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator'),

        Actions = require('../../js/actions/Actions'),
        Constants = require('../../js/constants/Constants'),
        ReportsController = require('../../js/components/report/ReportsController'),
        Reports = require('../../js/components/report/Reports');

    beforeEach(function() {
        spyOn(Actions, 'loadAllReports');
    });

    it('shows only reports of the corresponding type when type filter is triggered', function () {
        var controller = Environment.render(<ReportsController />),
            reportsComponent = TestUtils.findRenderedComponentWithType(controller, Reports),
            reports = Generator.generateReports(),
            renderedReports, filter;
        controller.onReportsLoaded({action: Actions.loadAllReports, reports: reports});
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports).toEqual(reports);

        filter = {phase: Constants.PRELIMINARY_REPORT_PHASE};
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports.length).toEqual(Math.ceil(reports.length / 2));
        for (var i = 0, len = renderedReports.length; i < len; i++) {
            expect(renderedReports[i].phase).toEqual(Constants.PRELIMINARY_REPORT_PHASE);
        }
    });

    it('shows only reports in the corresponding category when category filter is triggered', function () {
        var controller = Environment.render(<ReportsController />),
            reportsComponent = TestUtils.findRenderedComponentWithType(controller, Reports),
            reports = Generator.generateReports(),
            categories = Generator.getCategories(),
            renderedReports, filter, matchingReportsCount;
        controller.onReportsLoaded({action: Actions.loadAllReports, reports: reports});
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports).toEqual(reports);

        filter = {'occurrenceCategory.id': categories[Generator.getRandomInt(categories.length)].id};
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        matchingReportsCount = reports.filter(function (item) {
            return item.occurrenceCategory.id === filter['occurrenceCategory.id'];
        }).length;
        expect(renderedReports.length).toEqual(matchingReportsCount);
        for (var i = 0, len = renderedReports.length; i < len; i++) {
            expect(renderedReports[i].occurrenceCategory.id).toEqual(filter['occurrenceCategory.id']);
        }
    });

    it('shows only reports matching all filters', function () {
        var controller = Environment.render(<ReportsController />),
            reportsComponent = TestUtils.findRenderedComponentWithType(controller, Reports),
            reports = Generator.generateReports(),
            renderedReports, filter, sampleReport;
        controller.onReportsLoaded({action: Actions.loadAllReports, reports: reports});
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports).toEqual(reports);

        sampleReport = reports[Generator.getRandomInt(reports.length)];
        filter = {
            'phase': sampleReport.phase,
            'occurrenceCategory.id': sampleReport.occurrenceCategory.id
        };
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports.length).not.toBeLessThan(1);
        for (var i = 0, len = renderedReports.length; i < len; i++) {
            expect(renderedReports[i].phase).toEqual(filter['phase']);
            expect(renderedReports[i].occurrenceCategory.id).toEqual(filter['occurrenceCategory.id']);
        }
    });

    it('shows all reports when all filters are switched to show all', function () {
        var controller = Environment.render(<ReportsController />),
            reportsComponent = TestUtils.findRenderedComponentWithType(controller, Reports),
            reports = Generator.generateReports(),
            renderedReports, filter, sampleReport;
        controller.onReportsLoaded({action: Actions.loadAllReports, reports: reports});
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports).toEqual(reports);

        sampleReport = reports[Generator.getRandomInt(reports.length)];
        filter = {
            'phase': sampleReport.phase,
            'occurrenceCategory.id': sampleReport.occurrenceCategory.id
        };
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports.length).toBeLessThan(reports.length);

        filter = {
            'phase': 'all',
            'occurrenceCategory.id': 'all'
        };
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports.length).toEqual(reports.length);
    });
});
