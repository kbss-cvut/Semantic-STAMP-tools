'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Actions from "../../js/actions/Actions";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import en from "../../js/i18n/en";
import AuditFindings from "../../js/components/report/audit/AuditFindings";

describe('AuditFindings', () => {

    var audit,
        onChange;

    beforeEach(() => {
        audit = {
            uri: Generator.getRandomUri()
        };
        onChange = jasmine.createSpy('onChange');
        spyOn(Actions, 'loadOptions');
    });

    it('renders notice when there are no findings in audit', () => {
        var component = Environment.render(<AuditFindings audit={audit} onChange={onChange}/>),

            notice = Environment.getComponentByTagAndText(component, 'div', en.messages['audit.findings.no-findings-message']),
            tables = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap').Table);
        expect(notice).not.toBeNull();
        expect(tables.length).toEqual(0);
    });

    it('renders findings table when findings are present', () => {
        audit.findings = generateFindings();
        var component = Environment.render(<AuditFindings audit={audit} onChange={onChange}/>),

            notice = Environment.getComponentByTagAndText(component, 'div', en.messages['audit.findings.no-findings-message']);
        expect(notice).toBeNull();
        var rows = TestUtils.scryRenderedDOMComponentsWithTag(component, 'tr');
        expect(rows.length).toEqual(audit.findings.length + 1); // + table head
    });

    function generateFindings() {
        var findings = [];
        for (var i = 0, count = Generator.getRandomPositiveInt(1, 5); i < count; i++) {
            findings.push({
                description: 'Test ' + i,
                types: [Generator.getRandomUri()],
                level: Generator.getRandomBoolean() ? 0 : 1
            });
        }
        return findings;
    }

    it('removes finding from audit on delete', () => {
        audit.findings = generateFindings();
        var component = Environment.render(<AuditFindings audit={audit} onChange={onChange}/>),
            toRemove = audit.findings[Generator.getRandomInt(audit.findings.length)];

        component._onDeleteFinding(toRemove);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.findings.length).toEqual(audit.findings.length - 1);
        expect(change.findings.indexOf(toRemove)).toEqual(-1);
    });

    it('replaces finding with updated one on finding edit finish', () => {
        audit.findings = generateFindings();
        var component = Environment.render(<AuditFindings audit={audit} onChange={onChange}/>),
            indexToEdit = Generator.getRandomInt(audit.findings.length),
            toEdit = audit.findings[indexToEdit],
            newEdited = {uri: Generator.getRandomUri()};

        component.state.currentFinding = toEdit;
        component._onEditFinish(newEdited);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.findings.indexOf(toEdit)).toEqual(-1);
        expect(change.findings[indexToEdit]).toEqual(newEdited);
    });

    it('simply adds finding on edit finish when it was added as new', () => {
        audit.findings = generateFindings();
        var component = Environment.render(<AuditFindings audit={audit} onChange={onChange}/>),
            newFinding = {
                isNew: true,
                description: 'Test'
            };

        component.state.currentFinding = newFinding;
        component._onEditFinish(newFinding);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.findings.length).toEqual(audit.findings.length + 1);
        expect(change.findings[change.findings.length - 1]).toEqual(newFinding);
        expect(newFinding.isNew).not.toBeDefined();
    });

    it('adds finding when it is the first to be added to audit', () => {
        var component = Environment.render(<AuditFindings audit={audit} onChange={onChange}/>),
            newFinding = {
                isNew: true,
                description: 'Test'
            };

        component.state.currentFinding = newFinding;
        component._onEditFinish(newFinding);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.findings.length).toEqual(1);
        expect(change.findings[0]).toEqual(newFinding);
    });
});
