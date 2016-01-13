'use strict';

describe('Investigation controller', function () {

    var React = require('react'),
        Button = require('react-bootstrap').Button,
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        InvestigationController = rewire('../../js/components/investigation/InvestigationController'),
        Investigation = rewire('../../js/components/investigation/Investigation'),
        Actions = require('../../js/actions/Actions');

    beforeEach(function () {
        spyOn(Actions, 'loadOccurrenceSeverityOptions');
        Environment.mockFactors(Investigation);
        InvestigationController.__set__('Investigation', Investigation);
    });

    it('shows only Cancel button if the displayed report is not the latest revision.', function () {
        var investigation = {
                key: 12345,
                revision: 1,
                occurrence: {
                    key: 117
                },
                occurrenceStart: Date.now() - 10000,
                occurrenceEnd: Date.now()
            }, revisions = [
                {
                    revision: 2,
                    key: 54321
                },
                {
                    revision: 1,
                    key: 12345
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
});
