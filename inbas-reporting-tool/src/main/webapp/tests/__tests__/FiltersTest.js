'use strict';

import React from "react";
import TestUtils from "react-addons-test-utils";
import Actions from "../../js/actions/Actions";
import Constants from "../../js/constants/Constants";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import OptionsStore from "../../js/stores/OptionsStore";
import Vocabulary from "../../js/constants/Vocabulary";
import Filters from "../../js/components/filter/Filters";

describe('Filters', () => {

    var onChange, onResetFilters,
        reports, count;

    beforeEach(() => {
        onChange = jasmine.createSpy('onChange');
        onResetFilters = jasmine.createSpy('onResetFilters');
        reports = Generator.generateReports();
        count = reports.length;
        spyOn(Actions, 'loadOptions');
    });

    it('generates options for occurrence categories from categories existing in the data', () => {
        var categories = getCategoriesAsJsonLd();
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        var component = Environment.render(<Filters filters={{}} data={reports} onChange={onChange}
                                                    onResetFilters={onResetFilters}/>).getWrappedComponent(),
            options = component.state[Constants.FILTERS[0].path],
            categoriesUsed = getCategoriesUsedByReports(categories);

        expect(options.length).toEqual(categoriesUsed.length);
        for (var i = 0, len = categoriesUsed.length; i < len; i++) {
            expect(options[i].value).toEqual(categoriesUsed[i]['@id']);
            expect(options[i].label).toEqual(categoriesUsed[i][Vocabulary.RDFS_LABEL]);
            expect(options[i].title).toEqual(categoriesUsed[i][Vocabulary.RDFS_COMMENT]);
        }
    });

    function getCategoriesAsJsonLd() {
        var categories = Generator.getCategories();
        return categories.map(item => {
            var res = {};
            res['@id'] = item.id;
            res[Vocabulary.RDFS_LABEL] = item.name;
            res[Vocabulary.RDFS_COMMENT] = item.description;
            return res;
        });
    }

    function getCategoriesUsedByReports(categories) {
        var cat,
            categoriesUsed = [];
        for (var i = 0, len = categories.length; i < len; i++) {
            cat = categories[i];
            for (var j = 0; j < count; j++) {
                if (cat['@id'] === reports[j].occurrenceCategory && categoriesUsed.indexOf(cat) === -1) {
                    categoriesUsed.push(cat);
                    break;
                }
            }
        }
        return categoriesUsed;
    }

    it('renders select with options corresponding to all unique categories used by data', () => {
        var categories = getCategoriesAsJsonLd();
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        var component = Environment.render(<Filters filters={{}} data={reports} onChange={onChange}
                                                    onResetFilters={onResetFilters}/>).getWrappedComponent(),
            categoriesUsed = getCategoriesUsedByReports(categories);

        var options = TestUtils.scryRenderedDOMComponentsWithTag(component, 'option');
        expect(options.length).toEqual(categoriesUsed.length);
        for (var i = 0, len = options.length; i < len; i++) {
            expect(options[i].value).toEqual(categoriesUsed[i]['@id']);
            expect(options[i].textContent).toEqual(categoriesUsed[i][Vocabulary.RDFS_LABEL]);
            expect(options[i].title).toEqual(categoriesUsed[i][Vocabulary.RDFS_COMMENT]);
        }
    });

    it('sets value of a filter to the value specified in props', () => {
        var categories = getCategoriesAsJsonLd(),
            value = reports[Generator.getRandomInt(reports.length)].occurrenceCategory;
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        var component = Environment.render(<Filters filters={{occurrenceCategory: value}} data={reports}
                                                    onChange={onChange} onResetFilters={onResetFilters}/>),

            select = TestUtils.findRenderedDOMComponentWithTag(component, 'select');
        expect(select.value).toEqual(value);
    });

    it('notifies owner about change in the filter value', () => {
        var categories = getCategoriesAsJsonLd(),
            value = reports[Generator.getRandomInt(reports.length)].occurrenceCategory;
        spyOn(OptionsStore, 'getOptions').and.returnValue(categories);
        var component = Environment.render(<Filters filters={{}} data={reports}
                                                    onChange={onChange} onResetFilters={onResetFilters}/>),

            select = TestUtils.findRenderedDOMComponentWithTag(component, 'select');
        select.value = value;
        TestUtils.Simulate.change(select);
        expect(onChange).toHaveBeenCalledWith({occurrenceCategory: value});
    });
});
