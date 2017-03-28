'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Constants from "../../js/constants/Constants";
import Environment from "../environment/Environment";

import InitialReportImport from "../../js/components/report/initial/InitialReportImport";

describe('Initial report import', () => {

    let onImportFinish, onCancel;

    beforeEach(() => {
        onImportFinish = jasmine.createSpy('onImportFinish');
        onCancel = jasmine.createSpy('onCancel');
    });

    it('passes new occurrence report instance containing imported initial report to onImportFinish', () => {
        const component = Environment.render(<InitialReportImport onImportFinish={onImportFinish}
                                                                  onCancel={onCancel}/>),
            text = 'Initial report text';
        const input = TestUtils.findRenderedDOMComponentWithTag(component.modalBody, 'textarea');
        input.value = text;
        TestUtils.Simulate.change(input, {target: input});
        const finishButton = TestUtils.scryRenderedDOMComponentsWithTag(component.modalFooter, 'button')[0];
        TestUtils.Simulate.click(finishButton);
        expect(onImportFinish).toHaveBeenCalled();
        const report = onImportFinish.calls.argsFor(0)[0];
        expect(report.javaClass).toEqual(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
        expect(report.initialReport).toBeDefined();
        expect(report.initialReport.description).toEqual(text);
    });
});
