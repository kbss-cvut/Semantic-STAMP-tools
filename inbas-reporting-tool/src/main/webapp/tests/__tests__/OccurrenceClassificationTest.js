'use strict';

describe('Occurrence classification', () => {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        OccurrenceClassification = require('../../js/components/report/occurrence/OccurrenceClassification'),
        OptionsStore = require('../../js/stores/OptionsStore'),
        JsonLdUtils = require('jsonld-utils').default,
        Actions = require('../../js/actions/Actions'),
        messages = require('../../js/i18n/en').messages,
        report, onChange;

    beforeEach(() => {
        report = {
            severityAssessment: 'http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-431/v-200',
            occurrence: {
                eventTypes: []
            }
        };
        onChange = jasmine.createSpy('onChange');
    });

    it('processes occurrence category options when getting them from store on init', () => {
        spyOn(OptionsStore, 'getOptions').and.returnValue(Generator.getJsonLdSample());
        spyOn(JsonLdUtils, 'processTypeaheadOptions').and.callThrough();
        Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>);

        expect(JsonLdUtils.processTypeaheadOptions).toHaveBeenCalled();
    });

    it('processes occurrence category options when triggered from store on load', () => {
        spyOn(OptionsStore, 'getOptions').and.returnValue([]);
        spyOn(JsonLdUtils, 'processTypeaheadOptions').and.callThrough();
        Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>);
        OptionsStore.trigger('occurrenceCategory', Generator.getJsonLdSample());

        expect(JsonLdUtils.processTypeaheadOptions).toHaveBeenCalledWith(Generator.getJsonLdSample());
    });

    it('renders neither secondary occurrence category button nor typeahead when primary category is not selected', () => {
        var component = Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>),

            button = Environment.getComponentByTagAndText(component, 'button', messages['occurrence.add-category']),
            typeaheads = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap-typeahead'));
        expect(button).toBeNull();
        expect(typeaheads.length).toEqual(1);   // Only the primary category
    });

    it('renders add secondary occurrence category button when primary category is present', () => {
        report.occurrence.eventTypes = [Generator.randomCategory()['id']];
        var component = Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>),

            button = Environment.getComponentByTagAndText(component, 'button', messages['occurrence.add-category']),
            typeaheads = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap-typeahead'));
        expect(button).not.toBeNull();
        expect(typeaheads.length).toEqual(1);   // Only the primary category
    });

    it('renders secondary occurrence category typeahead when occurrence has two event types', () => {
        report.occurrence.eventTypes = [Generator.randomCategory()['id'], Generator.randomCategory()['id']];
        var component = Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>),

            typeaheads = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap-typeahead'));
        expect(typeaheads.length).toEqual(2);
    });

    it('renders secondary occurrence category typeahead when add secondary category button is clicked', () => {
        report.occurrence.eventTypes = [Generator.randomCategory()['id']];
        var component = Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>),

            button = Environment.getComponentByTagAndText(component, 'button', messages['occurrence.add-category']);
        TestUtils.Simulate.click(button);

        var typeaheads = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap-typeahead'));
        expect(typeaheads.length).toEqual(2);
    });

    it('adds secondary category when it is selected', () => {
        report.occurrence.eventTypes = [Generator.randomCategory()['id']];
        var category = Generator.randomCategory(),
            component = Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>),

            button = Environment.getComponentByTagAndText(component, 'button', messages['occurrence.add-category']);
        TestUtils.Simulate.click(button);
        component.onCategorySelect(category, 1);
        var change = onChange.calls.argsFor(0)[0];
        expect(change.occurrence.eventTypes.length).toEqual(2);
        expect(change.occurrence.eventTypes.indexOf(category['id'])).not.toEqual(-1);
    });

    it('removes secondary category when remove is clicked', () => {
        report.occurrence.eventTypes = [Generator.randomCategory()['id'], Generator.getRandomUri()];
        var toRemove = report.occurrence.eventTypes[1], // The second one will be removed
            component = Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>),

            removeButton = TestUtils.findRenderedDOMComponentWithClass(component, 'in-input-line');
        TestUtils.Simulate.click(removeButton);
        var change = onChange.calls.argsFor(0)[0];
        expect(change.occurrence.eventTypes.length).toEqual(1);
        expect(change.occurrence.eventTypes.indexOf(toRemove)).toEqual(-1);
    });

    it('renders secondary occurrence category button when secondary category was removed', () => {
        report.occurrence.eventTypes = [Generator.randomCategory()['id'], Generator.randomCategory()['id']];
        var toRemove = report.occurrence.eventTypes[1], // The second one will be removed
            component = Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>),

            removeButton = TestUtils.findRenderedDOMComponentWithClass(component, 'in-input-line');
        TestUtils.Simulate.click(removeButton);

        var button = Environment.getComponentByTagAndText(component, 'button', messages['occurrence.add-category']);
        expect(button).not.toBeNull();
    })
});
