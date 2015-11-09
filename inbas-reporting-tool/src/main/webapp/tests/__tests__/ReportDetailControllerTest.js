'use strict';

describe('ReportDetailController tests', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Actions = require('../../js/actions/Actions'),
        RouterStore = require('../../js/stores/RouterStore'),
        ReportDetailController = require('../../js/components/reports/ReportDetailController'),
        Routes = require('../../js/utils/Routes');

    it('Creates new empty report when none is passed from router or in path key', function () {
        var controller = TestUtils.renderIntoDocument(<ReportDetailController params={{}}/>),
            state = controller.getInitialState();
        expect(state.loading).toBeFalsy();
        expect(state.report).toBeDefined();
        expect(state.report.occurrence).toBeDefined();
    });

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
        spyOn(Actions, 'findReport').and.callThrough();
        var params = {reportKey: 12345},
            controller = TestUtils.renderIntoDocument(<ReportDetailController params={params}/>),
            state = controller.getInitialState();
        expect(Actions.findReport).toHaveBeenCalledWith(params.reportKey);
        expect(state.loading).toBeTruthy();
        expect(state.report).toBeNull();
    });
});
