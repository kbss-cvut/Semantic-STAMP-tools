'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import assign from "object-assign";
import Actions from "../../js/actions/Actions";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import SmallScreenFactors from "../../js/components/factor/smallscreen/SmallScreenFactors";
import Vocabulary from "../../js/constants/Vocabulary";

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
            edges: []
        };
        spyOn(Actions, 'loadOptions');
    });

    it('does not render line for the occurrence', () => {
        report.occurrence.eventType = Generator.getRandomUri();
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                                 rootAttribute='occurrence'/>),
            row = Environment.getComponentByTagAndContainedText(component, 'tr', report.occurrence.eventType);
        expect(row).toBeNull();
    });

    it('renders nothing when report does not contain a factor graph', () => {
        delete report.factorGraph;
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                                 rootAttribute='occurrence'/>),
            panel = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap').Panel);
        expect(panel.length).toEqual(0);
    });

    it('removes node from factor graph when delete is clicked and confirmed', () => {
        const toRemove = report.factorGraph.nodes[Generator.getRandomPositiveInt(1, report.factorGraph.nodes.length)],
            component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                               rootAttribute='occurrence'/>);
        const rowsBefore = TestUtils.scryRenderedDOMComponentsWithTag(component, 'tr');
        component._onDeleteClick(toRemove);
        component._onDeleteSubmit();
        const rowsAfter = TestUtils.scryRenderedDOMComponentsWithTag(component, 'tr');
        expect(rowsAfter.length).toEqual(rowsBefore.length - 1);
        expect(component.state.factorGraph.nodes.indexOf(toRemove)).toEqual(-1);
    });

    it('renders editable row when edit factor is clicked', () => {
        const toEdit = report.factorGraph.nodes[Generator.getRandomPositiveInt(1, report.factorGraph.nodes.length)],
            component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                               rootAttribute='occurrence'/>);
        const editableBefore = TestUtils.scryRenderedComponentsWithType(component,
            require('../../js/components/factor/smallscreen/FactorEditRow').default.wrappedComponent);
        expect(editableBefore.length).toEqual(0);
        component._onEditClick(toEdit);
        const editableAfter = TestUtils.scryRenderedComponentsWithType(component,
            require('../../js/components/factor/smallscreen/FactorEditRow').default.wrappedComponent);
        expect(editableAfter.length).toEqual(1);
    });

    it('replaces edited factor with its updated version on edit finish', () => {
        const toEdit = report.factorGraph.nodes[Generator.getRandomPositiveInt(1, report.factorGraph.nodes.length)],
            update = assign({}, toEdit),
            component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                               rootAttribute='occurrence'/>);
        component._onEditClick(toEdit);
        update.eventType = Generator.randomCategory().id;
        component._onEditFinish(update);
        const factorGraph = component.state.factorGraph;
        expect(factorGraph.nodes.indexOf(toEdit)).toEqual(-1);
        expect(factorGraph.nodes.indexOf(update)).not.toEqual(-1);
        expect(component.state.currentFactor).toBeNull();
        expect(component.state.editRow).toBeFalsy();
    });

    it('replaces the edited factor also in factor graph edges on edit finish', () => {
        const toEdit = report.factorGraph.nodes[Generator.getRandomPositiveInt(1, report.factorGraph.nodes.length)],
            update = assign({}, toEdit);
        report.factorGraph.edges.push({
            from: report.factorGraph.nodes[0],
            to: toEdit,
            linkType: Vocabulary.HAS_PART
        }, {
            from: toEdit,
            to: report.factorGraph.nodes[2],
            linkType: Vocabulary.HAS_PART
        });
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                                 rootAttribute='occurrence'/>);
        component._onEditClick(toEdit);
        update.eventType = Generator.randomCategory().id;
        component._onEditFinish(update);
        const factorGraph = component.state.factorGraph;
        expect(factorGraph.nodes.indexOf(toEdit)).toEqual(-1);
        expect(factorGraph.nodes.indexOf(update)).not.toEqual(-1);
        expect(component.state.currentFactor).toBeNull();
        expect(component.state.editRow).toBeFalsy();
        for (let i = 0, len = report.factorGraph.edges.length; i < len; i++) {
            if (report.factorGraph.edges[i].from === toEdit) {
                expect(factorGraph.edges[i].from).toEqual(update);
            } else if (report.factorGraph.edges[i].to === toEdit) {
                expect(factorGraph.edges[i].to).toEqual(update);
            }
        }
    });

    it('creates new factor and sets it as currently edited one when add is clicked', () => {
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                                 rootAttribute='occurrence'/>),
            nodes = component.state.factorGraph.nodes.slice();
        component._onAdd();
        expect(component.state.factorGraph.nodes.length).toEqual(nodes.length + 1);
        expect(component.state.currentFactor).not.toBeNull();
        expect(component.state.editRow).toBeTruthy();
    });

    it('sets reference id on the newly added factor', () => {
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                                 rootAttribute='occurrence'/>);
        component._onAdd();
        expect(component.state.factorGraph.nodes[component.state.factorGraph.nodes.length - 1].referenceId).toBeDefined();
    });

    it('adds a has-part link from parent to the new factor when new factor is created', () => {
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                                 rootAttribute='occurrence'/>),
            edges = component.state.factorGraph.edges.slice();
        component._onAdd();
        expect(component.state.factorGraph.edges.length).toEqual(edges.length + 1);
        const newEdge = component.state.factorGraph.edges[component.state.factorGraph.edges.length - 1];
        expect(newEdge.from).toEqual(report.occurrence);
        expect(newEdge.to).toEqual(component.state.factorGraph.nodes[component.state.factorGraph.nodes.length - 1]);
        expect(newEdge.linkType).toEqual(Vocabulary.HAS_PART);
    });

    it('prevents setting end time before start time', () => {
        const toEdit = report.factorGraph.nodes[Generator.getRandomPositiveInt(1, report.factorGraph.nodes.length)],
            update = assign({}, toEdit),
            component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                               rootAttribute='occurrence'/>);
        component._onEditClick(toEdit);
        update.endTime = update.startTime - 1000;
        component._onEditFinish(update);
        expect(component.state.factorGraph.nodes.indexOf(toEdit)).not.toEqual(-1);
        expect(component.state.factorGraph.nodes.indexOf(update)).toEqual(-1);
    });

    it('updates start time of ancestors when the updated event start time exceeds the parent\'s start time', () => {
        const toEdit = report.factorGraph.nodes[report.factorGraph.nodes.length - 1],
            parent = report.factorGraph.nodes[1],
            root = report.factorGraph.nodes[0],
            update = assign({}, toEdit);
        report.factorGraph.edges.push({
            from: root,
            to: parent,
            linkType: Vocabulary.HAS_PART
        }, {
            from: parent,
            to: toEdit,
            linkType: Vocabulary.HAS_PART
        });
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                                 rootAttribute='occurrence'/>);
        component._onEditClick(toEdit);
        update.startTime = root.startTime - 100000;
        component._onEditFinish(update);
        const newNodes = component.state.factorGraph.nodes;
        expect(newNodes[0].startTime).toEqual(update.startTime);
        expect(newNodes[1].startTime).toEqual(update.startTime);
    });

    it('updates end time of ancestors when the updated event end time exceeds the parent\'s end time', () => {
        const toEdit = report.factorGraph.nodes[report.factorGraph.nodes.length - 1],
            parent = report.factorGraph.nodes[1],
            root = report.factorGraph.nodes[0],
            update = assign({}, toEdit);
        report.factorGraph.edges.push({
            from: root,
            to: parent,
            linkType: Vocabulary.HAS_PART
        }, {
            from: parent,
            to: toEdit,
            linkType: Vocabulary.HAS_PART
        });
        const component = Environment.render(<SmallScreenFactors report={report} onChange={onChange}
                                                                 rootAttribute='occurrence'/>);
        component._onEditClick(toEdit);
        update.endTime = root.endTime + 100000;
        component._onEditFinish(update);
        const newNodes = component.state.factorGraph.nodes;
        expect(newNodes[0].endTime).toEqual(update.endTime);
        expect(newNodes[1].endTime).toEqual(update.endTime);
    });

    // TODO When the root element (occurrence) is updated, it should be replaced also in the report
    // TODO If the user shrinks parent of some subevent, it should be shrunk too
});
