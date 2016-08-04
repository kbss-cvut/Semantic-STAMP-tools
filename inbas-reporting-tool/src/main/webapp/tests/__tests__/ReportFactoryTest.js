'use strict';

describe('Report factory', () => {

    var Constants = require('../../js/constants/Constants'),
        Vocabulary = require('../../js/constants/Vocabulary'),
        ReportFactory = require('../../js/model/ReportFactory');

    it('creates occurrence report for occurrence report Java class.', () => {
        var report = ReportFactory.createReport(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
        expect(report.javaClass).toEqual(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
    });

    it('creates occurrence report for occurrence report OWL class.', () => {
        var report = ReportFactory.createReport(Vocabulary.OCCURRENCE_REPORT);
        expect(report.javaClass).toEqual(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
    });

    it('creates safety issue report report for safety issue report Java class.', () => {
        var report = ReportFactory.createReport(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
        expect(report.javaClass).toEqual(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
    });

    it('creates safety issue report report for safety issue report OWL class.', () => {
        var report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
        expect(report.javaClass).toEqual(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
    });
});
