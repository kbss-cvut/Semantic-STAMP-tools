'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Actions from "../../js/actions/Actions";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import Aircraft from "../../js/components/report/occurrence/Aircraft";

describe('Aircraft', () => {

    let onChange;

    beforeEach(() => {
        spyOn(Actions, 'loadOptions');
        onChange = jasmine.createSpy('onChange');
    });

    it('sets aircraft reference to null when existing aircraft is toggled off', () => {
        let aircraft = {
            operator: {
                name: 'Test operator',
                uri: Generator.getRandomUri()
            },
            types: [Generator.randomCategory().id]
        };
        const component = Environment.render(<Aircraft onChange={onChange} aircraft={aircraft}/>);
        expect(component.state.aircraftPresent).toBeTruthy();
        const aircraftToggle = TestUtils.findRenderedComponentWithType(component, require('react-bootstrap').Checkbox);
        TestUtils.Simulate.change(TestUtils.findRenderedDOMComponentWithTag(aircraftToggle, 'input'));
        expect(onChange).toHaveBeenCalledWith({aircraft: null});
        expect(component.state.aircraftPresent).toBeFalsy();
    });
});
