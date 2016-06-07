'use strict';

import React from "react";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";
import OptionsStore from "../../../stores/OptionsStore";
import Select from "../../Select";
import Utils from "../../../utils/Utils";

class Department extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired,
        i18n: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            options: Utils.processSelectOptions(OptionsStore.getOptions('department'))
        };
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onOptionsLoaded = (type, data) => {
        if (type === 'department') {
            this.setState({
                options: Utils.processSelectOptions(data)
            });
        }
    };

    _onChange = (evt) => {
        var change = {};
        change[evt.target.name] = evt.target.value;
        this.props.onChange(change);
    };

    render() {
        var i18n = this.props.i18n,
            report = this.props.report;
        return <Select name='responsibleDepartment' label={i18n('report.responsible-department')} addDefault={true}
                       value={report.responsibleDepartment} onChange={this._onChange} options={this.state.options}/>
    }
}

export default injectIntl(I18nWrapper(Department));
