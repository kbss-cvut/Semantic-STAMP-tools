'use strict';

import React from "react";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import FindingMeasures from "../../js/components/report/audit/FindingMeasures";

describe('FindingMeasures', () => {

    var measures,
        onChange;

    beforeEach(() => {
        onChange = jasmine.createSpy('onChange');
        measures = Generator.generateCorrectiveMeasures();
    });

    it('removes corrective measure from measures on delete', () => {
        var toRemove = measures[Generator.getRandomPositiveInt(0, measures.length)],
            component = Environment.render(<FindingMeasures onChange={onChange} correctiveMeasures={measures}/>);
        component.getWrappedComponent()._onDeleteMeasure(toRemove);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.correctiveMeasures.length).toEqual(measures.length - 1);
        expect(change.correctiveMeasures.indexOf(toRemove)).toEqual(-1);
    });

    it('updates measure on edit finish', () => {
        var index = Generator.getRandomPositiveInt(0, measures.length),
            toEdit = measures[index],
            component = Environment.render(<FindingMeasures onChange={onChange} correctiveMeasures={measures}/>);
        toEdit.description = 'Updated description';
        component.getWrappedComponent()._onEditFinished(toEdit);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.correctiveMeasures.length).toEqual(measures.length);
        expect(change.correctiveMeasures[index].description).toEqual(toEdit.description);
    });

    it('appends new measure to the end of measures on edit finish', () => {
        var newMeasure = {
                deadline: Date.now(),
                isNew: true,
                description: 'New corrective measure'
            },
            component = Environment.render(<FindingMeasures onChange={onChange} correctiveMeasures={measures}/>);
        component.getWrappedComponent()._onEditFinished(newMeasure);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.correctiveMeasures.length).toEqual(measures.length + 1);
        expect(change.correctiveMeasures[change.correctiveMeasures.length - 1]).toEqual(newMeasure);
    });

    it('adds first measure on edit finish', () => {
        var newMeasure = {
                deadline: Date.now(),
                isNew: true,
                description: 'New corrective measure'
            },
            component = Environment.render(<FindingMeasures onChange={onChange} correctiveMeasures={null}/>);
        component.getWrappedComponent()._onEditFinished(newMeasure);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.correctiveMeasures.length).toEqual(1);
        expect(change.correctiveMeasures[0]).toEqual(newMeasure);
    });
});
