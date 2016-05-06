'use strict';

describe('ReportsController', () => {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,

        Actions = require('../../js/actions/Actions'),
        Constants = require('../../js/constants/Constants'),
        ReportsController = require('../../js/components/report/ReportsController'),
        Reports = require('../../js/components/report/Reports'),
        reports;

    beforeEach(() => {
        jasmine.addMatchers(Environment.customMatchers);
        spyOn(Actions, 'loadAllReports');
        spyOn(Actions, 'loadEventTypes');
        reports = Generator.generateReports();
    });

    xit('shows only reports of the corresponding type when type filter is triggered', () => {
        var controller = Environment.render(<ReportsController />),
            reportsComponent = TestUtils.findRenderedComponentWithType(controller, Reports),
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

    it('sorts reports descending, ascending by identification', () => {
        var controller = Environment.render(<ReportsController />),
            reportsComponent = TestUtils.findRenderedComponentWithType(controller, Reports),
            renderedReports;
        randomShuffle(reports);
        controller.onReportsLoaded({action: Actions.loadAllReports, reports: reports});
        renderedReports = reportsComponent.props.reports;
        expect(Environment.arraysEqual(reports, renderedReports)).toBeTruthy();

        controller.onSort('identification');    // Descending
        verifyOrder(reportsComponent, 'identification', 'toBeLexGreaterOrEqual', true);
        controller.onSort('identification');    // Ascending
        verifyOrder(reportsComponent, 'identification', 'toBeLexGreaterOrEqual', false);
        controller.onSort('identification');    // No sort
        renderedReports = reportsComponent.props.reports;
        expect(Environment.arraysEqual(reports, renderedReports)).toBeTruthy();
    });

    function verifyOrder(component, orderAtt, comparisonFn, not) {
        var renderedReports = component.props.reports;
        expect(renderedReports.length).toEqual(reports.length);
        for (var i = 1, len = renderedReports.length; i < len; i++) {
            if (not) {
                expect(renderedReports[i][orderAtt]).not[comparisonFn](renderedReports[i - 1][orderAtt]);
            } else {
                expect(renderedReports[i][orderAtt])[comparisonFn](renderedReports[i - 1][orderAtt]);
            }
        }
    }

    it('sorts reports descending, ascending by date', () => {
        var controller = Environment.render(<ReportsController />),
            reportsComponent = TestUtils.findRenderedComponentWithType(controller, Reports),
            renderedReports;
        randomShuffle(reports);
        controller.onReportsLoaded({action: Actions.loadAllReports, reports: reports});
        renderedReports = reportsComponent.props.reports;
        expect(Environment.arraysEqual(reports, renderedReports)).toBeTruthy();

        controller.onSort('date');      // Descending
        verifyOrder(reportsComponent, 'date', 'toBeGreaterThan', true);
        controller.onSort('date');      // Ascending
        verifyOrder(reportsComponent, 'date', 'toBeLessThan', true);
        controller.onSort('date');      // No sort
        renderedReports = reportsComponent.props.reports;
        expect(Environment.arraysEqual(reports, renderedReports)).toBeTruthy();
    });

    // TODO Add tests of combined sort

    /**
     * Knuth shuffle algorithm.
     */
    function randomShuffle(arr) {
        var currentIndex = arr.length,
            tmp, randomIndex;
        while (currentIndex !== 0) {
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex -= 1;

            tmp = arr[currentIndex];
            arr[currentIndex] = arr[randomIndex];
            arr[randomIndex] = tmp;
        }
        return arr;
    }
});
