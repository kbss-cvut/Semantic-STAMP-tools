import React from "react";
import Environment from "../environment/Environment";

import {messages} from "../../js/i18n/en";
import WizardStep from "../../js/components/wizard/WizardStep";

describe("Wizard step", () => {

    class TestComponent extends React.Component {
        render() {
            return <div>test</div>;
        }
    }

    let onFinish;

    beforeEach(() => {
        onFinish = jasmine.createSpy('onFinish');
    });

    it('does not render Finish button for when read only is specified', () => {
        const component = Environment.render(<WizardStep isLastStep={true} component={TestComponent} readOnly={true}
                                                         stepIndex={0} onFinish={onFinish}/>),
            finishButton = Environment.getComponentByTagAndText(component, 'button', messages['wizard.finish']);
        expect(finishButton).toBeNull();
    });

    it('render Close instead of Cancel button when read only is specified', () => {
        const component = Environment.render(<WizardStep isLastStep={true} component={TestComponent} readOnly={true}
                                                         stepIndex={0} onFinish={onFinish}/>),
            cancelButton = Environment.getComponentByTagAndText(component, 'button', messages['cancel']),
            closeButton = Environment.getComponentByTagAndText(component, 'button', messages['close']);
        expect(cancelButton).toBeNull();
        expect(closeButton).not.toBeNull();
    });
});
