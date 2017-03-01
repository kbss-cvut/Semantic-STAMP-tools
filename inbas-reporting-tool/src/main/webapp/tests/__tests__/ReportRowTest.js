'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import {messages} from "../../js/i18n/en";
import ReportRow from "../../js/components/report/ReportRow";
import UserStore from "../../js/stores/UserStore";
import Vocabulary from "../../js/constants/Vocabulary";

describe('Report row', () => {

    let actions,
        report;

    beforeEach(() => {
        actions = jasmine.createSpyObj('actions', ['onEdit', 'onRemove']);
        report = Generator.generateReports()[0];
        Environment.mockCurrentUser();
    });

    it('renders report date correctly when it is set to 0', () => {
        report.date = 0;
        const component = Environment.renderIntoTable(<ReportRow actions={actions} report={report}/>);

        const cells = TestUtils.scryRenderedDOMComponentsWithClass(component, 'report-row'),
            dateCell = cells[1];
        expect(dateCell.textContent).toMatch(/01-01-70 0(0|1):00/);
    });

    it('renders all actions buttons for regular/admin user', () => {
        const component = Environment.renderIntoTable(<ReportRow actions={actions} report={report}/>),
            buttons = TestUtils.scryRenderedDOMComponentsWithTag(component, 'button');
        expect(buttons.length).toEqual(2);
        expect(buttons[0].textContent).toEqual(messages['open']);
        expect(buttons[1].textContent).toEqual(messages['delete']);
    });

    it('does not render delete button for guest user', () => {
        UserStore.getCurrentUser.and.returnValue({
            types: [Vocabulary.ROLE_GUEST]
        });
        const component = Environment.renderIntoTable(<ReportRow actions={actions} report={report}/>),
            buttons = TestUtils.scryRenderedDOMComponentsWithTag(component, 'button');
        expect(buttons.length).toEqual(1);
        expect(buttons[0].textContent).toEqual(messages['open']);
    });
});
