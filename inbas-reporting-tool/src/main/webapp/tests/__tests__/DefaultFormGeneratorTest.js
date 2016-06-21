'use strict';

describe('Default form generator', () => {

    var Constants = require('../../js/constants/Constants'),
        DefaultFormGenerator = require('../../js/model/DefaultFormGenerator'),
        WizardGenerator = require('../../js/components/wizard/generator/WizardGenerator'),
        WizardStore = require('../../js/stores/WizardStore');

    beforeEach(() => {
        spyOn(WizardStore, 'initWizard');
    });

    it('generates empty one-step wizard as a default form', () => {
        var form = DefaultFormGenerator.generateForm(),
            wizardSteps = WizardGenerator._constructWizardSteps(form);

        expect(wizardSteps.length).toEqual(1);
        expect(WizardStore.initWizard).toHaveBeenCalledWith(null, [form['@graph'][0][Constants.FORM.HAS_SUBQUESTION][0]]);
    });
});
