'use strict';

xdescribe('Test factor tree hierarchy serialization for JSON', function() {

    var GanttController,
        FactorJsonSerializer,
        Generator = require('../environment/Generator').default;

    beforeEach(function() {
        GanttController = jasmine.createSpyObj('ganttController', ['getFactor', 'getChildren', 'getLinks']);
        FactorJsonSerializer = require('../../js/utils/FactorJsonSerializer');
        FactorJsonSerializer.setGanttController(GanttController);
        GanttController.getLinks.and.returnValue([]);
    });

    it('Serializes only the root when it has no children', function() {
        var root = Generator.generateFactors(Date.now(), Date.now(), 0);
        Generator.generateGanttTasksForFactors(root, GanttController);

        var result = FactorJsonSerializer.getFactorHierarchy();
        expect(result).toEqual(root);
    });

    it('Serializes a factor hierarchy with depth equal to 2', function() {
        var root = Generator.generateFactors(Date.now(), Date.now(), 2);
        Generator.generateGanttTasksForFactors(root, GanttController);

        var result = FactorJsonSerializer.getFactorHierarchy();
        expect(result).toEqual(root);
    });

    it('Omits links attribute if there are no links in the hierarchy', function() {
        var root = Generator.generateFactors(Date.now(), Date.now(), 2);
        Generator.generateGanttTasksForFactors(root, GanttController);

        var result = FactorJsonSerializer.getLinks();
        expect(result).toBeNull();
    });

    it('Transforms links from the gantt component into simple fields of causes and mitigates', function() {
        var root = Generator.generateFactors(Date.now(), Date.now(), 2);
        Generator.generateGanttTasksForFactors(root, GanttController);
        Generator.generateGanttLinksForFactors(root, GanttController);

        var result = FactorJsonSerializer.getLinks();
        expect(result).not.toBeNull();
        expect(result.causes.length).toEqual(2);
        expect(result.mitigates.length).toEqual(1);
        var causeOne = result.causes[0],
            causeTwo = result.causes[1],
            mitigate = result.mitigates[0];
        expect(causeOne.from).toEqual(root.children[0].referenceId);
        expect(causeOne.to).toEqual(root.children[1].referenceId);
        expect(causeTwo.from).toEqual(root.children[0].children[0].referenceId);
        expect(causeTwo.to).toEqual(root.children[0].children[1].referenceId);
        expect(mitigate.from).toEqual(root.children[1].children[0].referenceId);
        expect(mitigate.to).toEqual(root.children[1].children[1].referenceId);
    });
});
