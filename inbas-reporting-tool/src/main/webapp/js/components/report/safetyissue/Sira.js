'use strict';

import React from "react";
import assign from "object-assign";
import JsonLdUtils from "jsonld-utils";
import {Button, Panel} from "react-bootstrap";
import Actions from "../../../actions/Actions";
import ArmsStore from "../../../stores/ArmsStore";
import Constants from "../../../constants/Constants";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Input from "../../Input";
import OptionsStore from "../../../stores/OptionsStore";
import Select from "../../Select";
import Utils from "../../../utils/Utils";
import Vocabulary from "../../../constants/Vocabulary";

const OPTIONS = ['initialEventFrequency', 'barrierUosAvoidanceFailFrequency',
    'barrierRecoveryFailFrequency', 'accidentSeverity'];
const SIRA = 'sira';

function processOptions(callback) {
    for (let i = 0, len = OPTIONS.length; i < len; i++) {
        callback(OPTIONS[i]);
    }
}

class Sira extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        let state = {};
        processOptions((item) => {
            state[item] = JsonLdUtils.processSelectOptions(Utils.neighbourSort(OptionsStore.getOptions(item)), this.props.intl);
        });
        state[SIRA] = OptionsStore.getOptions(SIRA);
        this.state = state;
    }

    componentDidMount() {
        processOptions((item) => {
            if (this.state[item].length === 0) {
                Actions.loadOptions(item);
            }
        });
        if (this.state[SIRA].length === 0) {
            Actions.loadOptions(SIRA);
        }
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
        this.unsubscribeSira = ArmsStore.listen(this._onSiraCalculated);
    }

    _onOptionsLoaded = (type, data) => {
        if (OPTIONS.indexOf(type) == -1 && type !== SIRA) {
            return;
        }
        let stateUpdate = {};
        if (type === SIRA) {
            stateUpdate[SIRA] = data;
        } else {
            Utils.neighbourSort(data, 'http://onto.fel.cvut.cz/ontologies/arms/sira/model/is-higher-than');
            stateUpdate[type] = JsonLdUtils.processSelectOptions(data, this.props.intl);
        }
        this.setState(stateUpdate);
    };

    _onSiraCalculated = (data) => {
        if (data.action === Actions.calculateSira) {
            let update = assign({}, this.props.report.sira);
            update.siraValue = data.sira;
            this.props.onChange({sira: update});
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
        this.unsubscribeSira();
    }

    _onOptionSelected = (e) => {
        let update = assign({}, this.props.report.sira);
        update[e.target.name] = e.target.value;
        this.props.onChange({sira: update});
        this._calculateSira();
    };

    _calculateSira() {
        for (let i = 0, len = OPTIONS.length; i < len; i++) {
            if (!this.props.report.sira[OPTIONS[i]]) {
                return;
            }
        }
        Actions.calculateSira(this.props.report.sira);
    }

    _resolveSiraValue(siraValue) {
        let sira = this.state.sira;
        for (let i = 0, len = sira.length; i < len; i++) {
            if (siraValue === sira[i]['@id']) {
                return JsonLdUtils.getLocalized(sira[i][Vocabulary.RDFS_LABEL], this.props.intl);
            }
        }
        return '';
    }

    _resetSira = () => {
        let change = assign({}, this.props.report.sira);
        processOptions((item) => {
            change[item] = null;
        });
        change.siraValue = null;
        this.props.onChange({sira: change});
    };

    render() {
        return <Panel header={<h5>{this.i18n('safety-issue.sira.label')}</h5>} bsStyle='info'
                      title={this.i18n('safety-issue.sira-tooltip')}>
            <div className='row'>
                {this._renderAttributeSelections()}
                {this._renderSiraValue()}
                {this._renderResetSiraButton()}
            </div>
        </Panel>;
    }

    _renderAttributeSelections() {
        let items = [],
            i18n = this.i18n,
            report = this.props.report;
        processOptions((item) => {
            items.push(<div key={item} className='col-xs-2'>
                <Select name={item} label={i18n('safety-issue.sira.' + item)} addDefault={true}
                        title={i18n('safety-issue.sira.' + item + '-tooltip')} options={this.state[item]}
                        value={report.sira ? report.sira[item] : ''} onChange={this._onOptionSelected}/>
            </div>);
        });
        return items;
    }

    _renderSiraValue() {
        let siraValue = this.props.report.sira ? this.props.report.sira.siraValue : '',
            sira = this._resolveSiraValue(siraValue),
            inputClass = siraValue ? Constants.SIRA_COLORS[siraValue] : '';

        return <div className='col-xs-2'>
            <Input label={this.i18n('safety-issue.sira.value-label')} value={sira} className={inputClass}
                   readOnly={true}/>
        </div>;
    }

    _renderResetSiraButton() {
        let sira = this.props.report.sira;
        return sira && sira.siraValue ? <div className='col-xs-1'>
            <Button bsStyle='primary' bsSize='small' onClick={this._resetSira} className='in-input-line'
                    title={this.i18n('safety-issue.sira.reset-tooltip')}>{this.i18n('safety-issue.sira.reset')}</Button>
        </div> : null;
    }
}

export default injectIntl(I18nWrapper(Sira));
