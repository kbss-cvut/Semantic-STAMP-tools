'use strict';

describe('Occurrence report controller', function () {

    const React = require('react'),
        Button = require('react-bootstrap').Button,
        rewire = require('rewire'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        ReportController = rewire('../../js/components/report/occurrence/OccurrenceReportController'),
        OccurrenceReport = rewire('../../js/components/report/occurrence/OccurrenceReport'),
        Actions = require('../../js/actions/Actions'),
        Routing = require('../../js/utils/Routing'),
        Routes = require('../../js/utils/Routes');

    beforeEach(function () {
        spyOn(Actions, 'loadOptions');
        Environment.mockCurrentUser();
        Environment.mockFactors(OccurrenceReport);
        ReportController.__set__('ReportDetail', OccurrenceReport);
    });

    it('shows only Cancel button if the displayed report is not the latest revision.', function () {
        var report = Generator.generateOccurrenceReport(),
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
            hiddenButtons = ['Save', 'Submit to authority'],
            i, len;
        spyOn(Actions, 'loadReport');
        spyOn(Actions, 'loadRevisions');
        var result = Environment.render(<ReportController report={report} revisions={revisions}/>);

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
        var report = Generator.generateOccurrenceReport(),
            newSummary = 'New investigation summary.';
        spyOn(Actions, 'loadReport');
        spyOn(Actions, 'loadRevisions');
        var result = Environment.render(<ReportController report={report}/>);

        result.onChange({summary: newSummary});
        expect(result.state.report.summary).toEqual(newSummary);
    });

    it('calls loadReport when revision is selected.', function () {
        var report = Generator.generateOccurrenceReport(),
            selectedRevision = {revision: 2, key: '111222333'};
        spyOn(Actions, 'loadReport');
        spyOn(Actions, 'loadRevisions');
        var result = Environment.render(<ReportController report={report}/>);
        spyOn(result, 'loadReport');

        result.onRevisionSelected(selectedRevision);
        expect(result.loadReport).toHaveBeenCalledWith(selectedRevision.key);
    });

    it('reloads report on save success.', function () {
        var report = Generator.generateOccurrenceReport();
        spyOn(Actions, 'loadReport');
        var result = Environment.render(<ReportController report={report}/>);

        result.onSuccess();
        expect(Actions.loadReport).toHaveBeenCalled();
    });

    // Since report removal is implemented in the ReportDetailControllerMixin, the following tests test all the report
    // detail controllers

    it('removes report chain when on remove is called', () => {
        var report = Generator.generateOccurrenceReport();
        report.fileNumber = Date.now();
        var component = Environment.render(<ReportController report={report}/>);
        spyOn(Actions, 'deleteReportChain');

        component.onRemove();
        expect(Actions.deleteReportChain).toHaveBeenCalled();
        var args = Actions.deleteReportChain.calls.argsFor(0);
        expect(args[0]).toEqual(report.fileNumber);
    });

    it('transitions to reports list when remove is successful', () => {
        var report = Generator.generateOccurrenceReport();
        report.fileNumber = Date.now();
        var component = Environment.render(<ReportController report={report}/>);
        spyOn(Actions, 'deleteReportChain').and.callFake((fileNo, onSuccess) => {
            onSuccess();
        });
        spyOn(Routing, 'transitionTo');

        component.onRemove();
        expect(Routing.transitionTo).toHaveBeenCalledWith(Routes.reports);
    });

    it('shows error message when report remove fails', () => {
        var report = Generator.generateOccurrenceReport(),
            errorMessage: 'I failed to remove the report';
        report.fileNumber = Date.now();
        var component = Environment.render(<ReportController report={report}/>);
        spyOn(Actions, 'deleteReportChain').and.callFake((fileNo, onSuccess, onError) => {
            onError({
                message: errorMessage
            });
        });

        var detailComponent = TestUtils.findRenderedComponentWithType(component, OccurrenceReport.WrappedComponent);
        detailComponent._onDelete();
        expect(Actions.deleteReportChain).toHaveBeenCalled();
        var errorMsg = Environment.getComponentByTagAndContainedText(component, 'div', errorMessage);
        expect(errorMessage).not.toBeNull();
    });
});
