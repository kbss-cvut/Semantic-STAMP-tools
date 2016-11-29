'use strict';

describe('OccurrenceReport', function () {

    const React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Actions = require('../../js/actions/Actions'),
        ReportFactory = require('../../js/model/ReportFactory'),
        OccurrenceReport = rewire('../../js/components/report/occurrence/OccurrenceReport'),
        messages = require('../../js/i18n/en').messages;
    let handlers,
        report;

    beforeEach(function () {
        spyOn(Actions, 'updateReport');
        spyOn(Actions, 'loadOptions');
        handlers = jasmine.createSpyObj('handlers', ['onCancel', 'onSuccess', 'onChange']);
        Environment.mockFactors(OccurrenceReport);
        report = Generator.generateOccurrenceReport();
    });

    it('Gets factor graph on submit', () => {
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>),
            saveEvent = {
                preventDefault: function () {
                }
            },
            FactorJsonSerializer = OccurrenceReport.__get__('Factors').__get__('FactorJsonSerializer');
        component.onSave(saveEvent);
        expect(FactorJsonSerializer.getFactorGraph).toHaveBeenCalled();
    });

    it('calls createReport when new report is saved', () => {
        report.isNew = true;
        spyOn(Actions, 'createReport');
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>),
            saveEvent = {
                preventDefault: function () {
                }
            },
            FactorJsonSerializer = OccurrenceReport.__get__('Factors').__get__('FactorJsonSerializer');
        component.onSave(saveEvent);

        expect(Actions.createReport).toHaveBeenCalled();
    });

    it('calls updateReport when an existing report is saved', () => {
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>),
            saveEvent = {
                preventDefault: function () {
                }
            },
            FactorJsonSerializer = OccurrenceReport.__get__('Factors').__get__('FactorJsonSerializer');
        component.onSave(saveEvent);

        expect(Actions.updateReport).toHaveBeenCalled();
    });

    it('does not display report file number when it is not defined (e.g. for new reports.)', () => {
        report = ReportFactory.createOccurrenceReport();
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>);
        expect(Environment.getComponentByTagAndContainedText(component, 'h3', messages['fileNo'])).toBeNull();
    });

    it('does not display \'Create new revision\' button for new reports', () => {
        report = ReportFactory.createOccurrenceReport();
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>);
        expect(Environment.getComponentByTagAndText(component, 'a', messages['detail.submit'])).toBeNull();
    });

    it('does not render the ECCAIRS report button for regular occurrence reports', () => {
        report = ReportFactory.createOccurrenceReport();
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>),
            topRightButtons = TestUtils.scryRenderedDOMComponentsWithClass(component, 'detail-top-button');
        expect(topRightButtons.length).toEqual(1);
    });

    it('renders ECCAIRS report button when displayed report was imported from ECCAIRS', () => {
        report = ReportFactory.createOccurrenceReport();
        report.isEccairsReport = function () {
            return true;
        };
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>),
            topRightButtons = TestUtils.scryRenderedDOMComponentsWithClass(component, 'detail-top-button');
        expect(topRightButtons.length).toEqual(2);
    });

    it('does not render the create new revision from the latest ECCAIRS report action item when report was not imported from ECCAIRS', () => {
        report = ReportFactory.createOccurrenceReport();
        report.isEccairsReport = function () {
            return false;
        };
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>),
            menuItems = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap').MenuItem),
            revFromEccairs = menuItems.find(item => item.props.onClick === component._onNewRevisionForEccairs);
        expect(revFromEccairs).not.toBeDefined();
    });

    it('renders the create new revision from the latest ECCAIRS report action item when report was imported from ECCAIRS', () => {
        report = ReportFactory.createOccurrenceReport();
        report.isEccairsReport = function () {
            return true;
        };
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>),
            menuItems = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap').MenuItem),
            revFromEccairs = menuItems.find(item => item.props.onClick === component._onNewRevisionForEccairs);
        expect(revFromEccairs).toBeDefined();
    });

    it('renders info about report being ECCAIRS in the panel header', () => {
        report = ReportFactory.createOccurrenceReport();
        report.isEccairsReport = function () {
            return true;
        };
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>),
            header = TestUtils.findRenderedDOMComponentWithTag(component, 'h2');
        expect(header.textContent.indexOf(messages['report.eccairs.label'])).not.toEqual(-1);
    });

    it('loading disables all bottom action buttons', () => {
        const component = Environment.render(<OccurrenceReport report={report} handlers={handlers}/>);
        component.onLoading();
        const toolbars = TestUtils.scryRenderedComponentsWithType(component, require('react-bootstrap').ButtonToolbar),
            toolbar = toolbars[toolbars.length - 1],
            buttons = TestUtils.scryRenderedDOMComponentsWithTag(toolbar, 'button');
        buttons.forEach(b => expect(b.disabled).toBeTruthy());
    });
});
