'use strict';

describe('ReportsController', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator'),

        Constants = require('../../js/constants/Constants'),
        ReportsController = require('../../js/components/reports/ReportsController'),
        Reports = require('../../js/components/reports/Reports');

    it('shows only reports of the corresponding type when type filter is triggered.', function () {
        var controller = Environment.render(<ReportsController />),
            reportsComponent = TestUtils.findRenderedComponentWithType(controller, Reports),
            reports = Generator.generateReports(),
            renderedReports, filter;
        controller.onReportsLoaded(reports);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports).toEqual(reports);

        filter = {phase: 'preliminary'};
        controller.onFilterChange(filter);
        renderedReports = reportsComponent.props.reports;
        expect(renderedReports.length).toEqual(Math.ceil(reports.length / 2));
        for (var i = 0, len = renderedReports.length; i < len; i++) {
            expect(renderedReports[i].phase).toEqual(Constants.PRELIMINARY_REPORT_PHASE);
        }
    });
});
