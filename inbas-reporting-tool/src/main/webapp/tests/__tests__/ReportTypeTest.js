'use strict';

describe('ReportType', function () {

    var ReportType = require('../../js/model/ReportType'),
        Generator = require('../environment/Generator'),
        PreliminaryReportFactory = require('../../js/model/PreliminaryReportFactory'),
        InvestigationController = require('../../js/components/report/occurrence/InvestigationController');

    it('returns default detail controller for new report when getDetailController is called', function () {
        var report = PreliminaryReportFactory.createReport(),

            controller = ReportType.getDetailController(report);
        expect(controller).toEqual(InvestigationController);
    });
});
