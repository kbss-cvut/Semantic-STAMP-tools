'use strict';

describe('ReportDetailController tests', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Actions = require('../../js/actions/Actions'),
        RouterStore = require('../../js/stores/RouterStore'),
        ReportDetailController = require('../../js/components/preliminary/ReportDetailController'),
        Routes = require('../../js/utils/Routes'),
        Constants = require('../../js/constants/Constants');

    it('Uses report passed from router store if it is set', function () {
        var report = {
            initialReports: [{text: 'First Initial Report'}]
        };
        spyOn(RouterStore, 'getTransitionPayload').and.returnValue(report);

        var controller = TestUtils.renderIntoDocument(<ReportDetailController params={{}}/>),
            state = controller.getInitialState();
        expect(RouterStore.getTransitionPayload).toHaveBeenCalledWith(Routes.createReport.name);
        expect(state.loading).toBeFalsy();
        expect(state.report.initialReports).toEqual(report.initialReports);
        expect(state.report.occurrence).toBeDefined();
    });

    it('Loads existing report when report key is passed in path params', function () {
        spyOn(Actions, 'findPreliminary').and.callThrough();
        var params = {reportKey: 12345},
            controller = TestUtils.renderIntoDocument(<ReportDetailController params={params}/>),
            state = controller.getInitialState();
        expect(Actions.findPreliminary).toHaveBeenCalledWith(params.reportKey);
        expect(state.loading).toBeTruthy();
        expect(state.report).toBeNull();
    });

    it('Initializes new report when no key is specified', function () {
        var controller = TestUtils.renderIntoDocument(<ReportDetailController params={{}}/>),
            report = controller.state.report;

        expect(controller.state.loading).toBeFalsy();
        expect(report).toBeDefined();
        expect(report.isNew).toBeTruthy();
        expect(report.occurrence).toBeDefined();
        expect(report.occurrence.startTime).toBeDefined();
        expect(report.occurrence.endTime).toBeDefined();
        expect(report.occurrence.reportingPhase).toEqual(Constants.PRELIMINARY_REPORT_PHASE);
    });

    it('Initializes new report with imported initial report', function () {
        var payload = {
            initialReports: [{
                text: 'Initial report'
            }]
        };
        spyOn(RouterStore, 'getTransitionPayload').and.returnValue(payload);
        var controller = TestUtils.renderIntoDocument(<ReportDetailController params={{}}/>),
            report = controller.state.report;

        expect(report.occurrence).toBeDefined();
        expect(report.initialReports.length).toEqual(1);
        expect(report.initialReports[0]).toEqual(payload.initialReports[0]);
    });
});
