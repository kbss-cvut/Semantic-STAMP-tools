'use strict';

describe('Report store', function () {

    const rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Actions = require('../../js/actions/Actions'),
        Ajax = rewire('../../js/utils/Ajax'),
        Generator = require('../environment/Generator').default,
        ReportStore = rewire('../../js/stores/ReportStore'),
        reqMockMethods = ['get', 'put', 'post', 'del', 'send', 'accept', 'set', 'end'];
    let reqMock;

    beforeEach(function () {
        reqMock = Environment.mockRequestMethods(reqMockMethods);
        Ajax.__set__('request', reqMock);
        Ajax.__set__('Logger', Environment.mockLogger());
        ReportStore.__set__('Ajax', Ajax);
        jasmine.getGlobal().top = {};
        ReportStore.__set__('reportsLoading', false);
    });

    it('triggers with data and action identification when reports are loaded', function () {
        const reports = [
            {id: 'reportOne'},
            {id: 'reportTwo'}
        ];
        spyOn(ReportStore, 'trigger').and.callThrough();
        reqMock.end.and.callFake(function (handler) {
            handler(null, {
                body: reports
            });
        });
        ReportStore.onLoadAllReports();

        expect(ReportStore.trigger).toHaveBeenCalledWith({
            action: Actions.loadAllReports,
            reports: reports
        });
    });

    it('triggers with empty reports when an ajax error occurs', function () {
        spyOn(ReportStore, 'trigger').and.callThrough();
        reqMock.end.and.callFake(function (handler) {
            const err = {
                status: 400,
                response: {
                    text: '{"message": "Error message." }',
                    req: {
                        method: 'GET'
                    }
                }
            };
            handler(err, null);
        });
        ReportStore.onLoadAllReports();

        expect(ReportStore.trigger).toHaveBeenCalledWith({
            action: Actions.loadAllReports,
            reports: []
        });
    });

    it('triggers with data and action when report is loaded', function () {
        const report = {id: 'reportOne'};
        spyOn(ReportStore, 'trigger').and.callThrough();
        reqMock.end.and.callFake(function (handler) {
            handler(null, {
                body: report
            });
        });
        ReportStore.onLoadReport();

        expect(ReportStore.trigger).toHaveBeenCalledWith({
            action: Actions.loadReport,
            report: report
        });
    });

    it('triggers with null report when ajax error occurs', function () {
        spyOn(ReportStore, 'trigger').and.callThrough();
        reqMock.end.and.callFake(function (handler) {
            const err = {
                status: 404,
                response: {
                    text: '{"message": "Report not found." }',
                    req: {
                        method: 'GET'
                    }
                }
            };
            handler(err, null);
        });
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

    it('passes report keys as query params when they are specified', () => {
        const keys = [];
        for (let i = 0, cnt = Generator.getRandomPositiveInt(1, 5); i < cnt; i++) {
            keys.push(Generator.getRandomInt().toString());
        }
        spyOn(Ajax, 'get').and.callThrough();
        ReportStore.onLoadAllReports(keys);

        expect(Ajax.get).toHaveBeenCalled();
        const url = Ajax.get.calls.argsFor(0)[0];
        keys.forEach(key => {
            expect(url.indexOf('key=' + key)).not.toEqual(-1);
        });
    });
});
