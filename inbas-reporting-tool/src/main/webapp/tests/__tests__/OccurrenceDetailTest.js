'use strict';

describe('OccurrenceDetail', function () {

    var React = require('react'),
        Environment = require('../environment/Environment'),
        Constants = require('../../js/constants/Constants'),
        ReportFactory = require('../../js/model/ReportFactory'),
        OccurrenceDetail = require('../../js/components/report/occurrence/Occurrence');

    it('Sets occurrence end time to the same as start time when start time is edited for the first time in a new report', function () {
        var report = ReportFactory.createReport(),
            onAttributeChange = jasmine.createSpy('onAttributeChange'),
            detail = Environment.render(<OccurrenceDetail report={report} onAttributeChange={onAttributeChange}/>);

        var newStart = report.occurrenceStart - 100000;
        detail.onStartChange(newStart);
        expect(onAttributeChange).toHaveBeenCalledTimes(2);
        expect(onAttributeChange).toHaveBeenCalledWith('occurrenceStart', newStart);
        expect(onAttributeChange).toHaveBeenCalledWith('occurrenceEnd', newStart + Constants.MINUTE);
    });

    it('Change occurrence end only on first edit of occurrence start', function () {
        var report = ReportFactory.createReport(),
            onAttributeChange = jasmine.createSpy('onAttributeChange'),
            detail = Environment.render(<OccurrenceDetail report={report} onAttributeChange={onAttributeChange}/>);
        var newStart = report.occurrenceStart - 100000;
        detail.onStartChange(newStart);
        newStart = newStart + Constants.MINUTE;
        detail.onStartChange(newStart);
        expect(onAttributeChange).toHaveBeenCalledTimes(3);
    });

    it('Does not modify occurrence end for existing reports', function() {
        var report = ReportFactory.createReport(),
            onAttributeChange = jasmine.createSpy('onAttributeChange'),
            detail = Environment.render(<OccurrenceDetail report={report} onAttributeChange={onAttributeChange}/>);
        delete report.isNew;
        var newStart = report.occurrenceStart - Constants.MINUTE;
        detail.onStartChange(newStart);
        expect(onAttributeChange).toHaveBeenCalledTimes(1);
    });
});