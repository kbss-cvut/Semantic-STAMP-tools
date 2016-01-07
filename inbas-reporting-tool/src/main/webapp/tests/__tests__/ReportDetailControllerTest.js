'use strict';

describe('ReportDetailController tests', function () {

    var React = require('react'),
        Button = require('react-bootstrap').Button,
        Environment = require('../environment/Environment'),
        Actions = require('../../js/actions/Actions'),
        RouterStore = require('../../js/stores/RouterStore'),
        ReportDetailController = require('../../js/components/preliminary/ReportDetailController'),
        Routes = require('../../js/utils/Routes'),
        Constants = require('../../js/constants/Constants');

    beforeEach(function () {
        spyOn(Actions, 'loadOccurrenceSeverityOptions');
    });

    it('Uses report passed from router store if it is set', function () {
        var report = {
            initialReports: [{text: 'First Initial Report'}],
            occurrenceStart: Date.now() - 10000,
            occurrenceEnd: Date.now()
        };
        spyOn(RouterStore, 'getTransitionPayload').and.returnValue(report);

        var controller = Environment.render(<ReportDetailController params={{}}/>),
            state = controller.getInitialState();
        expect(RouterStore.getTransitionPayload).toHaveBeenCalledWith(Routes.createReport.name);
        expect(state.loading).toBeFalsy();
        expect(state.report.initialReports).toEqual(report.initialReports);
        expect(state.report.occurrence).toBeDefined();
    });

    it('Loads existing report when report key is passed in path params', function () {
        spyOn(Actions, 'findPreliminary');
        var params = {reportKey: 12345},
            controller = Environment.render(<ReportDetailController params={params}/>),
            state = controller.getInitialState();
        expect(Actions.findPreliminary).toHaveBeenCalledWith(params.reportKey);
        expect(state.loading).toBeTruthy();
        expect(state.report).toBeNull();
    });

    it('Initializes new report when no key is specified', function () {
        var controller = Environment.render(<ReportDetailController params={{}}/>),
            report = controller.state.report;

        expect(controller.state.loading).toBeFalsy();
        expect(report).toBeDefined();
        expect(report.occurrenceStart).toBeDefined();
        expect(report.occurrenceEnd).toBeDefined();
        expect(report.isNew).toBeTruthy();
        expect(report.occurrence).toBeDefined();
        expect(report.occurrence.reportingPhase).toEqual(Constants.PRELIMINARY_REPORT_PHASE);
    });

    it('Initializes new report with imported initial report', function () {
        var payload = {
            initialReports: [{
                text: 'Initial report'
            }]
        };
        spyOn(RouterStore, 'getTransitionPayload').and.returnValue(payload);
        var controller = Environment.render(<ReportDetailController params={{}}/>),
            report = controller.state.report;

        expect(report.occurrence).toBeDefined();
        expect(report.initialReports.length).toEqual(1);
        expect(report.initialReports[0]).toEqual(payload.initialReports[0]);
    });

    it('Show only cancel button for older revisions of a report', function () {
        var report = {
            key: '123455',
            occurrence: {key: '554321'},
            initialReports: [{text: 'First Initial Report'}],
            occurrenceStart: Date.now() - 10000,
            occurrenceEnd: Date.now(),
            revision: 1
        }, revisions = [
            {revision: 2, key: '123456'},
            {revision: 1, key: '123455'}
        ];
        spyOn(Actions, 'findPreliminary');
        spyOn(Actions, 'loadPreliminaryRevisions');
        var controller = Environment.render(<ReportDetailController params={{reportKey: '123455'}}/>),
            expectedButtons = ['Cancel'],
            hiddenButtons = ['Save', 'Submit to authority', 'Investigate'],
            i;
        controller.onReportStoreTrigger({action: Actions.findPreliminary, report: report});
        controller.onReportStoreTrigger({action: Actions.loadPreliminaryRevisions, revisions: revisions});

        for (i = 0; i < expectedButtons.length; i++) {
            expect(getButton(controller, expectedButtons[i])).not.toBeNull();
        }
        for (i = 0; i < hiddenButtons.length; i++) {
            expect(getButton(controller, hiddenButtons[i])).toBeNull();
        }
    });

    function getButton(root, text) {
        return Environment.getComponentByText(root, Button, text);
    }
});
