'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import {DropdownButton} from "react-bootstrap";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import Actions from "../../js/actions/Actions";
import AuditFinding from "../../js/components/report/audit/AuditFinding";
import FindingMeasures from "../../js/components/report/audit/FindingMeasures";
import SafaAuditFindingAttributes from "../../js/components/report/audit/SafaAuditFindingAttributes";

describe('AuditFinding', () => {

    var finding,
        report,
        onSave, onClose;

    beforeEach(() => {
        finding = {};
        report = {
            isNew: true,
            isSafa: function () {
                return false;
            }
        };
        onSave = jasmine.createSpy('onSave');
        onClose = jasmine.createSpy('onClose');
        spyOn(Actions, 'loadOptions');
    });

    it('sets finding type on new finding when type is selected', () => {
        var selectedType = Generator.randomCategory(),
            component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report}/>);
        component.state.findingType = Generator.getCategories();
        component._onTypeSelected(selectedType);

        var newFinding = component.state.finding;
        expect(newFinding.types.indexOf(selectedType.id)).not.toEqual(-1);
    });

    it('replaces finding type in existing finding when type is selected', () => {
        var originalType = Generator.randomCategory(),
            selectedType = Generator.randomCategory();
        finding.types = [originalType.id];
        var component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report}/>);
        component.state.findingType = Generator.getCategories();
        component._onTypeSelected(selectedType);

        var newFinding = component.state.finding;
        expect(newFinding.types.indexOf(selectedType.id)).not.toEqual(-1);
        if (selectedType !== originalType) {
            expect(newFinding.types.indexOf(originalType.id)).toEqual(-1);
        }
    });

    it('does not show safety issue button when audit is new', () => {
        report.isNew = true;
        var component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report} show={true}/>);

        var siButton = TestUtils.scryRenderedComponentsWithType(component._modalFooter, DropdownButton);
        expect(siButton.length).toEqual(0);
    });

    it('does not show safety issue button when finding is new', () => {
        report.isNew = false;
        var component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report} show={true}/>);

        var siButton = TestUtils.scryRenderedComponentsWithType(component._modalFooter, DropdownButton);
        expect(siButton.length).toEqual(0);
    });

    it('shows safety issue button when existing finding is used', () => {
        report.isNew = false;
        finding.uri = Generator.getRandomUri();
        var component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report} show={true}/>);

        var siButton = TestUtils.scryRenderedComponentsWithType(component._modalFooter, DropdownButton);
        expect(siButton.length).toEqual(1);
    });

    it('does not show status and date of last status modification if audit is not a SAFA audit', () => {
        finding.uri = Generator.getRandomUri();

        let component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report} show={true}/>),
            safaAttributes = TestUtils.scryRenderedComponentsWithType(component._modalBody, SafaAuditFindingAttributes);
        expect(safaAttributes.length).toEqual(0);
    });

    it('shows finding status and date of last status modification if audit is SAFA audit', () => {
        report.isSafa = function () {
            return true;
        };
        finding.uri = Generator.getRandomUri();

        let component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report} show={true}/>),
            safaAttributes = TestUtils.scryRenderedComponentsWithType(component._modalBody, SafaAuditFindingAttributes);
        expect(safaAttributes.length).toEqual(1);
    });

    it('does not show corrective measures if audit is SAFA audit', () => {
        report.isSafa = function () {
            return true;
        };
        finding.uri = Generator.getRandomUri();

        let component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report} show={true}/>),
            measures = TestUtils.scryRenderedComponentsWithType(component._modalBody, FindingMeasures);
        expect(measures.length).toEqual(0);
    });
});
