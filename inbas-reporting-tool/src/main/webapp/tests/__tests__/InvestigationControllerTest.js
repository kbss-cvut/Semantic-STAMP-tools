'use strict';

describe('Investigation controller', function () {

    var React = require('react'),
        Button = require('react-bootstrap').Button,
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator'),
        InvestigationController = rewire('../../js/components/investigation/InvestigationController'),
        Investigation = rewire('../../js/components/investigation/Investigation'),
        Actions = require('../../js/actions/Actions');

    beforeEach(function () {
        spyOn(Actions, 'loadOccurrenceSeverityOptions');
        Environment.mockFactors(Investigation);
        InvestigationController.__set__('Investigation', Investigation);
    });

    it('shows only Cancel button if the displayed report is not the latest revision.', function () {
        var investigation = Generator.generateInvestigation(),
            revisions = [
                {
                    revision: 2,
                    key: 54321
                },
                {
                    revision: investigation.revision,
                    key: investigation.key
                }
            ],
            expectedButtons = ['Cancel'],
            hiddenButtons = ['Save', 'Submit to authority'],
            i, len;
        spyOn(Actions, 'findInvestigation');
        spyOn(Actions, 'loadInvestigationRevisions');
        var result = Environment.render(<InvestigationController params={{}}/>);
        result.onInvestigationStoreTrigger({action: Actions.findInvestigation, investigation: investigation});
        result.onInvestigationStoreTrigger({action: Actions.loadInvestigationRevisions, revisions: revisions});

        for (i = 0, len = expectedButtons.length; i < len; i++) {
            expect(getButton(result, expectedButtons[i])).not.toBeNull();
        }
        for (i = 0, len = hiddenButtons.length; i < len; i++) {
            expect(getButton(result, hiddenButtons[i])).toBeNull();
        }
    });

    function getButton(root, text) {
        return Environment.getComponentByText(root, Button, text);
    }

    it('updates report state when onChange is called.', function () {
        var investigation = Generator.generateInvestigation(),
            newSummary = 'New investigation summary.';
        spyOn(Actions, 'findInvestigation');
        spyOn(Actions, 'loadInvestigationRevisions');
        var result = Environment.render(<InvestigationController params={{}}/>);
        result.onInvestigationStoreTrigger({action: Actions.findInvestigation, investigation: investigation});

        result.onChange({summary: newSummary});
        expect(result.state.report.summary).toEqual(newSummary);
    });

    it('calls loadReport when revision is selected.', function () {
        var investigation = Generator.generateInvestigation(),
            selectedRevision = {revision: 2, key: '111222333'};
        spyOn(Actions, 'findInvestigation');
        spyOn(Actions, 'loadInvestigationRevisions');
        var result = Environment.render(<InvestigationController params={{}}/>);
        result.onInvestigationStoreTrigger({action: Actions.findInvestigation, investigation: investigation});
        spyOn(result, 'loadReport');

        result.onRevisionSelected(selectedRevision);
        expect(result.loadReport).toHaveBeenCalledWith(selectedRevision.key);
    });
});
