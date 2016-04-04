'use strict';

describe('ARMS Attributes', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        rewire = require('rewire'),
        assign = require('object-assign'),
        Environment = require('../environment/Environment'),

        ArmsAttributes = rewire('../../js/components/reports/arms/ArmsAttributes'),
        arms, barriers = ['EFFECTIVE', 'LIMITED', 'MINIMAL', 'NOT_EFFECTIVE'],
        outcomes = [
            {key: 'NEGLIGIBLE', value: ''},
            {key: 'MINOR', value: ''},
            {key: 'MAJOR', value: ''},
            {key: 'CATASTROPHIC', value: ''}
        ], report = {};

    beforeEach(function () {
        var OptionsStore = jasmine.createSpyObj('OptionsStore', ['listen', 'getBarrierEffectivenessOptions', 'getAccidentOutcomeOptions']);
        ArmsAttributes.__set__('OptionsStore', OptionsStore);
        OptionsStore.getBarrierEffectivenessOptions.and.callFake(function () {
            return barriers;
        });
        OptionsStore.getAccidentOutcomeOptions.and.callFake(function () {
            return outcomes;
        });
        arms = Environment.render(<ArmsAttributes report={report}
                                                  onChange={function(change) {assign(report, change);}}/>);
    });

    it('Calculates the ARMS index from barrier effectiveness and accident outcome values', function () {
        // According to https://www.easa.europa.eu/essi/documents/ARMS.pdf, slide 27
        var expectedValues = {
            'NEGLIGIBLE': {
                'EFFECTIVE': 1,
                'LIMITED': 1,
                'MINIMAL': 1,
                'NOT_EFFECTIVE': 1
            },
            'MINOR': {
                'EFFECTIVE': 2,
                'LIMITED': 4,
                'MINIMAL': 20,
                'NOT_EFFECTIVE': 100
            },
            'MAJOR': {
                'EFFECTIVE': 10,
                'LIMITED': 20,
                'MINIMAL': 100,
                'NOT_EFFECTIVE': 500
            },
            'CATASTROPHIC': {
                'EFFECTIVE': 50,
                'LIMITED': 100,
                'MINIMAL': 500,
                'NOT_EFFECTIVE': 2500
            }
        };
        for (var i = 0, len = barriers.length; i < len; i++) {
            var barrier = barriers[i];
            for (var j = 0, lenn = outcomes.length; j < lenn; j++) {
                var outcome = outcomes[j];
                simulateArmsChange({barrier: barrier, outcome: outcome.key});
                expect(report.armsIndex).toEqual(expectedValues[outcome.key][barrier]);
            }
        }
    });

    function simulateArmsChange(change) {
        TestUtils.Simulate.change(arms.refs.barrierEffectiveness.getWrappedInstance().getInputNode().getInputDOMNode(), {
            target: {
                name: 'barrierEffectiveness',
                value: change.barrier
            }
        });
        TestUtils.Simulate.change(arms.refs.accidentOutcome.getWrappedInstance().getInputNode().getInputDOMNode(), {
            target: {
                name: 'accidentOutcome',
                value: change.outcome
            }
        });
    }

    it('colors ARMS index field green for non-serious outcomes and barrier effectiveness', function () {
        var lowest = {outcome: 'NEGLIGIBLE', barrier: 'LIMITED'},
            highest = {outcome: 'MAJOR', barrier: 'EFFECTIVE'};
        simulateArmsChange(lowest);
        // Re-render for the changes to take effect
        arms = Environment.render(<ArmsAttributes report={report}
                                                  onChange={function(change) {assign(report, change);}}/>);
        expect(arms.refs.armsIndex.props.className).toEqual('arms-index-green');
        simulateArmsChange(highest);
        arms = Environment.render(<ArmsAttributes report={report}
                                                  onChange={function(change) {assign(report, change);}}/>);
        expect(arms.refs.armsIndex.props.className).toEqual('arms-index-green');
    });

    it('colors ARMS index field yellow for mid-serious outcomes and barrier effectiveness', function () {
        var lowest = {outcome: 'MINOR', barrier: 'MINIMAL'},
            highest = {outcome: 'CATASTROPHIC', barrier: 'LIMITED'};
        simulateArmsChange(lowest);
        // Re-render for the changes to take effect
        arms = Environment.render(<ArmsAttributes report={report}
                                                  onChange={function(change) {assign(report, change);}}/>);
        expect(arms.refs.armsIndex.props.className).toEqual('arms-index-yellow');
        simulateArmsChange(highest);
        arms = Environment.render(<ArmsAttributes report={report}
                                                  onChange={function(change) {assign(report, change);}}/>);
        expect(arms.refs.armsIndex.props.className).toEqual('arms-index-yellow');
    });

    it('colors ARMS index field red for serious outcomes and barrier ineffectiveness', function () {
        var lowest = {outcome: 'MAJOR', barrier: 'NOT_EFFECTIVE'},
            highest = {outcome: 'CATASTROPHIC', barrier: 'MINIMAL'};
        simulateArmsChange(lowest);
        // Re-render for the changes to take effect
        arms = Environment.render(<ArmsAttributes report={report}
                                                  onChange={function(change) {assign(report, change);}}/>);
        expect(arms.refs.armsIndex.props.className).toEqual('arms-index-red');
        simulateArmsChange(highest);
        arms = Environment.render(<ArmsAttributes report={report}
                                                  onChange={function(change) {assign(report, change);}}/>);
        expect(arms.refs.armsIndex.props.className).toEqual('arms-index-red');
    });
});
