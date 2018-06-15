import React from "react";
import TestUtils from "react-addons-test-utils";
import Actions from "../../js/actions/Actions";
import Constants from "../../js/constants/Constants";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import {messages} from "../../js/i18n/en";
import OptionsStore from "../../js/stores/OptionsStore";
import Vocabulary from "../../js/constants/Vocabulary";
import Filters from "../../js/components/filter/Filters";

describe('Filters', () => {

    let onChange, onResetFilters,
        reports, count;

    beforeEach(() => {
        onChange = jasmine.createSpy('onChange');
        onResetFilters = jasmine.createSpy('onResetFilters');
        reports = Generator.generateReports();
        count = reports.length;
        spyOn(Actions, 'loadOptions');
    });

    it('adds default filter option - any - to the rendered select', () => {
        const categories = getCategoriesAsJsonLd();
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        const component = Environment.render(<Filters filters={{}} data={reports} onChange={onChange}
                                                    onResetFilters={onResetFilters}/>),
            options = component.state[Constants.FILTERS[0].options];

        const defaultOption = options[0];
        expect(defaultOption.value).toEqual(Constants.FILTER_DEFAULT);
        expect(defaultOption.label).toEqual(messages['reports.filter.type.all']);
    });

    it('generates options for occurrence categories from categories existing in the data', () => {
        const categories = getCategoriesAsJsonLd();
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        const component = Environment.render(<Filters filters={{}} data={reports} onChange={onChange}
                                                    onResetFilters={onResetFilters}/>),
            options = component.state[Constants.FILTERS[0].options],
            categoriesUsed = getCategoriesUsedByReports(categories);

        expect(options.length).toEqual(categoriesUsed.length + 1);
        for (let i = 0, len = categoriesUsed.length; i < len; i++) {
            expect(options[i + 1].value).toEqual(categoriesUsed[i]['@id']);
            expect(options[i + 1].label).toEqual(categoriesUsed[i][Vocabulary.RDFS_LABEL]);
            expect(options[i + 1].title).toEqual(categoriesUsed[i][Vocabulary.RDFS_COMMENT]);
        }
    });

    function getCategoriesAsJsonLd() {
        const categories = Generator.getCategories();
        return categories.map(item => {
            const res = {};
            res['@id'] = item.id;
            res[Vocabulary.RDFS_LABEL] = item.name;
            res[Vocabulary.RDFS_COMMENT] = item.description;
            return res;
        });
    }

    function getCategoriesUsedByReports(categories) {
        let cat,
            categoriesUsed = [];
        for (let i = 0, len = categories.length; i < len; i++) {
            cat = categories[i];
            for (let j = 0; j < count; j++) {
                if (categoriesUsed.indexOf(cat) === -1 &&
                    (cat['@id'] === reports[j].occurrenceCategory || Array.isArray(reports[j].occurrenceCategory) &&
                    reports[j].occurrenceCategory.indexOf(cat['@id']) !== -1)) {
                    categoriesUsed.push(cat);
                    break;
                }
            }
        }
        return categoriesUsed;
    }

    it('generates options for occurrence categories existing in data when multiple categories per record exist', () => {
        let multipleCats = false;
        for (let i = 0, len = reports.length; i < len; i++) {
            if (!multipleCats || Generator.getRandomBoolean()) {
                reports[i].occurrenceCategory = [reports[i].occurrenceCategory, Generator.randomCategory().id];
                multipleCats = true;
            }
        }
        const categories = getCategoriesAsJsonLd();
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        const component = Environment.render(<Filters filters={{}} data={reports} onChange={onChange}
                                                    onResetFilters={onResetFilters}/>),
            options = component.state[Constants.FILTERS[0].options],
            categoriesUsed = getCategoriesUsedByReports(categories);

        expect(options.length).toEqual(categoriesUsed.length + 1);
        let found;
        for (let i = 0, len = categoriesUsed.length; i < len; i++) {
            found = false;
            for (let j = 1, lenn = options.length; j < lenn; j++) {
                if (categoriesUsed[i]['@id'] === options[j].value) {
                    expect(options[j].label).toEqual(categoriesUsed[i][Vocabulary.RDFS_LABEL]);
                    expect(options[j].title).toEqual(categoriesUsed[i][Vocabulary.RDFS_COMMENT]);
                    found = true;
                    break;
                }
            }
            expect(found).toBeTruthy();
        }
    });

    it('renders select with options corresponding to all unique categories used by data', () => {
        const categories = getCategoriesAsJsonLd();
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        const component = Environment.render(<Filters filters={{}} data={reports} onChange={onChange}
                                                    onResetFilters={onResetFilters}/>),
            categoriesUsed = getCategoriesUsedByReports(categories);

        const select = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Select'))[0],
            options = TestUtils.scryRenderedDOMComponentsWithTag(select, 'option');
        expect(options.length).toEqual(categoriesUsed.length + 1);
        for (let i = 0, len = categoriesUsed.length; i < len; i++) {
            expect(options[i + 1].value).toEqual(categoriesUsed[i]['@id']);
            expect(options[i + 1].textContent).toEqual(categoriesUsed[i][Vocabulary.RDFS_LABEL]);
            expect(options[i + 1].title).toEqual(categoriesUsed[i][Vocabulary.RDFS_COMMENT]);
        }
    });

    it('sets value of a filter to the value specified in props', () => {
        const categories = getCategoriesAsJsonLd(),
            value = reports[Generator.getRandomInt(reports.length)].occurrenceCategory;
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        const component = Environment.render(<Filters filters={{occurrenceCategory: value}} data={reports}
                                                    onChange={onChange} onResetFilters={onResetFilters}/>),

            select = TestUtils.scryRenderedDOMComponentsWithTag(component, 'select')[0];
        expect(select.value).toEqual(value);
    });

    it('notifies owner about change in the filter value', () => {
        const categories = getCategoriesAsJsonLd(),
            value = reports[Generator.getRandomInt(reports.length)].occurrenceCategory;
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        const component = Environment.render(<Filters filters={{}} data={reports}
                                                    onChange={onChange} onResetFilters={onResetFilters}/>),

            select = TestUtils.scryRenderedDOMComponentsWithTag(component, 'select')[0];
        select.value = value;
        TestUtils.Simulate.change(select);
        expect(onChange).toHaveBeenCalledWith({occurrenceCategory: value});
    });

    it('does not render filter if there is only one value', () => {
        const categories = getCategoriesAsJsonLd();
        reports.forEach(r => r.phase = 'http://onto.fel.cvut.cz/ontologies/documentation/processed');
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        const component = Environment.render(<Filters filters={{}} data={reports}
                                                      onChange={onChange} onResetFilters={onResetFilters}/>),
            selects = TestUtils.scryRenderedDOMComponentsWithTag(component, 'select');
        expect(selects.length).toEqual(1);
        expect(selects[0].name).toEqual(Constants.FILTERS[0].path);
    });
});
