'use strict';

jest.dontMock('../../js/components/reports/ReportDetailController');
jest.dontMock('../../js/actions/Actions');

describe('ReportDetailController tests', function () {

    var React = require('react/addons'),
        TestUtils = React.addons.TestUtils,
        Actions = require('../../js/actions/Actions'),
        RouterStore = require('../../js/stores/RouterStore'),
        ReportDetailController = require('../../js/components/reports/ReportDetailController');

    it('Creates new empty report when none is passed from router or in path key', function () {
        var controller = TestUtils.renderIntoDocument(<ReportDetailController params={{}}/>),
            state = controller.getInitialState();
        expect(state.loading).toBeFalsy();
        expect(state.report).toBeDefined();
        expect(state.report.occurrenceTime).toBeDefined();
    });

    it('Uses report passed from router store if it is set', function () {
        var report = {
            initialReports: [{text: 'First Initial Report'}]
        };
        spyOn(RouterStore, 'getTransitionPayload').andReturn(report);

        var controller = TestUtils.renderIntoDocument(<ReportDetailController params={{}}/>),
            state = controller.getInitialState();
        expect(RouterStore.getTransitionPayload).toHaveBeenCalledWith('report_new');
        expect(state.loading).toBeFalsy();
        expect(state.report.initialReports).toEqual(report.initialReports);
        expect(state.report.occurrenceTime).toBeDefined();
    });

    it('Loads existing report when report key is passed in path params', function () {
        spyOn(Actions, 'findReport').andCallThrough();
        var params = {reportKey: 12345},
            controller = TestUtils.renderIntoDocument(<ReportDetailController params={params}/>),
            state = controller.getInitialState();
        expect(Actions.findReport).toHaveBeenCalledWith(params.reportKey);
        expect(state.loading).toBeTruthy();
        expect(state.report).toBeNull();
    });
});
