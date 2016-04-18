'use strict';

describe('ReportController', function () {

    var React = require('react'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator'),
        Actions = require('../../js/actions/Actions'),
        RouterStore = require('../../js/stores/RouterStore'),
        ReportController = require('../../js/components/report/ReportController'),
        Routes = require('../../js/utils/Routes'),
        Constants = require('../../js/constants/Constants');

    beforeEach(function () {
        spyOn(Actions, 'loadOptions');
        spyOn(Actions, 'loadOccurrenceCategories');
    });

    xit('Uses report passed from router store if it is set', function () {
        var report = {
            initialReports: [{text: 'First Initial Report'}],
            occurrenceStart: Date.now() - 10000,
            occurrenceEnd: Date.now()
        };
        spyOn(RouterStore, 'getTransitionPayload').and.returnValue(report);

        var controller = Environment.render(<ReportController params={{}}/>),
            state = controller.getInitialState();
        expect(RouterStore.getTransitionPayload).toHaveBeenCalledWith(Routes.createReport.name);
        expect(state.loading).toBeFalsy();
        expect(state.report.initialReports).toEqual(report.initialReports);
        expect(state.report.occurrence).toBeDefined();
    });

    it('Loads existing report when report key is passed in path params', function () {
        spyOn(Actions, 'loadReport');
        var params = {reportKey: 12345},
            controller = Environment.render(<ReportController params={params}/>),
            state = controller.state;
        expect(Actions.loadReport).toHaveBeenCalledWith(params.reportKey);
        expect(state.loading).toBeTruthy();
        expect(state.report).toBeNull();
    });

    it('Initializes new report when no key is specified', function () {
        var controller = Environment.render(<ReportController params={{}}/>),
            report = controller.state.report;

        expect(controller.state.loading).toBeFalsy();
        expect(report).toBeDefined();
        expect(report.occurrenceStart).toBeDefined();
        expect(report.occurrenceEnd).toBeDefined();
        expect(report.isNew).toBeTruthy();
        expect(report.occurrence).toBeDefined();
    });

    xit('Initializes new report with imported initial report', function () {
        var payload = {
            initialReports: [{
                text: 'Initial report'
            }]
        };
        spyOn(RouterStore, 'getTransitionPayload').and.returnValue(payload);
        var controller = Environment.render(<ReportController params={{}}/>),
            report = controller.state.report;

        expect(report.occurrence).toBeDefined();
        expect(report.initialReports.length).toEqual(1);
        expect(report.initialReports[0]).toEqual(payload.initialReports[0]);
    });
});
