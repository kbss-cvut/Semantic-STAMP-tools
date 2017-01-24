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
        spyOn(Actions, 'loadOptions');
    });

    it('does not render line for the occurrence', () => {
        report.factorGraph = {
            nodes: [
                report.occurrence,
                {
                    uri: Generator.getRandomUri(),
                    eventType: Generator.randomCategory().id
                }
            ]
        };
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
});
