'use strict';

import React from "react";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import AuditFinding from "../../js/components/report/audit/AuditFinding";

describe('AuditFinding', () => {

    var finding,
        report,
        onSave, onClose;

    beforeEach(() => {
        finding = {};
        report = {
            isNew: true
        };
        onSave = jasmine.createSpy('onSave');
        onClose = jasmine.createSpy('onClose');
    });

    it('sets finding type on new finding when type is selected', () => {
        var selectedType = Generator.randomCategory(),
            component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report}/>);
        component.getWrappedComponent().state.findingType = Generator.getCategories();
        component.getWrappedComponent()._onTypeSelected(selectedType);

        var newFinding = component.getWrappedComponent().state.finding;
        expect(newFinding.types.indexOf(selectedType.id)).not.toEqual(-1);
    });

    it('replaces finding type in existing finding when type is selected', () => {
        var originalType = Generator.randomCategory(),
            selectedType = Generator.randomCategory();
        finding.types = [originalType.id];
        var component = Environment.render(<AuditFinding onSave={onSave} onClose={onClose} finding={finding}
                                                         report={report}/>);
        component.getWrappedComponent().state.findingType = Generator.getCategories();
        component.getWrappedComponent()._onTypeSelected(selectedType);

        var newFinding = component.getWrappedComponent().state.finding;
        expect(newFinding.types.indexOf(selectedType.id)).not.toEqual(-1);
        if (selectedType !== originalType) {
            expect(newFinding.types.indexOf(originalType.id)).toEqual(-1);
        }
    });
});
