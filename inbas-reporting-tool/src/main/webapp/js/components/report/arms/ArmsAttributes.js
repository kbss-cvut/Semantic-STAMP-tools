'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";
import Actions from "../../../actions/Actions";
import Ajax from "../../../utils/Ajax";
import ArmsUtils from "../../../utils/ArmsUtils";
import Input from "../../Input";
import OptionsStore from "../../../stores/OptionsStore";
import Select from "../../Select";
import Vocabulary from "../../../constants/Vocabulary";

const GREATER_THAN = 'http://onto.fel.cvut.cz/ontologies/arms/sira/model/is-higher-than';
const ACCIDENT_OUTCOME = 'accidentOutcome';
const BARRIER_EFFECTIVENESS = 'barrierEffectiveness';

/**
 * Have to sort the array this way, because every item only knows only its immediate successor
 * @param arr The array to sort
 */
function neighbourSort(arr) {
    for (var i = 0, len = arr.length; i < len; i++) {
        for (var j = i; j < len; j++) {
            if (arr[i][GREATER_THAN] && arr[i][GREATER_THAN]['@id'] === arr[j]['@id']) {
                var tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }
    }
}

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
            neighbourSort(data);
            update[ACCIDENT_OUTCOME] = this._transformOutcomesOptions(data);
            this.setState(update);
        } else if (type === BARRIER_EFFECTIVENESS) {
            neighbourSort(data);
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
