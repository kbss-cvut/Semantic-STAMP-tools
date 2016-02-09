'use strict';

describe('ReportDetailController tests', function () {

    var React = require('react'),
        Button = require('react-bootstrap').Button,
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator'),
        Actions = require('../../js/actions/Actions'),
        RouterStore = require('../../js/stores/RouterStore'),
        ReportDetailController = require('../../js/components/preliminary/ReportDetailController');

    beforeEach(function () {
        spyOn(Actions, 'loadOptions');
        spyOn(Actions, 'loadOccurrenceCategories');
    });

    it('Show only cancel button for older revisions of a report', function () {
        var report = Generator.generatePreliminaryReport(),
            revisions = [
                {revision: 2, key: '123456'},
                {revision: report.revision, key: report.key}
            ];
        spyOn(Actions, 'loadReport');
        spyOn(Actions, 'loadRevisions');
        var controller = Environment.render(<ReportDetailController report={report} revisions={revisions}/>),
            expectedButtons = ['Cancel'],
            hiddenButtons = ['Save', 'Submit to authority', 'Investigate'],
            i;

        for (i = 0; i < expectedButtons.length; i++) {
            expect(getButton(controller, expectedButtons[i])).not.toBeNull();
        }
        for (i = 0; i < hiddenButtons.length; i++) {
            expect(getButton(controller, hiddenButtons[i])).toBeNull();
        }
    });

    function getButton(root, text) {
        return Environment.getComponentByText(root, Button, text);
    }

    it('updates report state when onChange is called', function () {
        var report = Generator.generatePreliminaryReport(),
            newSummary = 'New summary';
        spyOn(Actions, 'loadReport');
        spyOn(Actions, 'loadRevisions');
        var controller = Environment.render(<ReportDetailController report={report}/>);

        controller.onChange({summary: newSummary});
        expect(controller.state.report.summary).toEqual(newSummary);
    });

    it('calls loadReport when revision is selected.', function () {
        var report = Generator.generatePreliminaryReport(),
            selectedRevision = {revision: 2, key: '111222333'};
        spyOn(Actions, 'loadReport');
        spyOn(Actions, 'loadRevisions');
        var controller = Environment.render(<ReportDetailController report={report}/>);
        spyOn(controller, 'loadReport');

        controller.onRevisionSelected(selectedRevision);
        expect(controller.loadReport).toHaveBeenCalledWith(selectedRevision.key);
    });
});
