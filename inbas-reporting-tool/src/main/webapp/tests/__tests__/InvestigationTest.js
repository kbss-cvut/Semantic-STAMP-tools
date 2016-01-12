'use strict';

describe('Investigation', function () {

    var React = require('react'),
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator'),
        Actions = require('../../js/actions/Actions'),
        Investigation = rewire('../../js/components/investigation/Investigation'),
        Factors = rewire('../../js/components/investigation/Factors'),
        GanttController, FactorRenderer, FactorJsonSerializer, handlers,
        investigation;

    beforeEach(function () {
        spyOn(Actions, 'updateInvestigation');
        spyOn(Actions, 'loadOccurrenceSeverityOptions');
        handlers = jasmine.createSpyObj('handlers', ['onCancel', 'onSuccess', 'onChange']);
        GanttController = jasmine.createSpyObj('GanttController', ['init', 'setScale', 'expandSubtree', 'updateOccurrenceEvent']);
        FactorRenderer = jasmine.createSpyObj('FactorRenderer', ['renderFactors']);
        FactorJsonSerializer = jasmine.createSpyObj('FactorJsonSerializer', ['getFactorHierarchy', 'getLinks', 'setGanttController']);
        Factors.__set__('GanttController', GanttController);
        Factors.__set__('FactorRenderer', FactorRenderer);
        Factors.__set__('FactorJsonSerializer', FactorJsonSerializer);
        Investigation.__set__('Factors', Factors);
        Investigation.__set__('Actions', Actions);
        investigation = Generator.generateInvestigation();
    });

    it('Gets factor hierarchy and links on submit', function () {
        var component = Environment.render(<Investigation investigation={investigation} handlers={handlers}/>),
            saveEvent = {
                preventDefault: function () {
                }
            };
        component.onSave(saveEvent);
        expect(FactorJsonSerializer.getFactorHierarchy).toHaveBeenCalled();
        expect(FactorJsonSerializer.getLinks).toHaveBeenCalled();
    });
});