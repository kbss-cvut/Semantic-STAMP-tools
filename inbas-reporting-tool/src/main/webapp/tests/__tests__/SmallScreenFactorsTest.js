'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Actions from "../../js/actions/Actions";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import SmallScreenFactors from "../../js/components/factor/smallscreen/SmallScreenFactors";

describe('SmallScreenFactors', () => {

    let report,
        onChange;

    beforeEach(() => {
        onChange = jasmine.createSpy('onChange');
        report = Generator.generateOccurrenceReport();
        const nodes = Generator.generateFactorGraphNodes();
        nodes.unshift(report.occurrence);
        report.factorGraph = {
            nodes: nodes,
            links: []
        };
        spyOn(Actions, 'loadOptions');
    });

    it('does not render line for the occurrence', () => {
        report.occurrence.eventType = Generator.getRandomUri();
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}/>),
            row = Environment.getComponentByTagAndContainedText(component, 'tr', report.occurrence.eventType);
        expect(row).toBeNull();
    });

    it('renders nothing when report does not contain a factor graph', () => {
        delete report.factorGraph;
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}/>),
            panel = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap').Panel);
        expect(panel.length).toEqual(0);
    });

    it('removes node from factor graph when delete is clicked and confirmed', () => {
        const toRemove = report.factorGraph.nodes[Generator.getRandomPositiveInt(1, report.factorGraph.nodes.length)],
            component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}/>);
        const rowsBefore = TestUtils.scryRenderedDOMComponentsWithTag(component, 'tr');
        component._onDeleteClick(toRemove);
        component._onDeleteSubmit();
        const rowsAfter = TestUtils.scryRenderedDOMComponentsWithTag(component, 'tr');
        expect(rowsAfter.length).toEqual(rowsBefore.length - 1);
        expect(component.state.factorGraph.nodes.indexOf(toRemove)).toEqual(-1);
    });

    it('renders editable row when edit factor is clicked', () => {
        const toEdit = report.factorGraph.nodes[Generator.getRandomPositiveInt(1, report.factorGraph.nodes.length)],
            component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}/>);
        const editableBefore = TestUtils.scryRenderedComponentsWithType(component,
            require('../../js/components/factor/smallscreen/FactorEditRow').default.wrappedComponent);
        expect(editableBefore.length).toEqual(0);
        component._onEditClick(toEdit);
        const editableAfter = TestUtils.scryRenderedComponentsWithType(component,
            require('../../js/components/factor/smallscreen/FactorEditRow').default.wrappedComponent);
        expect(editableAfter.length).toEqual(1);
    });
});
