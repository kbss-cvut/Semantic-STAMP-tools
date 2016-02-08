'use strict';

var React = require('react');
var Reflux = require('reflux');
var Panel = require('react-bootstrap').Panel;
var Input = require('../../Input');
var Select = require('../../Select');

var OptionsStore = require('../../../stores/OptionsStore');
var Utils = require('../../../utils/Utils');

var injectIntl = require('../../../utils/injectIntl');
var I18nMixin = require('../../../i18n/I18nMixin');

var ARMS_THRESHOLDS = {
    green: 20,
    yellow: 500
};

var ARMS_INDEX_MIN = 1;
var ARMS_INDEX_MAX = 10000;

var ArmsAttributes = React.createClass({
    mixins: [Reflux.ListenerMixin, I18nMixin],

    propTypes: {
        onAttributeChange: React.PropTypes.func.isRequired,
        report: React.PropTypes.object.isRequired
    },

    getInitialState: function () {
        return {
            barriers: OptionsStore.getBarrierEffectivenessOptions(),
            outcomes: OptionsStore.getAccidentOutcomeOptions()
        };
    },

    componentDidMount: function () {
        this.listenTo(OptionsStore, this.onOptionsLoaded);
    },

    onOptionsLoaded: function (type, data) {
        if (type === 'barrierEffectiveness') {
            this.setState({barriers: data});
        } else if (type === 'accidentOutcome') {
            this.setState({outcomes: data});
        }
    },

    onChange: function (e) {
        var value = e.target.value;
        if (e.target.name === 'armsIndex') {
            if (value < ARMS_INDEX_MIN) {
                value = ARMS_INDEX_MIN;
            } else if (value > ARMS_INDEX_MAX) {
                value = ARMS_INDEX_MAX;
            }
        }
        this.props.onAttributeChange(e.target.name, value);
    },


    render: function () {
        var report = this.props.report;
        return (
            <Panel header={<h5>{this.i18n('preliminary.detail.arms.panel-title')}</h5>} bsStyle='info'>
                <div className='row'>
                    <div className='col-xs-4' title={this.i18n('preliminary.detail.arms.barriers-tooltip')}>
                        <Select name='barrierEffectiveness' value={report.barrierEffectiveness}
                                label={this.i18n('preliminary.detail.arms.barriers-label')}
                                tooltip={this.i18n('preliminary.detail.arms.barriers-tooltip')}
                                onChange={this.onChange} options={this.renderBarrierOptions()}/>
                    </div>

                    <div className='col-xs-4' title={this.i18n('preliminary.detail.arms.outcomes-tooltip')}>
                        <Select name='accidentOutcome' value={report.accidentOutcome}
                                label={this.i18n('preliminary.detail.arms.outcomes-label')}
                                tooltip={this.i18n('preliminary.detail.arms.outcomes-tooltip')}
                                onChange={this.onChange} options={this.renderOutcomesOptions()}/>
                    </div>
                    <div className='col-xs-4'>
                        <Input className={this.resolveArmsIndexCls()} type='number' name='armsIndex'
                               value={report.armsIndex} label='ARMS Index'
                               onChange={this.onChange} min={ARMS_INDEX_MIN} max={ARMS_INDEX_MAX}/>
                    </div>
                </div>
            </Panel>
        );
    },

    renderBarrierOptions: function () {
        var barriers = this.state.barriers,
            options = [];
        for (var i = 0, len = barriers.length; i < len; i++) {
            options.push({value: barriers[i], label: Utils.constantToString(barriers[i], true)});
        }
        return options;
    },

    renderOutcomesOptions: function () {
        var outcomes = this.state.outcomes,
            options = [];
        for (var i = 0, len = outcomes.length; i < len; i++) {
            options.push({
                value: outcomes[i].key,
                label: Utils.constantToString(outcomes[i].key, true),
                title: outcomes[i].value
            });
        }
        return options;
    },

    resolveArmsIndexCls: function () {
        var armsIndex = this.props.report.armsIndex;
        if (!armsIndex) {
            return '';
        }
        if (armsIndex < ARMS_THRESHOLDS.green) {
            return 'arms-index-green';
        }
        if (armsIndex < ARMS_THRESHOLDS.yellow) {
            return 'arms-index-yellow';
        }
        if (armsIndex >= ARMS_THRESHOLDS.yellow) {
            return 'arms-index-red';
        }
    }
});

module.exports = injectIntl(ArmsAttributes);
