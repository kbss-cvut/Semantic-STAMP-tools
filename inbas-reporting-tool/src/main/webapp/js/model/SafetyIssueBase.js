'use strict';

import React from "react";
import assign from "object-assign";
import JsonLdUtils from "jsonld-utils";
import Constants from "../constants/Constants";
import I18nStore from "../stores/I18nStore";
import ObjectTypeResolver from "../utils/ObjectTypeResolver";
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

    renderMoreInfo(options) {
        const type = ObjectTypeResolver.resolveType(this, options.category);
        return type ? JsonLdUtils.getJsonAttValue(type, Vocabulary.RDFS_LABEL) : '';
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

    renderMoreInfo(options) {
        const type = ObjectTypeResolver.resolveType(this, options.findingType),
            label = type ? JsonLdUtils.getJsonAttValue(type, Vocabulary.RDFS_LABEL) + ',' : '';
        return <div>
            <span className='issue-base-more-info'>{label}</span>
            <span className='issue-base-more-info'>
                {I18nStore.i18n('audit.findings.table.level') + ' ' + this.level}
            </span>
        </div>;
    }
}

export default class SafetyIssueBase {

    static create(event, report) {
        let base;
        if (report) {
            if (report.javaClass && report.javaClass === Constants.OCCURRENCE_REPORT_JAVA_CLASS) {
                base = new OccurrenceBase();
                base.initWith(report.occurrence, report);
            } else if (report.javaClass && report.javaClass === Constants.AUDIT_REPORT_JAVA_CLASS) {
                base = new AuditFindingBase();
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
