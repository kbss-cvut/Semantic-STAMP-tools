'use strict';

describe('Investigation', function () {

    var React = require('react'),
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator'),
        Actions = require('../../js/actions/Actions'),
        Investigation = rewire('../../js/components/investigation/Investigation'),
        handlers,
        investigation;

    beforeEach(function () {
        spyOn(Actions, 'updateReport');
        spyOn(Actions, 'loadOptions');
        spyOn(Actions, 'loadOccurrenceCategories');
        handlers = jasmine.createSpyObj('handlers', ['onCancel', 'onSuccess', 'onChange']);
        Environment.mockFactors(Investigation);
        investigation = Generator.generateInvestigation();
    });

    it('Gets factor hierarchy and links on submit', function () {
        var component = Environment.render(<Investigation investigation={investigation} handlers={handlers}/>),
            saveEvent = {
                preventDefault: function () {
                }
            },
            FactorJsonSerializer = Investigation.__get__('Factors').__get__('FactorJsonSerializer');
        component.onSave(saveEvent);
        expect(FactorJsonSerializer.getFactorHierarchy).toHaveBeenCalled();
        expect(FactorJsonSerializer.getLinks).toHaveBeenCalled();
    });
});