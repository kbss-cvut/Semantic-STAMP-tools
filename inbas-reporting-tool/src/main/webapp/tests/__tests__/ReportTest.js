'use strict';

describe('Report', function () {

    const React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Constants = require('../../js/constants/Constants'),
        Report = require('../../js/components/report/Report').default,
        ResourceNotFound = require('../../js/components/ResourceNotFound'),
        ReportNotRenderable = require('../../js/components/ReportNotRenderable');

    it('shows not found error when report is not found', function () {
        const rendered = Environment.render(<Report loading={false} report={null}/>),
            notFoundError = TestUtils.findRenderedComponentWithType(rendered, ResourceNotFound);
        expect(notFoundError).not.toBeNull();
    });

    it('shows not renderable error when report cannot be rendered', function () {
        const report = Generator.generateOccurrenceReport();
        report.occurrence.startTime = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
        report.occurrence.endTime = Date.now();

        const rendered = Environment.render(<Report loading={false} report={report}/>),
            notRenderableError = TestUtils.findRenderedComponentWithType(rendered, ReportNotRenderable);
        expect(notRenderableError).not.toBeNull();
    });

    it('shows not renderable error when occurrence start is much smaller than sub-event start', () => {
        Environment.mockGantt();
        const report = Generator.generateOccurrenceReport();
        report.factorGraph = {};
        report.factorGraph.nodes = Generator.generateFactorGraphNodes();
        report.factorGraph.nodes.splice(0, 0, report.occurrence);
        report.factorGraph.edges = Generator.generatePartOfLinksForNodes(report.occurrence, report.factorGraph.nodes);
        report.occurrence.startTime = report.factorGraph.nodes[1].startTime - Constants.MAX_OCCURRENCE_START_END_DIFF - 10000;
        report.occurrence.endTime = report.occurrence.startTime + 1000;

        const component = Environment.render(<Report loading={false} report={report}/>),
            notRenderableError = TestUtils.findRenderedComponentWithType(component, ReportNotRenderable);
        expect(notRenderableError).not.toBeNull();
    });
});