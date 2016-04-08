'use strict';

describe('Report', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator'),
        Constants = require('../../js/constants/Constants'),
        Report = require('../../js/components/reports/Report'),
        ResourceNotFound = require('../../js/components/ResourceNotFound'),
        ReportNotRenderable = require('../../js/components/ReportNotRenderable');

    it('shows not found error when report is not found', function () {
        var rendered = Environment.render(<Report loading={false} report={null}/>),
            notFoundError = TestUtils.findRenderedComponentWithType(rendered, ResourceNotFound.wrappedComponent);
        expect(notFoundError).not.toBeNull();
    });

    it('shows not renderable error when report cannot be rendered', function () {
        var report = Generator.generatePreliminaryReport();
        report.occurrenceStart = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
        report.occurrenceEnd = Date.now();

        var rendered = Environment.render(<Report loading={false} report={report}/>),
            notRenderableError = TestUtils.findRenderedComponentWithType(rendered, ReportNotRenderable.wrappedComponent);
        expect(notRenderableError).not.toBeNull();
    })
});