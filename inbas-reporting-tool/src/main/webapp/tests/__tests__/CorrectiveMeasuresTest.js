'use strict';

import React from "react";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import CorrectiveMeasures from "../../js/components/correctivemeasure/CorrectiveMeasures";

describe('CorrectiveMeasures', () => {
    var measures,
        onChange,
        report;

    beforeEach(() => {
        onChange = jasmine.createSpy('onChange');
        measures = [];
        for (var i = 0; i < Generator.getRandomPositiveInt(2, 10); i++) {
            measures.push({
                uri: Generator.getRandomUri(),
                description: 'Corrective measure ' + i,
                deadline: Date.now(),
                implemented: Generator.getRandomBoolean()
            });
        }
        report = {
            correctiveMeasures: measures
        }
    });

    it('removes corrective measure from measures on delete', () => {
        var indexToRemove = Generator.getRandomPositiveInt(0, measures.length),
            toRemove = measures[indexToRemove],
            component = Environment.render(<CorrectiveMeasures onChange={onChange} report={report}/>);
        component.getWrappedComponent().onRemove(indexToRemove);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.correctiveMeasures.length).toEqual(measures.length - 1);
        expect(change.correctiveMeasures.indexOf(toRemove)).toEqual(-1);
    });

    it('updates measure on edit finish', () => {
        var index = Generator.getRandomPositiveInt(0, measures.length),
            toEdit = measures[index],
            component = Environment.render(<CorrectiveMeasures onChange={onChange} report={report}/>);
        toEdit.description = 'Updated description';
        component.getWrappedComponent().updateCorrectiveMeasure(toEdit);
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
            component = Environment.render(<CorrectiveMeasures onChange={onChange} report={report}/>);
        component.getWrappedComponent().updateCorrectiveMeasure(newMeasure);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.correctiveMeasures.length).toEqual(measures.length + 1);
        expect(change.correctiveMeasures[change.correctiveMeasures.length - 1]).toEqual(newMeasure);
    });

    it('adds first measure on edit finish', () => {
        delete report.correctiveMeasures;
        var newMeasure = {
                deadline: Date.now(),
                isNew: true,
                description: 'New corrective measure'
            },
            component = Environment.render(<CorrectiveMeasures onChange={onChange} report={report}/>);
        component.getWrappedComponent().updateCorrectiveMeasure(newMeasure);
        expect(onChange).toHaveBeenCalled();
        var change = onChange.calls.argsFor(0)[0];
        expect(change.correctiveMeasures.length).toEqual(1);
        expect(change.correctiveMeasures[0]).toEqual(newMeasure);
    });
});
