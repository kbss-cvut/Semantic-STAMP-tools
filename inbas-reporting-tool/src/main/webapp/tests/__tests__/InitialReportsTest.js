'use strict';

jest.dontMock('../../js/components/initialreport/InitialReports');
jest.dontMock('../../js/components/initialreport/InitialReport');
jest.dontMock('../../js/components/initialreport/Steps');

describe('InitialReports component tests', function () {

    var React = require('react/addons'),
        TestUtils = React.addons.TestUtils,
        InitialReports = require('../../js/components/initialreport/InitialReports');

    it('Opens initial report add dialog on Add button click', function () {
        var reports = TestUtils.renderIntoDocument(<InitialReports report={{}} onAttributeChange={function() {}}/>);
        var addButton = React.findDOMNode(reports.refs.addInitialReport);
        var wizard = reports.refs.initialReportWizard;
        expect(wizard.props.show).toBeFalsy();
        TestUtils.Simulate.click(addButton);
        expect(wizard.props.show).toBeTruthy();
    });

    it('Adds new initial report when wizard is submitted', function () {
        var callbacks = {
            onAttributeChange: function () {
            },
            onClose: function () {
            }
        };
        spyOn(callbacks, 'onAttributeChange');
        spyOn(callbacks, 'onClose');
        var reports = TestUtils.renderIntoDocument(<InitialReports report={{initialReports: []}}
                                                                   onAttributeChange={callbacks.onAttributeChange}/>),
            initialReportText = 'Test',
            initialReport = {text: initialReportText};

        reports.saveNewInitialReport({initialReport: initialReport}, callbacks.onClose);
        expect(callbacks.onAttributeChange).toHaveBeenCalledWith('initialReports', [initialReport]);
        expect(callbacks.onClose).toHaveBeenCalled();
    });

    it('Updates initial reports when editing one is finished', function () {
        var callbacks = {
            onAttributeChange: function () {
            },
            onClose: function () {
            }
        };
        spyOn(callbacks, 'onAttributeChange');
        spyOn(callbacks, 'onClose');
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
