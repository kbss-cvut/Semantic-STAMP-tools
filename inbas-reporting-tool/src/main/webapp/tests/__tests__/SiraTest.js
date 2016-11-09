'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Actions from "../../js/actions/Actions";
import Constants from "../../js/constants/Constants";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import OptionsStore from "../../js/stores/OptionsStore";
import Sira from "../../js/components/report/safetyissue/Sira";
import Vocabulary from "../../js/constants/Vocabulary";

describe('Sira', () => {

    var onChange,
        report,
        option;

    beforeEach(() => {
        onChange = jasmine.createSpy('onChange').and.callFake((change) => {
            Object.getOwnPropertyNames(change).forEach((item) => {
                report[item] = change[item];
            });
        });
        spyOn(Actions, 'loadOptions');
        spyOn(Actions, 'calculateSira');
        report = Generator.generateSafetyIssueReport();
        report.sira = {};
        option = {
            '@id': Generator.getRandomUri()
        };
        option[Vocabulary.RDFS_LABEL] = 'Random option';
    });

    it('resolves existing SIRA value label', () => {
        let siraValue = Object.getOwnPropertyNames(Constants.SIRA_COLORS)[0],
            option = {
                '@id': siraValue,
            };
        option[Vocabulary.RDFS_LABEL] = 'SIRA Value';
        report.sira.siraValue = siraValue;
        spyOn(OptionsStore, 'getOptions').and.returnValue([option]);

        let component = Environment.render(<Sira report={report} onChange={onChange}/>),
            result = TestUtils.findRenderedDOMComponentWithTag(component, 'input');
        expect(result.value).toEqual(option[Vocabulary.RDFS_LABEL]);
    });

    it('sets input class based on SIRA value', () => {
        let siraValue = Object.getOwnPropertyNames(Constants.SIRA_COLORS)[0],
            option = {
                '@id': siraValue,
            };
        option[Vocabulary.RDFS_LABEL] = 'SIRA Value';
        report.sira.siraValue = siraValue;
        let component = Environment.render(<Sira report={report} onChange={onChange}/>),
            result = TestUtils.findRenderedDOMComponentWithTag(component, 'input');
        expect(result.className.indexOf(Constants.SIRA_COLORS[siraValue])).not.toEqual(-1);
    });

    it('calculates SIRA value when all SIRA attributes are selected', () => {
        spyOn(OptionsStore, 'getOptions').and.returnValue([option]);
        let component = Environment.render(<Sira report={report} onChange={onChange}/>),
            selects = TestUtils.scryRenderedDOMComponentsWithTag(component, 'select');
        selects.forEach(s => {
            s.value = option['@id'];
            TestUtils.Simulate.change(s);
        });
        expect(Actions.calculateSira).toHaveBeenCalledWith(report.sira);
    });

    it('calculates SIRA only when all SIRA attributes are selected', () => {
        spyOn(OptionsStore, 'getOptions').and.returnValue([option]);
        let component = Environment.render(<Sira report={report} onChange={onChange}/>),
            selects = TestUtils.scryRenderedDOMComponentsWithTag(component, 'select');
        selects.forEach(s => {
            expect(Actions.calculateSira).not.toHaveBeenCalled();
            s.value = option['@id'];
            TestUtils.Simulate.change(s);
        });
        expect(Actions.calculateSira).toHaveBeenCalled();
    });

    it('renders reset button when sira value is set', () => {
        spyOn(OptionsStore, 'getOptions').and.returnValue([option]);
        report.sira.siraValue = option['@id'];
        let component = Environment.render(<Sira report={report} onChange={onChange}/>),
            resetButtons = TestUtils.scryRenderedDOMComponentsWithTag(component, 'button');
        expect(resetButtons.length).toEqual(1);
    });

    it('does not render reset button when sira value is not calculated', () => {
        spyOn(OptionsStore, 'getOptions').and.returnValue([option]);
        let component = Environment.render(<Sira report={report} onChange={onChange}/>),
            resetButtons = TestUtils.scryRenderedDOMComponentsWithTag(component, 'button');
        expect(resetButtons.length).toEqual(0);
    });

    it('resets SIRA value and attributes when reset button is clicked', () => {
        // TODO
        spyOn(OptionsStore, 'getOptions').and.returnValue([option]);
        report.sira.siraValue = option['@id'];
        report.sira.initialEventFrequency = option['@id'];
        report.sira.barrierUosAvoidanceFailFrequency = option['@id'];
        report.sira.barrierRecoveryFailFrequency = option['@id'];
        report.sira.accidentSeverity = option['@id'];
        let component = Environment.render(<Sira report={report} onChange={onChange}/>),
            resetButtons = TestUtils.scryRenderedDOMComponentsWithTag(component, 'button');
        TestUtils.Simulate.click(resetButtons[0]);

        expect(onChange).toHaveBeenCalled();
        let change = onChange.calls.argsFor(0)[0];
        expect(change.sira.siraValue).toBeNull();
        expect(change.sira.initialEventFrequency).toBeNull();
        expect(change.sira.barrierUosAvoidanceFailFrequency).toBeNull();
        expect(change.sira.barrierRecoveryFailFrequency).toBeNull();
        expect(change.sira.accidentSeverity).toBeNull();
    });
});
