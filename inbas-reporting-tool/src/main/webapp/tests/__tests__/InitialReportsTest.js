'use strict';

describe('InitialReports component tests', function () {

    var React = require('react'),
        ReactDOM = require('react-dom'),
        TestUtils = require('react-addons-test-utils'),
        InitialReports = require('../../js/components/initialreport/InitialReports');

    it('Opens initial report add dialog on Add button click', function () {
        var reports = TestUtils.renderIntoDocument(<InitialReports report={{}} onAttributeChange={function() {}}/>);
        var addButton = ReactDOM.findDOMNode(reports.refs.addInitialReport);
        var wizard = reports.refs.initialReportWizard;
        expect(wizard.props.show).toBeFalsy();
        TestUtils.Simulate.click(addButton);
        expect(wizard.props.show).toBeTruthy();
    });

    it('Adds new initial report when wizard is submitted', function () {
        var callbacks = jasmine.createSpyObj('callbacks', ['onAttributeChange', 'onClose']);
        var reports = TestUtils.renderIntoDocument(<InitialReports report={{initialReports: []}}
                                                                   onAttributeChange={callbacks.onAttributeChange}/>),
            initialReportText = 'Test',
            initialReport = {text: initialReportText};

        reports.saveNewInitialReport({initialReport: initialReport}, callbacks.onClose);
        expect(callbacks.onAttributeChange).toHaveBeenCalledWith('initialReports', [initialReport]);
        expect(callbacks.onClose).toHaveBeenCalled();
    });

    it('Updates initial reports when editing one is finished', function () {
        var callbacks = jasmine.createSpyObj('callbacks', ['onAttributeChange', 'onClose']);
        var initialReports = [{text: 'Initial text'}];
        var reports = TestUtils.renderIntoDocument(<InitialReports report={{initialReports: initialReports}}
                                                                   onAttributeChange={callbacks.onAttributeChange}/>),
            updatedText = 'Updated text',
            initialReport = {text: updatedText};
        reports.state.editedInitialReportIndex = 0;
        reports.saveInitialReport({initialReport: initialReport}, callbacks.onClose);
        expect(callbacks.onAttributeChange).toHaveBeenCalledWith('initialReports', [initialReport]);
        expect(callbacks.onClose).toHaveBeenCalled();
    });
});
