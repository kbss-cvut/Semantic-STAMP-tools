'use strict';

describe('Responsible department(s)', () => {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Button = require('react-bootstrap').Button,
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Typeahead = require('react-bootstrap-typeahead'),

        Department = require('../../js/components/report/occurrence/Department').default,
        report, onChange;

    beforeEach(() => {
        report = Generator.generateOccurrenceReport();
        onChange = jasmine.createSpy('onChange');
    });

    it('displays as many typeahead as there are departments in the report', () => {
        var cnt = Generator.getRandomPositiveInt(1, 5),
            result, typeaheads;
        generateDepartments(cnt);
        result = Environment.render(<Department report={report} onChange={onChange}/>);
        typeaheads = TestUtils.scryRenderedComponentsWithType(result, Typeahead);
        expect(typeaheads.length).toEqual(cnt);
    });

    function generateDepartments(cnt) {
        report.responsibleDepartments = [];
        for (var i = 0; i < cnt; i++) {
            report.responsibleDepartments.push(Generator.getRandomUri());
        }
    }

    it('displays one empty typeahead when there is no responsible department in the report', () => {
        var result = Environment.render(<Department report={report} onChange={onChange}/>),
            typeaheads = TestUtils.scryRenderedComponentsWithType(result, Typeahead);
        expect(typeaheads.length).toEqual(1);
        expect(typeaheads[0].props.value).toEqual('');
    });

    it('updates correct department URI when one of multiple is changed', () => {
        var cnt = 5,
            result, typeaheads, expected,
            index = Generator.getRandomPositiveInt(0, cnt),
            department = {
                id: Generator.getRandomUri(),
                name: 'Test'
            };
        generateDepartments(cnt);
        result = Environment.render(<Department report={report} onChange={onChange}/>);
        typeaheads = TestUtils.scryRenderedComponentsWithType(result, Typeahead);

        typeaheads[index].props.onOptionSelected(department);
        expected = report.responsibleDepartments.slice();
        expected[index] = department.id;
        expect(onChange).toHaveBeenCalledWith({responsibleDepartments: expected});
    });

    it('appends department URI when it was selected in the added typeahead', () => {
        var cnt = 5,
            result, typeaheads, expected, addButton,
            index = Generator.getRandomPositiveInt(0, cnt),
            department = {
                id: Generator.getRandomUri(),
                name: 'Test'
            };
        generateDepartments(cnt);
        result = Environment.render(<Department report={report} onChange={onChange}/>);
        addButton = Environment.getComponentByText(result, Button, 'Add');
        TestUtils.Simulate.click(addButton);
        typeaheads = TestUtils.scryRenderedComponentsWithType(result, Typeahead);
        typeaheads[typeaheads.length - 1].props.onOptionSelected(department);
        expected = report.responsibleDepartments.slice();
        expected.push(department.id);
        expect(onChange).toHaveBeenCalledWith({responsibleDepartments: expected});
    });

    it('renders additional typeahead when add is triggered', () => {
        var cnt = Generator.getRandomPositiveInt(1, 5),
            result, typeaheads, addButton;
        generateDepartments(cnt);
        result = Environment.render(<Department report={report} onChange={onChange}/>);
        typeaheads = TestUtils.scryRenderedComponentsWithType(result, Typeahead);
        expect(typeaheads.length).toEqual(cnt);
        addButton = Environment.getComponentByText(result, Button, 'Add');
        TestUtils.Simulate.click(addButton);
        typeaheads = TestUtils.scryRenderedComponentsWithType(result, Typeahead);
        expect(typeaheads.length).toEqual(cnt + 1);
    });
});
