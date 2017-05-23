'use strict';

describe('Occurrence report controller', function () {

    const React = require('react'),
        Button = require('react-bootstrap').Button,
        rewire = require('rewire'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Actions = require('../../js/actions/Actions'),
        Constants = require('../../js/constants/Constants'),
        Generator = require('../environment/Generator').default,
        messages = require('../../js/i18n/en').messages,
        ReportController = rewire('../../js/components/report/occurrence/OccurrenceReportController'),
        OccurrenceReport = rewire('../../js/components/report/occurrence/OccurrenceReport'),
        ReportNotRenderable = require('../../js/components/ReportNotRenderable').default;

    beforeEach(function () {
        spyOn(Actions, 'loadOptions');
        Environment.mockFactors(OccurrenceReport);
        ReportController.__set__('ReportDetail', OccurrenceReport);
    });

    it('shows only Cancel button if the displayed report is not the latest revision.', function () {
        const report = Generator.generateOccurrenceReport(),
            revisions = [
                {
                    revision: 2,
                    key: 54321
                },
                {
                    revision: report.revision,
                    key: report.key
                }
            ],
            expectedButtons = ['Cancel'],
            hiddenButtons = ['Save', 'Submit to authority'];
        let i, len;
        spyOn(Actions, 'loadReport');
        spyOn(Actions, 'loadRevisions');
        const result = Environment.render(<ReportController report={report} revisions={revisions}/>);

        for (i = 0, len = expectedButtons.length; i < len; i++) {
            expect(getButton(result, expectedButtons[i])).not.toBeNull();
        }
        for (i = 0, len = hiddenButtons.length; i < len; i++) {
            expect(getButton(result, hiddenButtons[i])).toBeNull();
        }
    });

    function getButton(root, text) {
        return Environment.getComponentByText(root, Button, text);
    }

    it('updates report state when onChange is called.', function () {
        const report = Generator.generateOccurrenceReport(),
            newSummary = 'New investigation summary.';
        spyOn(Actions, 'loadReport');
        spyOn(Actions, 'loadRevisions');
        const result = Environment.render(<ReportController report={report}/>);

        result.onChange({summary: newSummary});
        expect(result.state.report.summary).toEqual(newSummary);
    });

    it('calls loadReport when revision is selected.', function () {
        const report = Generator.generateOccurrenceReport(),
            selectedRevision = {revision: 2, key: '111222333'};
        spyOn(Actions, 'loadReport');
        spyOn(Actions, 'loadRevisions');
        const result = Environment.render(<ReportController report={report}/>);
        spyOn(result, 'loadReport');

        result.onRevisionSelected(selectedRevision);
        expect(result.loadReport).toHaveBeenCalledWith(selectedRevision.key);
    });

    it('reloads report on save success.', function () {
        const report = Generator.generateOccurrenceReport();
        spyOn(Actions, 'loadReport');
        const result = Environment.render(<ReportController report={report}/>);

        result.onSuccess();
        expect(Actions.loadReport).toHaveBeenCalled();
    });

    it('shows not renderable error when report cannot be rendered', function () {
        const report = Generator.generateOccurrenceReport();
        report.occurrence.startTime = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
        report.occurrence.endTime = Date.now();

        const rendered = Environment.render(<ReportController report={report}/>),
            notRenderableError = TestUtils.findRenderedComponentWithType(rendered, ReportNotRenderable);
        expect(notRenderableError).not.toBeNull();
    });

    it('shows not renderable error when occurrence start is much smaller than sub-event start', () => {
        const report = Generator.generateOccurrenceReport();
        report.factorGraph = {};
        report.factorGraph.nodes = Generator.generateFactorGraphNodes();
        report.factorGraph.nodes.splice(0, 0, report.occurrence);
        report.factorGraph.edges = Generator.generatePartOfLinksForNodes(report.occurrence, report.factorGraph.nodes);
        report.occurrence.startTime = report.factorGraph.nodes[1].startTime - Constants.MAX_OCCURRENCE_START_END_DIFF - 10000;
        report.occurrence.endTime = report.occurrence.startTime + 1000;

        const component = Environment.render(<ReportController report={report}/>),
            notRenderableError = TestUtils.findRenderedComponentWithType(component, ReportNotRenderable);
        expect(notRenderableError).not.toBeNull();
    });

    it('offers resolution of time-based errors causing report not to be renderable', () => {
        const report = Generator.generateOccurrenceReport();
        report.occurrence.startTime = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
        report.occurrence.endTime = Date.now();

        const rendered = Environment.render(<ReportController report={report}/>),
            fixButton = Environment.getComponentByTagAndText(rendered, 'button', messages['issue-fix']);
        expect(fixButton).not.toBeNull();
    });
});
