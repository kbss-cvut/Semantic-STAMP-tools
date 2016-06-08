'use strict';

import React from "react";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";
import OptionsStore from "../../../stores/OptionsStore";
import Typeahead from "react-bootstrap-typeahead";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";
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
            options: Department._processOptions(OptionsStore.getOptions('department'))
        };
    }

    static _processOptions(options) {
        var opts = Utils.processTypeaheadOptions(options);
        opts.sort((a, b) => {
            return a.name.localeCompare(b.name);
        });
        return opts;
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
                options: Department._processOptions(data)
            });
        }
    };

    _onChange = (dept) => {
        this.props.onChange({responsibleDepartment: dept.id});
    };

    _resolveDepartment() {
        var dept = this.props.report.responsibleDepartment;
        if (dept) {
            var option = this.state.options.find((item) => {
                return item.id === dept;
            });
            return option ? option.name : '';
        }
        return '';
    }

    render() {
        var i18n = this.props.i18n;
        return <div>
            <label className='control-label'>
                {i18n('report.responsible-department')}
            </label>
            <Typeahead className='form-group form-group-sm' name='responsibleDepartment'
                       ref='responsibleDepartment' formInputOption='id' optionsButton={true}
                       placeholder={i18n('report.responsible-department')}
                       onOptionSelected={this._onChange} filterOption='name'
                       value={this._resolveDepartment()}
                       displayOption='name' options={this.state.options}
                       customClasses={{input: 'form-control'}} customListComponent={TypeaheadResultList}/>
        </div>;
    }
}

export default injectIntl(I18nWrapper(Department));
