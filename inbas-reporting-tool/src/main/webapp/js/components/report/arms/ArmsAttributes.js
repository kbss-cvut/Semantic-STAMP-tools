'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";
import Actions from "../../../actions/Actions";
import ArmsStore from "../../../stores/ArmsStore";
import ArmsUtils from "../../../utils/ArmsUtils";
import Constants from "../../../constants/Constants";
import Input from "../../Input";
import OptionsStore from "../../../stores/OptionsStore";
import Select from "../../Select";
import Utils from "../../../utils/Utils";
import Vocabulary from "../../../constants/Vocabulary";

const ACCIDENT_OUTCOME = Constants.ARMS.ACCIDENT_OUTCOME;
const BARRIER_EFFECTIVENESS = Constants.ARMS.BARRIER_EFFECTIVENESS;

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
        this.unsubscribeArms = ArmsStore.listen(this._onArmsIndexCalculated);
    };

    componentWillUnmount() {
        this.unsubscribe();
        this.unsubscribeArms();
    };

    _onOptionsLoaded = (type, data) => {
        var update = {};
        if (type === ACCIDENT_OUTCOME) {
            Utils.neighbourSort(data);
            update[ACCIDENT_OUTCOME] = this._transformOutcomesOptions(data);
            this.setState(update);
        } else if (type === BARRIER_EFFECTIVENESS) {
            Utils.neighbourSort(data);
            update[BARRIER_EFFECTIVENESS] = this._transformBarrierOptions(data);
            this.setState(update);
        }
    };

    _transformBarrierOptions(data) {
        return data.map((item) => {
            return {
                label: item[Vocabulary.RDFS_LABEL]['@value'],
                value: item['@id']
            };
        });
    }

    _transformOutcomesOptions(data) {
        return data.map((item) => {
            return {
                value: item['@id'],
                label: item[Vocabulary.RDFS_LABEL]['@value'],
                title: item[Vocabulary.RDFS_COMMENT]['@value']
            };
        });
    }

    _onArmsIndexCalculated = (data) => {
        if (data.action === Actions.calculateArmsIndex) {
            this.props.onChange({'armsIndex': data.armsIndex});
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
        Actions.calculateArmsIndex(report);
    }

    render() {
        var i18n = this.props.i18n,
            report = this.props.report,
            armsIndex = report.armsIndex;
        return <Panel header={<h5>{i18n('arms.title')}</h5>} bsStyle='info'>
            <div className='row'>
                <div className='col-xs-4' title={i18n('arms.barrier-effectiveness.tooltip')}>
                    <Select name='barrierEffectiveness' value={report.barrierEffectiveness}
                            label={i18n('arms.barrier-effectiveness')} addDefault={true}
                            tooltip={i18n('arms.barrier-effectiveness.tooltip')}
                            onChange={this._onChange} options={this.state.barrierEffectiveness}/>
                </div>

                <div className='col-xs-4' title={i18n('arms.accident-outcome.tooltip')}>
                    <Select name='accidentOutcome' value={report.accidentOutcome}
                            label={i18n('arms.accident-outcome')} addDefault={true}
                            tooltip={i18n('arms.accident-outcome.tooltip')}
                            onChange={this._onChange} options={this.state.accidentOutcome}/>
                </div>
                <div className='col-xs-4'>
                    <Input className={ArmsUtils.resolveArmsIndexClass(armsIndex)} type='number'
                           name='armsIndex' ref='armsIndex' value={armsIndex ? armsIndex : ''}
                           label={i18n('arms.index')} readOnly/>
                </div>
            </div>
        </Panel>;
    }
}

export default injectIntl(I18nWrapper(ArmsAttributes));
