'use strict';

import assign from "object-assign";
import Constants from "../constants/Constants";
import Vocabulary from "../constants/Vocabulary";

class OccurrenceBase {
    constructor(data = {}) {
        assign(this, data);
    }

    initWith(occurrence, report) {
        assign(this, occurrence);
        this.javaClass = Constants.OCCURRENCE_SAFETY_ISSUE_BASE_CLASS;
        this.severity = report.severityAssessment;
        this.reportKey = report.key;
        if (!this.types) {
            this.types = [];
        }
        if (this.types.indexOf(Vocabulary.OCCURRENCE) === -1) {
            this.types.push(Vocabulary.OCCURRENCE);
        }
    }

    getLabel() {
        return 'occurrencereport.label';
    }

    getName() {
        return this.name;
    }

    renderMoreInfo() {
        // TODO
        return '';
    }
}

class AuditFindingBase {
    constructor(data = {}) {
        assign(this, data);
    }

    initWith(finding, report) {
        assign(this, finding);
        this.javaClass = Constants.AUDIT_FINDING_SAFETY_ISSUE_BASE_CLASS;
        this.reportKey = report.key;
        if (!this.types) {
            this.types = [];
        }
        if (this.types.indexOf(Vocabulary.AUDIT_FINDING) === -1) {
            this.types.push(Vocabulary.AUDIT_FINDING);
        }
    }

    getLabel() {
        return 'audit.finding.header';
    }

    getName() {
        return this.description;
    }

    renderMoreInfo() {
        // TODO
        return '';
    }
}

export default class SafetyIssueBase {

    static create(event, report) {
        var base;
        if (report) {
            if (report.javaClass && report.javaClass === Constants.OCCURRENCE_REPORT_JAVA_CLASS) {
                base = new OccurrenceBase();
                base.initWith(report.occurrence, report);
            } else if (report.javaClass && report.javaClass === Constants.AUDIT_REPORT_JAVA_CLASS) {
                base = new AuditFindingBase(event, report);
                base.initWith(event, report);
            }
        } else if (event.javaClass === Constants.OCCURRENCE_SAFETY_ISSUE_BASE_CLASS) {
            base = new OccurrenceBase(event);
        } else if (event.javaClass === Constants.AUDIT_FINDING_SAFETY_ISSUE_BASE_CLASS) {
            base = new AuditFindingBase(event);
        }
        return base;
    }
}
