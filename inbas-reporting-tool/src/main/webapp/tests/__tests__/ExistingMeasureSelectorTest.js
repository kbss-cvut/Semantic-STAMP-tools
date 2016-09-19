'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import ExistingMeasureSelector from "../../js/components/report/audit/ExistingMeasureSelector";

describe('Existing measure selector', () => {

    var onChange;

    beforeEach(() => {
        onChange = jasmine.createSpy('onChange');
    });

    it('resolves measures from findings except the current one.', () => {
        var audit = generateAuditWithFindingsAndMeasures(),
            finding = audit.findings[Generator.getRandomInt(audit.findings.length)],

            component = Environment.render(<ExistingMeasureSelector onChange={onChange} audit={audit}
                                                                    finding={finding}/>);

        var result = component._getMeasuresForSelect(),
            shouldContain, f;
        for (var i = 0, len = audit.findings.length; i < len; i++) {
            f = audit.findings[i];
            shouldContain = f !== finding;
            for (var j = 0, lenn = f.correctiveMeasures.length; j < lenn; j++) {
                if (shouldContain) {
                    expect(result.indexOf(f.correctiveMeasures[j])).not.toEqual(-1);
                } else {
                    expect(result.indexOf(f.correctiveMeasures[j])).toEqual(-1);
                }
            }
        }
    });

    function generateAuditWithFindingsAndMeasures() {
        var audit = {},
            findings = [];
        for (var i = 0, cnt = Generator.getRandomPositiveInt(2, 5); i < cnt; i++) {
            findings.push({
                correctiveMeasures: Generator.generateCorrectiveMeasures()
            });
        }
        audit.findings = findings;
        return audit;
    }

    it('calls onChange with selected corrective measure when an option is selected', () => {
        var audit = generateAuditWithFindingsAndMeasures(),
            finding = audit.findings[Generator.getRandomInt(audit.findings.length)],

            component = Environment.render(<ExistingMeasureSelector onChange={onChange} audit={audit}
                                                                    finding={finding}/>);
        var select = TestUtils.findRenderedDOMComponentWithTag(component, 'select'),
            options = TestUtils.scryRenderedDOMComponentsWithTag(component, 'option'),
            selectedIndex = Generator.getRandomPositiveInt(0, options.length),
            selectedOption = options[selectedIndex + 1];    // There is one extra option - the --Select-- one
        TestUtils.Simulate.change(select, {target: {value: selectedOption.value}});
        expect(onChange).toHaveBeenCalledWith(component.measures[selectedIndex]);
    });
});
