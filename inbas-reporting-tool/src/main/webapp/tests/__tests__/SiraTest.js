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
        var safetyIssue = Generator.generateSafetyIssueReport().safetyIssue,
            TestParent = React.createClass({
                getInitialState: function () {
                    return {
                        safetyIssue: safetyIssue
                    }
                },
                render() {
                    return <Sira safetyIssue={this.state.safetyIssue} onChange={onChange}/>;
                }
            });
        var component = Environment.render(<TestParent/>),
            select = TestUtils.findRenderedDOMComponentWithTag(component, 'select');
        Object.getOwnPropertyNames(Constants.SIRA_COLORS).forEach(sira => {
            safetyIssue.sira = sira;
            component.setState({safetyIssue: safetyIssue});
            expect(select.className.indexOf(Constants.SIRA_COLORS[sira])).not.toEqual(-1);
        });
    });
});
