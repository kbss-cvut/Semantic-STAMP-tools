'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";
import Actions from "../../../actions/Actions";
import Ajax from "../../../utils/Ajax";
import Input from "../../Input";
import OptionsStore from "../../../stores/OptionsStore";
import Select from "../../Select";
import Utils from "../../../utils/Utils";

const ACCIDENT_OUTCOME = 'accidentOutcome';
const BARRIER_EFFECTIVENESS = 'barrierEffectiveness';

var ARMS_THRESHOLDS = {
    green: 20,
    yellow: 500
};

class ArmsAttributes extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            accidentOutcome: [],
            barrierEffectiveness: []
        };
    }

    componentWillMount() {
        Actions.loadOptions(ACCIDENT_OUTCOME);
        Actions.loadOptions(BARRIER_EFFECTIVENESS);
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    };

    componentWillUnmount() {
        this.unsubscribe();
    };

    _onOptionsLoaded = (type, data) => {
        var update = {};
        if (type === ACCIDENT_OUTCOME) {
            update[ACCIDENT_OUTCOME] = data;
            this.setState(update);
        } else if (type === BARRIER_EFFECTIVENESS) {
            update[BARRIER_EFFECTIVENESS] = data;
            this.setState(update);
        }
    };

    _onChange = (e) => {
        var change = {};
        change[e.target.name] = e.target.value;
        this.props.onChange(change);
        this._calculateArmsIndex();
    };

    _calculateArmsIndex() {
        var report = this.props.report;
        if (!report.barrierEffectiveness || !report.accidentOutcome) {
            return;
        }
        Ajax.get('rest/arms?' + ACCIDENT_OUTCOME + '=' + report.accidentOutcome +
            '&' + BARRIER_EFFECTIVENESS + '=' + report.barrierEffectiveness).end((data) => {
            this.props.onChange({'armsIndex': Number(data)});
        });
    }

    render() {
        var i18n = this.props.i18n,
            report = this.props.report;
        return (
            <Panel header={<h5>{i18n('arms.title')}</h5>} bsStyle='info'>
                <div className='row'>
                    <div className='col-xs-4' title={i18n('arms.barrier-effectiveness.tooltip')}>
                        <Select name='barrierEffectiveness' value={report.barrierEffectiveness}
                                label={i18n('arms.barrier-effectiveness')}
                                tooltip={i18n('arms.barrier-effectiveness.tooltip')}
                                onChange={this._onChange} options={this._renderBarrierOptions()}/>
                    </div>

                    <div className='col-xs-4' title={i18n('arms.accident-outcome.tooltip')}>
                        <Select name='accidentOutcome' value={report.accidentOutcome}
                                label={i18n('arms.accident-outcome')}
                                tooltip={i18n('arms.accident-outcome.tooltip')}
                                onChange={this._onChange} options={this._renderOutcomesOptions()}/>
                    </div>
                    <div className='col-xs-4'>
                        <Input className={this._resolveArmsIndexCls()} type='number' name='armsIndex' ref='armsIndex'
                               value={report.armsIndex} label={i18n('arms.index')} readOnly/>
                    </div>
                </div>
            </Panel>
        );
    }

    _renderBarrierOptions() {
        return this.state.barrierEffectiveness.map((item) => {
            return {
                label: Utils.constantToString(item, true),
                value: item
            };
        });
    }

    _renderOutcomesOptions() {
        return this.state.accidentOutcome.map((item) => {
            return {
                value: item.key,
                label: Utils.constantToString(item.key, true),
                title: item.value
            };
        });
    }

    _resolveArmsIndexCls() {
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
}

export default injectIntl(I18nWrapper(ArmsAttributes));
