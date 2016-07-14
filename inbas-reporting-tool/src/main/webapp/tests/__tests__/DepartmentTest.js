'use strict';

describe('Responsible department(s)', () => {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Button = require('react-bootstrap').Button,
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Typeahead = require('react-bootstrap-typeahead'),
        Vocabulary = require('../../js/constants/Vocabulary'),

        OptionsStore = require('../../js/stores/OptionsStore'),
        Department = require('../../js/components/report/occurrence/Department').default,
        report, onChange, options;

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
        var i, len;
        report.responsibleDepartments = [];
        for (i = 0; i < cnt; i++) {
            report.responsibleDepartments.push(Generator.getRandomUri());
        }
        options = [];
        var opt;
        for (i = 0, len = report.responsibleDepartments.length; i < len; i++) {
            opt = {
                '@id': report.responsibleDepartments[i]
            };
            opt[Vocabulary.RDFS_LABEL] = 'Option ' + i;
            options.push(opt);
        }
        for (i = 0; i < 5; i++) {
            opt = {
                '@id': Generator.getRandomUri()
            };
            opt[Vocabulary.RDFS_LABEL] = 'AdditionalOption' + i;
            options.push(opt);
        }
        spyOn(OptionsStore, 'getOptions').and.callFake(() => {
            return options;
        });
    }

    it('displays one empty typeahead when there is no responsible department in the report', () => {
        generateDepartments(0);
        var result = Environment.render(<Department report={report} onChange={onChange}/>),
            typeaheads = TestUtils.scryRenderedComponentsWithType(result, Typeahead);
        expect(typeaheads.length).toEqual(1);
        expect(typeaheads[0].props.value).toEqual('');
    });

    it('updates correct department URI when one of multiple is changed', () => {
        var cnt = 5,
            result, typeaheads, expected,
            // index = Generator.getRandomPositiveInt(0, cnt),
            index = cnt - 1,
            department;
        generateDepartments(cnt);
        department = options[Generator.getRandomPositiveInt(0, options.length)];
        result = Environment.render(<Department report={report} onChange={onChange}/>);
        typeaheads = TestUtils.scryRenderedComponentsWithType(result, Typeahead);

        typeaheads[index].props.onOptionSelected({
            id: department['@id']
        });
        expected = report.responsibleDepartments.slice();
        expected[index] = department['@id'];
        expect(onChange).toHaveBeenCalledWith({responsibleDepartments: expected});
    });

    it('appends department URI when it was selected in the added typeahead', () => {
        var cnt = 5,
            result, typeaheads, expected, addButton,
            department;
        generateDepartments(cnt);
        department = options[Generator.getRandomPositiveInt(0, options.length)];
        result = Environment.render(<Department report={report} onChange={onChange}/>);
        addButton = Environment.getComponentByText(result, Button, 'Add');
        TestUtils.Simulate.click(addButton);
        typeaheads = TestUtils.scryRenderedComponentsWithType(result, Typeahead);
        typeaheads[typeaheads.length - 1].props.onOptionSelected({
            id: department['@id']
        });
        expected = report.responsibleDepartments.slice();
        expected.push(department['@id']);
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

    it('removes department when delete is triggered', () => {
        var cnt = Generator.getRandomPositiveInt(1, 5),
            index = Generator.getRandomPositiveInt(0, cnt),
            result, deleteButtons, expected;
        generateDepartments(cnt);
        result = Environment.render(<Department report={report} onChange={onChange}/>);
        deleteButtons = Environment.getComponentsByText(result, Button, 'Delete');
        TestUtils.Simulate.click(deleteButtons[index]);
        expected = report.responsibleDepartments.slice();
        expected.splice(index, 1);
        expect(onChange).toHaveBeenCalledWith({responsibleDepartments: expected});
    });
});
