'use strict';

describe('Report validator', function () {

    var ReportValidator = require('../../js/validation/ReportValidator'),
        Constants = require('../../js/constants/Constants'),
        report;

    beforeEach(function () {
        report = {
            occurrence: {
                name: 'TestReport'
            },
            occurrenceStart: Date.now() - 1000,
            occurrenceEnd: Date.now(),
            severityAssessment: 'INCIDENT',
            occurrenceCategory: {
                eventType: {
                    id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-28',
                    name: '28 - RI-VAP: Runway incursion - vehicle, aircraft or person'
                }
            },
            summary: 'Report narrative',
            typeAssessments: [{
                eventType: {
                    id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100',
                    name: '2200100 - Runway incursions'
                }
            }]
        };
    });

    it('marks valid report as valid', function () {
        expect(ReportValidator.isValid(report)).toBeTruthy();
    });

    it('marks report without headline as invalid', function () {
        report.occurrence.name = '';
        expect(ReportValidator.isValid(report)).toBeFalsy();
    });

    it('marks report without occurrence class as invalid', function () {
        delete report.severityAssessment;
        expect(ReportValidator.isValid(report)).toBeFalsy();
    });

    it('marks report without occurrence category as invalid', function () {
        delete report.occurrenceCategory;
        expect(ReportValidator.isValid(report)).toBeFalsy();
    });

    it('marks report without narrative as invalid', function () {
        report.summary = '';
        expect(ReportValidator.isValid(report)).toBeFalsy();
    });

    it('returns missing field message when report without narrative is validated', function () {
        report.summary = '';
        expect(ReportValidator.getValidationMessage(report)).toEqual('detail.invalid-tooltip');
    });

    it('marks report with too large occurrence start and end time diff as invalid', function () {
        report.occurrenceStart = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
        report.occurrenceEnd = Date.now();
        expect(ReportValidator.isValid(report)).toBeFalsy();
    });

    it('reports time difference error message when report with too large occurrence start and end time diff is validated', function () {
        report.occurrenceStart = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
        report.occurrenceEnd = Date.now();
        expect(ReportValidator.getValidationMessage(report)).toEqual('detail.large-time-diff-tooltip');
    });

    it('marks report with too large occurrence start and end time diff as not renderable', function () {
        report.occurrenceStart = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
        report.occurrenceEnd = Date.now();
        expect(ReportValidator.canRender(report)).toBeFalsy();
    });
});