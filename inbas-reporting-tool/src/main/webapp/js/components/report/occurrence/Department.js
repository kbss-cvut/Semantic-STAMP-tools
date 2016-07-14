'use strict';

import React from "react";
import {Button, Glyphicon, Panel} from "react-bootstrap";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";
import OptionsStore from "../../../stores/OptionsStore";
import Typeahead from "react-bootstrap-typeahead";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";
import Utils from "../../../utils/Utils";

class Department extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            options: Department._processOptions(OptionsStore.getOptions('department')),
            added: false
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

    _onChange = (dept, index) => {
        var departments = this.props.report.responsibleDepartments ? this.props.report.responsibleDepartments.slice() : [];
        if (index) {
            departments[index] = dept.id;
        } else {
            departments.push(dept.id);
            this.setState({added: false});
        }
        this.props.onChange({responsibleDepartments: departments});
    };

    _onAdd = () => {
        this.setState({added: true});
    };

    _resolveDepartment(dept) {
        if (dept) {
            var option = this.state.options.find((item) => {
                return item.id === dept;
            });
            return option ? option.name : '';
        }
        return '';
    }

    render() {
        var i18n = this.props.i18n,
            departments = this._renderDepartments();
        return <Panel header={<h5>{i18n('report.organization')}</h5>} bsStyle='info'>
            {departments}
            <Button bsStyle='primary' bsSize='small' onClick={this._onAdd}
                    title={i18n('report.responsible-department.add-tooltip')}>
                <Glyphicon glyph='plus' style={{margin: '0 5px 0 0'}}/>
                {i18n('add')}
            </Button>
        </Panel>;
    }

    _renderDepartments() {
        var departments = this.props.report.responsibleDepartments,
            toRender = [],
            i18n = this.props.i18n;
        if (departments) {
            for (var i = 0, len = departments.length; i < len; i++) {
                toRender.push(<DepartmentInput key={'dept_' + i} value={this._resolveDepartment(departments[i])}
                                               index={i}
                                               onChange={this._onChange} i18n={i18n} options={this.state.options}/>);
            }
        }
        if (this.state.added || !departments) {
            toRender.push(<DepartmentInput key='dept_added' onChange={this._onChange} i18n={i18n} value={''}
                                           options={this.state.options}/>);
        }
        return toRender;
    }
}

class DepartmentInput extends React.Component {

    static propTypes = {
        value: React.PropTypes.string,
        options: React.PropTypes.array,
        onChange: React.PropTypes.func.isRequired,
        index: React.PropTypes.number,
        i18n: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
    }

    _onChange = (option) => {
        this.props.onChange(option, this.props.index);
    };

    render() {
        var i18n = this.props.i18n;
        return <div className='row'>
            <div className='col-xs-4'>
                <label className='control-label'>
                    {i18n('report.responsible-department')}
                </label>
                <Typeahead className='form-group form-group-sm' formInputOption='id' optionsButton={true}
                           placeholder={i18n('report.responsible-department')}
                           onOptionSelected={this._onChange} filterOption='name'
                           value={this.props.value} displayOption='name' options={this.props.options}
                           customClasses={{input: 'form-control'}} customListComponent={TypeaheadResultList}/>
            </div>
        </div>;
    }
}

export default injectIntl(I18nWrapper(Department));
