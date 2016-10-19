'use strict';

import React from "react";
import JsonLdUtils from "jsonld-utils";
import Actions from "../../../actions/Actions";
import Constants from "../../../constants/Constants";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import OptionsStore from "../../../stores/OptionsStore";
import Select from "../../Select";
import Utils from "../../../utils/Utils";

class Sira extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            options: JsonLdUtils.processSelectOptions(Utils.neighbourSort(OptionsStore.getOptions('sira')))
        }
    }

    componentDidMount() {
        if (this.state.options.length === 0) {
            Actions.loadOptions('sira');
        }
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    _onOptionsLoaded = (type, data) => {
        if (type !== 'sira') {
            return;
        }
        Utils.neighbourSort(data);
        this.setState({options: JsonLdUtils.processSelectOptions(data)});
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onOptionSelected = (e) => {
        this.props.onChange({sira: e.target.value});
    };

    render() {
        var report = this.props.report,
            inputProps = {
                className: report.sira ? Constants.SIRA_COLORS[report.sira] : ''
            };
        return <div className='row'>
            <div className='col-xs-4'>
                <Select label={this.i18n('safety-issue.sira.label')} onChange={this._onOptionSelected}
                        title={this.i18n('safety-issue.sira-tooltip')} inputProps={inputProps}
                        options={this.state.options} addDefault={true} value={report.sira}/>
            </div>
        </div>;
    }
}

export default injectIntl(I18nWrapper(Sira));
