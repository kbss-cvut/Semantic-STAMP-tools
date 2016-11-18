'use strict';

import React from "react";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import Actions from "../../js/actions/Actions";
import ReportFactory from "../../js/model/ReportFactory";
import Audit from "../../js/components/report/audit/Audit";

describe('Audit', () => {

    var audit,
        report,
        onChange;

    beforeEach(() => {
        report = ReportFactory.createAuditReport();
        audit = report.audit;
        onChange = jasmine.createSpy('onChange');
        spyOn(Actions, 'loadOptions');
    });

    it('replaces old audit type with the selected on on audit type select', () => {
        var originalCategory = Generator.randomCategory(),
            selectedCategory = Generator.randomCategory();
        audit.types = ['http://onto.fel.cvut.cz/ontologies/ufo/Event', originalCategory];
        var component = Environment.render(<Audit audit={audit} report={report} onChange={onChange}/>);
        component.state.auditType = Generator.getCategories();
        component._onTypeSelected(selectedCategory);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0],
            newTypes = change.audit.types;
        expect(newTypes.indexOf(selectedCategory.id)).not.toEqual(-1);
        if (selectedCategory.id !== originalCategory.id) {
            expect(newTypes.indexOf(originalCategory.id)).toEqual(-1);
        }
    });

    it('adds selected audit type when no other types where present in audit on audit type select', () => {
        var selectedCategory = Generator.randomCategory();
        var component = Environment.render(<Audit audit={audit} report={report} onChange={onChange}/>);
        component.state.auditType = Generator.getCategories();
        component._onTypeSelected(selectedCategory);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0],
            newTypes = change.audit.types;
        expect(newTypes.indexOf(selectedCategory.id)).not.toEqual(-1);
    })
});
