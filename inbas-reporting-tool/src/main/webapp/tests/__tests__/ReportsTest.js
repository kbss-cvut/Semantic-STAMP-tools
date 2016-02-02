'use strict';

describe('Reports', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),

        Reports = require('../../js/components/reports/Reports'),
        ReportsFilter = require('../../js/components/reports/ReportsFilter'),
        en = require('../../js/i18n/en'),

        actions = jasmine.createSpyObj('actions', ['onFilterChange']);

    it('shows message informing that there are no matching reports when filter finds no reports.', function () {
        var result = Environment.render(<Reports reports={[]} actions={actions} filter={{}}/>),
            filter = TestUtils.scryRenderedComponentsWithType(result, ReportsFilter.wrappedComponent),
            message = Environment.getComponentByTagAndText(result, 'div', en.messages['reports.filter.no-matching-found']);

        expect(filter.length).toEqual(1);
        expect(message).not.toBeNull();
    });

    it('shows message with a link to dashboard when no reports exist.', function () {
        var result = Environment.render(<Reports reports={[]} actions={actions}/>),
            filter = TestUtils.scryRenderedComponentsWithType(result, ReportsFilter.wrappedComponent),
            message = Environment.getComponentByTagAndText(result, 'div', en.messages['reports.no-reports'] + en.messages['reports.no-reports.link']),
            link = Environment.getComponentByTagAndText(result, 'a', en.messages['reports.no-reports.link']);

        expect(filter.length).toEqual(1);
        expect(message).not.toBeNull();
        expect(link).not.toBeNull();
    });
});
