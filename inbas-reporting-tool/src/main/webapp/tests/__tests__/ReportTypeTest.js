'use strict';

describe('ReportType', function () {

    var ReportType = require('../../js/model/ReportType'),
        Generator = require('../environment/Generator'),
        PreliminaryReportFactory = require('../../js/model/PreliminaryReportFactory'),
        PreliminaryReportController = require('../../js/components/preliminary/ReportDetailController'),
        InvestigationController = require('../../js/components/investigation/InvestigationController');

    it('returns preliminary detail controller for preliminary report object when getDetailController is called', function () {
        var report = Generator.generatePreliminaryReport(),

            controller = ReportType.getDetailController(report);
        expect(controller).toEqual(PreliminaryReportController);
    });

    it('returns preliminary detail controller for new report (which is preliminary) when getDetailController is called', function () {
        var report = PreliminaryReportFactory.createReport(),

            controller = ReportType.getDetailController(report);
        expect(controller).toEqual(PreliminaryReportController);
    });

    it('returns investigation controller for investigation report object when getDetailController is called', function () {
        var report = Generator.generateInvestigation(),

            controller = ReportType.getDetailController(report);
        expect(controller).toEqual(InvestigationController);
    });
});
