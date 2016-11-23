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

    it('adds isEccairsReport function to loaded report', () => {
        const report = Generator.generateOccurrenceReport();
        delete report.isEccairsReport;
        mockResponse(null, report);
        spyOn(ReportStore, 'trigger').and.callThrough();
        ReportStore.onLoadReport(report.key);
        const triggerArg = ReportStore.trigger.calls.argsFor(0)[0];
        expect(triggerArg.action).toEqual(Actions.loadReport);
        const loadedReport = triggerArg.report;
        expect(typeof loadedReport.isEccairsReport).toEqual('function');
    });

    describe(' - added isEccairsReport method', () => {

        it('returns true for ECCAIRS report', () => {
            const report = Generator.generateOccurrenceReport();
            delete report.isEccairsReport;
            report.types = [Vocabulary.ECCAIRS_REPORT];
            mockResponse(null, report);
            spyOn(ReportStore, 'trigger').and.callThrough();
            ReportStore.onLoadReport(report.key);
            expect(report.isEccairsReport()).toBeTruthy();
        });

        it('returns false for non-ECCAIRS report', () => {
            const report = Generator.generateOccurrenceReport();
            delete report.isEccairsReport;
            report.types = [];
            mockResponse(null, report);
            spyOn(ReportStore, 'trigger').and.callThrough();
            ReportStore.onLoadReport(report.key);
            expect(report.isEccairsReport()).toBeFalsy();
        });
    });

    it('adds isSafaReport function to loaded report', () => {
        const report = Generator.generateAuditReport();
        mockResponse(null, report);
        spyOn(ReportStore, 'trigger').and.callThrough();
        ReportStore.onLoadReport(report.key);
        const triggerArg = ReportStore.trigger.calls.argsFor(0)[0];
        expect(triggerArg.action).toEqual(Actions.loadReport);
        const loadedReport = triggerArg.report;
        expect(typeof loadedReport.isSafaReport).toEqual('function');
    });

    describe(' - added isSafaReport method', () => {

        it('returns true for SAFA audit report', () => {
            const report = Generator.generateAuditReport();
            delete report.isSafaReport;
            report.types = [Vocabulary.SAFA_REPORT];
            mockResponse(null, report);
            spyOn(ReportStore, 'trigger').and.callThrough();
            ReportStore.onLoadReport(report.key);
            expect(report.isSafaReport()).toBeTruthy();
        });

        it('returns false for non-SAFA report', () => {
            const report = Generator.generateAuditReport();
            delete report.isSafaReport;
            report.types = [];
            mockResponse(null, report);
            spyOn(ReportStore, 'trigger').and.callThrough();
            ReportStore.onLoadReport(report.key);
            expect(report.isSafaReport()).toBeFalsy();
        });
    });
});
