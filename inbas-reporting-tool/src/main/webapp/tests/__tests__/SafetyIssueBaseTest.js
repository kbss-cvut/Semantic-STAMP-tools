'use strict';

import Generator from "../environment/Generator";
import Constants from "../../js/constants/Constants";
import Vocabulary from "../../js/constants/Vocabulary";
import SafetyIssueBase from "../../js/model/SafetyIssueBase";

describe('SafetyIssueBase', () => {

    it('creates occurrence base for an occurrence report instance', () => {
        var occurrenceReport = Generator.generateOccurrenceReport(),

            result = SafetyIssueBase.create(occurrenceReport.occurrence, occurrenceReport);
        expect(result.javaClass).toEqual(Constants.OCCURRENCE_SAFETY_ISSUE_BASE_CLASS);
        expect(result.types.indexOf(Vocabulary.OCCURRENCE)).not.toEqual(-1);
    });

    it('adds occurrence report key and severity to occurrence base when base is created from occurrence report', () => {
        var occurrenceReport = Generator.generateOccurrenceReport(),

            result = SafetyIssueBase.create(occurrenceReport.occurrence, occurrenceReport);
        expect(result.reportKey).toEqual(occurrenceReport.key);
        expect(result.severity).toEqual(occurrenceReport.severityAssessment);
    });

    it('creates audit finding base for an audit finding instance', () => {
        var report = Generator.generateAuditReport(),
            finding = report.audit.findings[0],

            result = SafetyIssueBase.create(finding, report);
        expect(result.javaClass).toEqual(Constants.AUDIT_FINDING_SAFETY_ISSUE_BASE_CLASS);
        expect(result.types.indexOf(Vocabulary.AUDIT_FINDING)).not.toEqual(-1);
    });

    it('adds audit report key to audit finding base when base is created from audit report', () => {
        var report = Generator.generateAuditReport(),
            finding = report.audit.findings[0],

            result = SafetyIssueBase.create(finding, report);
        expect(result.reportKey).toEqual(report.key);
    });
});
