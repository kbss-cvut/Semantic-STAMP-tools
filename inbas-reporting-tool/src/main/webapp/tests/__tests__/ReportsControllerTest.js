'use strict';

describe('ReportsController', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,

        Actions = require('../../js/actions/Actions'),
        Constants = require('../../js/constants/Constants'),
        ReportsController = require('../../js/components/report/ReportsController'),
        Reports = require('../../js/components/report/Reports');

    beforeEach(function() {
        spyOn(Actions, 'loadAllReports');
        spyOn(Actions, 'loadEventTypes');
    });

    xit('shows only reports of the corresponding type when type filter is triggered', function () {
        var controller = Environment.render(<ReportsController />),
            reportsComponent = TestUtils.findRenderedComponentWithType(controller, Reports),
            reports = Generator.generateReports(),
            renderedReports, filter;
        controller.onReportsLoaded({action: Actions.loadAllReports, reports: reports});
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports).toEqual(reports);

        filter = {phase: null};
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports.length).toEqual(Math.ceil(reports.length / 2));
        for (var i = 0, len = renderedReports.length; i < len; i++) {
            expect(renderedReports[i].phase).toEqual(null);
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

        filter = {'occurrenceCategory': categories[Generator.getRandomInt(categories.length)].id};
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        matchingReportsCount = reports.filter(function (item) {
            return item.occurrenceCategory === filter['occurrenceCategory'];
        }).length;
        expect(renderedReports.length).toEqual(matchingReportsCount);
        for (var i = 0, len = renderedReports.length; i < len; i++) {
            expect(renderedReports[i].occurrenceCategory).toEqual(filter['occurrenceCategory']);
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
            'occurrenceCategory': sampleReport.occurrenceCategory
        };
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports.length).not.toBeLessThan(1);
        for (var i = 0, len = renderedReports.length; i < len; i++) {
            expect(renderedReports[i].phase).toEqual(filter['phase']);
            expect(renderedReports[i].occurrenceCategory).toEqual(filter['occurrenceCategory']);
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
            'occurrenceCategory': sampleReport.occurrenceCategory
        };
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports.length).toBeLessThan(reports.length);

        filter = {
            'occurrenceCategory': 'all'
        };
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports.length).toEqual(reports.length);
    });
});
