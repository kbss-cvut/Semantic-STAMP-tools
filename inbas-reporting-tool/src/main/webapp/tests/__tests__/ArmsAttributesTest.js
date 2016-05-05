'use strict';

describe('ARMS attributes', () => {

    var React = require('react'),
        assign = require('object-assign'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        ArmsAttributes = require('../../js/components/report/arms/ArmsAttributes').default,
        Actions = require('../../js/actions/Actions'),
        Ajax = require('../../js/utils/Ajax'),
        Select = require('../../js/components/Select'),
        report, onChange,
        accident = 'http://onto.fel.cvut.cz/ontologies/arms/sira/accident-outcome/negligible',
        barrier = 'http://onto.fel.cvut.cz/ontologies/arms/sira/barrier-effectiveness/effective';

    beforeEach(() => {
        spyOn(Ajax, 'get').and.callFake(() => {
            return Ajax
        });
        spyOn(Ajax, 'end');
        report = {};
        onChange = function (change) {
            assign(report, change);
        };
        spyOn(Actions, 'loadOptions');
    });

    it('does not request ARMS index calculation when only one ARMS attribute is set', () => {
        var component = Environment.render(<ArmsAttributes report={report} onChange={onChange}/>);
        simulateChange(component, {
            target: {
                name: 'barrierEffectiveness',
                value: barrier
            }
        });
        expect(Ajax.get).not.toHaveBeenCalled();
        delete report.barrierEffectiveness;
        simulateChange(component, {
            target: {
                name: 'accidentOutcome',
                value: accident
            }
        });
        expect(Ajax.get).not.toHaveBeenCalled();
    });

    function simulateChange(root, change) {
        var selects = TestUtils.scryRenderedComponentsWithType(root, Select);
        for (var i = 0; i < selects.length; i++) {
            if (selects[i].props.name === change.target.name) {
                TestUtils.Simulate.change(selects[i].getWrappedInstance().refs.select.refs.input.getInputDOMNode(), change);
                break;
            }
        }
    }

    it('uses Ajax to calculate ARMS index', () => {
        var component = Environment.render(<ArmsAttributes report={report} onChange={onChange}/>);
        simulateChange(component, {
            target: {
                name: 'barrierEffectiveness',
                value: barrier
            }
        });
        simulateChange(component, {
            target: {
                name: 'accidentOutcome',
                value: accident
            }
        });
        expect(Ajax.get).toHaveBeenCalledWith('rest/arms?accidentOutcome=' + accident + '&barrierEffectiveness=' + barrier);
    })
});
