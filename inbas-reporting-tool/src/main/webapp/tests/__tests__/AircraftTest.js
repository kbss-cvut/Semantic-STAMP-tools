'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Actions from "../../js/actions/Actions";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import Aircraft from "../../js/components/report/occurrence/Aircraft";
import OptionsStore from "../../js/stores/OptionsStore";
import Vocabulary from "../../js/constants/Vocabulary";

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
        spyOn(OptionsStore, 'getOptions').and.returnValue(Generator.getCategories());
        const component = Environment.render(<Aircraft onChange={onChange} aircraft={aircraft}/>);
        expect(component.state.aircraftPresent).toBeTruthy();
        const aircraftToggle = TestUtils.findRenderedComponentWithType(component, require('react-bootstrap').Checkbox);
        TestUtils.Simulate.change(TestUtils.findRenderedDOMComponentWithTag(aircraftToggle, 'input'));
        expect(onChange).toHaveBeenCalledWith({aircraft: null});
        expect(component.state.aircraftPresent).toBeFalsy();
    });

    it('replaces original aircraft type with newly selected one', () => {
        const originalCategory = Generator.getCategories()[0].id;
        let aircraft = {
            operator: {
                name: 'Test operator',
                uri: Generator.getRandomUri()
            },
            types: [originalCategory, Generator.getRandomUri()]
        };
        spyOn(OptionsStore, 'getOptions').and.returnValue(Generator.getCategories().map(item => {
            const res = {};
            res['@id'] = item.id;
            res[Vocabulary.RDFS_LABEL] = item.name;
            res[Vocabulary.RDFS_COMMENT] = item.description;
            return res;
        }));
        const component = Environment.render(<Aircraft onChange={onChange} aircraft={aircraft}/>);
        const anotherCategory = Generator.getCategories()[1];
        component._onAircraftTypeSelected(anotherCategory);
        expect(onChange).toHaveBeenCalled();
        const update = onChange.calls.argsFor(0)[0].aircraft;
        expect(update.types.indexOf(anotherCategory.id)).not.toEqual(-1);
        expect(update.types.indexOf(originalCategory)).toEqual(-1);
    });
});
