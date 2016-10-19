'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Actions from "../../js/actions/Actions";
import Constants from "../../js/constants/Constants";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import Sira from "../../js/components/report/safetyissue/Sira";

describe('Sira', () => {

    var onChange;

    beforeEach(() => {
        onChange = jasmine.createSpy('onChange');
        spyOn(Actions, 'loadOptions');
    });

    it('sets select CSS class based on the selected SIRA value', () => {
        var report = Generator.generateSafetyIssueReport(),
            TestParent = React.createClass({
                getInitialState: function () {
                    return {
                        report: report
                    }
                },
                render() {
                    return <Sira report={this.state.report} onChange={onChange}/>;
                }
            });
        var component = Environment.render(<TestParent/>),
            select = TestUtils.findRenderedDOMComponentWithTag(component, 'select');
        Object.getOwnPropertyNames(Constants.SIRA_COLORS).forEach(sira => {
            report.sira = sira;
            component.setState({report: report});
            expect(select.className.indexOf(Constants.SIRA_COLORS[sira])).not.toEqual(-1);
        });
    });
});
