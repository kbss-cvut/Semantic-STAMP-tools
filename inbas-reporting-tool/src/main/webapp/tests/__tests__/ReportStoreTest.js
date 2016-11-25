'use strict';

describe('Report store', function () {

    const rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Actions = require('../../js/actions/Actions'),
        Ajax = rewire('../../js/utils/Ajax'),
        ReportStore = rewire('../../js/stores/ReportStore'),
        reqMockMethods = ['get', 'put', 'post', 'del', 'send', 'accept', 'set', 'end'],
        Vocabulary = require('../../js/constants/Vocabulary');
    let reqMock;

    beforeEach(function () {
        reqMock = Environment.mockRequestMethods(reqMockMethods);
        Ajax.__set__('request', reqMock);
        Ajax.__set__('Logger', Environment.mockLogger());
        ReportStore.__set__('Ajax', Ajax);
        ReportStore.__set__('reportsLoading', false);
        jasmine.getGlobal().top = {};
    });

    it('triggers with data and action identification when reports are loaded', function () {
        const reports = [
            {id: 'reportOne'},
            {id: 'reportTwo'}
        ];
        spyOn(ReportStore, 'trigger').and.callThrough();
        mockResponse(null, reports);
        ReportStore.onLoadAllReports();

        expect(ReportStore.trigger).toHaveBeenCalledWith({
            action: Actions.loadAllReports,
            reports: reports
        });
    });

    function mockResponse(err, body) {
        reqMock.end.and.callFake(function (handler) {
            handler(err, {
                body: body
            });
        });
    }

    it('triggers with empty reports when an ajax error occurs', function () {
        spyOn(ReportStore, 'trigger').and.callThrough();
        mockResponse({
            status: 400,
            response: {
                text: '{"message": "Error message." }',
                req: {
                    method: 'GET'
                }
            }
        }, null);
        ReportStore.onLoadAllReports();

        expect(ReportStore.trigger).toHaveBeenCalledWith({
            action: Actions.loadAllReports,
            reports: []
        });
    });

    it('triggers with data and action when report is loaded', function () {
        const report = {id: 'reportOne'};
        spyOn(ReportStore, 'trigger').and.callThrough();
        mockResponse(null, report);
        ReportStore.onLoadReport();

        expect(ReportStore.trigger).toHaveBeenCalledWith({
            action: Actions.loadReport,
            report: report
        });
    });

    it('triggers with null report when ajax error occurs', function () {
        spyOn(ReportStore, 'trigger').and.callThrough();
        mockResponse({
            status: 404,
            response: {
                text: '{"message": "Report not found." }',
                req: {
                    method: 'GET'
                }
            }
        }, null);
        ReportStore.onLoadReport();

        expect(ReportStore.trigger).toHaveBeenCalledWith({
            action: Actions.loadReport,
            report: null
        });
    });

    it('does not start new request when loadAllReports is triggered and reports are already being loaded', () => {
        const reports = [
            {id: 'reportOne'},
            {id: 'reportTwo'}
        ];
        reqMock.end.and.callFake(function (handler) {
            setTimeout(() => {
                handler(null, {
                    body: reports
                });
            }, 500);

        });
        ReportStore.onLoadAllReports();
        ReportStore.onLoadAllReports();

        expect(reqMock.end.calls.count()).toEqual(1);
    });

    it('loads safety issue and adds base to it when addSafetyIssueBase is triggered', () => {
        const baseReport = Generator.generateOccurrenceReport(),
            base = baseReport.occurrence,
            issue = Generator.generateSafetyIssueReport();
        spyOn(ReportStore, 'trigger').and.callThrough();
        mockResponse(null, issue);
        ReportStore.onAddSafetyIssueBase(issue.key, {event: base, report: baseReport});
        expect(reqMock.end).toHaveBeenCalled();
        expect(issue.safetyIssue.basedOn).toBeDefined();
        expect(issue.safetyIssue.basedOn[0].uri).toEqual(base.uri);
        expect(issue.safetyIssue.basedOn[0].reportKey).toEqual(baseReport.key);
        expect(ReportStore.trigger).toHaveBeenCalled();
    });

    it('prevents report loading when it is already being loaded', () => {
        const issue = Generator.generateSafetyIssueReport();
        ReportStore.onLoadReport(issue.key);
        ReportStore.onLoadReport(issue.key);
        expect(reqMock.end.calls.count()).toEqual(1);
    });

    it('adds isSafaReport and isEccairsReport functions to loaded report', () => {
        const report = Generator.generateOccurrenceReport();
        delete report.isEccairsReport;
        mockResponse(null, report);
        spyOn(ReportStore, 'trigger').and.callThrough();
        ReportStore.onLoadReport(report.key);
        const triggerArg = ReportStore.trigger.calls.argsFor(0)[0];
        expect(triggerArg.action).toEqual(Actions.loadReport);
        const loadedReport = triggerArg.report;
        expect(typeof loadedReport.isEccairsReport).toEqual('function');
        expect(typeof loadedReport.isSafaReport).toEqual('function');
    });

    it('adds isSafaReport and isEccairsReport functions to all loaded reports', () => {
        const reports = Generator.generateReports();
        reports.forEach(r => {
            delete r.isSafaReport;
            delete r.isEccairsReport;
        });
        mockResponse(null, reports);
        spyOn(ReportStore, 'trigger').and.callThrough();
        ReportStore.onLoadAllReports();
        const triggerArg = ReportStore.trigger.calls.argsFor(0)[0];
        expect(ReportStore.trigger).toHaveBeenCalled();
        expect(triggerArg.action).toEqual(Actions.loadAllReports);
        const triggeredReports = triggerArg.reports;
        for (let i = 0, len = triggeredReports.length; i < len; i++) {
            expect(triggeredReports[i].isSafaReport).toBeDefined();
            expect(triggeredReports[i].isEccairsReport).toBeDefined();
        }
    });
});
