'use strict';

import React from "react";
import JsonLdUtils from "jsonld-utils";
import DateTimePicker from "kbss-react-bootstrap-datetimepicker";
import assign from "object-assign";
import Typeahead from "react-bootstrap-typeahead";
import Actions from "../../../actions/Actions";
import AuditFindings from "./AuditFindings";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Input from "../../Input";
import OptionsStore from "../../../stores/OptionsStore";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";
import Utils from "../../../utils/Utils";

class Audit extends React.Component {
    static propTypes = {
        audit: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            auditType: JsonLdUtils.processTypeaheadOptions(OptionsStore.getOptions('auditType')),
            organization: JsonLdUtils.processTypeaheadOptions(OptionsStore.getOptions('organization')),
            location: JsonLdUtils.processTypeaheadOptions(OptionsStore.getOptions('location'))
        };
    }

    componentDidMount() {
        if (this.state.auditType.length === 0) {
            Actions.loadOptions('auditType');
        }
        if (this.state.organization.length === 0) {
            Actions.loadOptions('organization');
        }
        if (this.state.location.length === 0) {
            Actions.loadOptions('location');
        }
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    _onOptionsLoaded = (type, data) => {
        if (type !== 'auditType' && type !== 'organization' && type !== 'location') {
            return;
        }
        var newState = {};
        newState[type] = JsonLdUtils.processTypeaheadOptions(data);
        this.setState(newState);
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onChange = (e) => {
        var change = {};
        change[e.target.name] = e.target.value;
        this._mergeChange(change);
    };

    _mergeChange = (change) => {
        var audit = assign({}, this.props.audit, change);
        this.props.onChange({audit: audit});
    };

    _onTypeSelected = (option) => {
        var types = this.props.audit.types ? this.props.audit.types.slice() : [];
        var origType = Utils.resolveType(types, this.state.auditType);
        if (origType) {
            types.splice(types.indexOf(origType.id), 1);
        }
        types.push(option.id);
        this._mergeChange({types: types});
    };

    _onAuditeeSelected = (option) => {
        this._mergeChange({auditee: option});
    };

    _resolveAuditType() {
        return Utils.resolveType(this.props.audit.types, this.state.auditType);
    }

    _resolveLocation() {
        var locations = this.state.location,
            auditLoc = this.props.audit.location;
        if (locations.length === 0 || !auditLoc) {
            return null;
        }
        var loc = locations.find((item) => item.id === auditLoc);
        return loc ? loc.name : '';
    }

    render() {
        var audit = this.props.audit,
            i18n = this.i18n,
            auditType = this._resolveAuditType();
        return <div>
            <div className='form-group'>
                <div className='row'>
                    <div className='col-xs-4'>
                        <Input type='text' name='name' value={audit.name}
                               onChange={this._onChange}
                               label={i18n('name') + '*'}
                               title={i18n('audit.name-tooltip')}/>
                    </div>
                </div>

                <div className='row'>
                    <div className='col-xs-4'>
                        <label className='control-label'>{i18n('audit.type.label')}</label>
                        <Typeahead className='form-group form-group-sm' formInputOption='id'
                                   placeholder={i18n('audit.type.placeholder')}
                                   onOptionSelected={this._onTypeSelected} filterOption='name' displayOption='name'
                                   value={auditType ? auditType.name : ''} options={this.state.auditType}
                                   customClasses={{input: 'form-control'}} optionsButton={true}
                                   customListComponent={TypeaheadResultList}/>
                    </div>
                </div>

                <div className='row'>
                    <div className='col-xs-4'>
                        <label className='control-label'>{i18n('audit.auditee.label') + '*'}</label>
                        <Typeahead className='form-group form-group-sm' formInputOption='id'
                                   placeholder={i18n('audit.auditee.placeholder')}
                                   onOptionSelected={this._onAuditeeSelected} filterOption='name'
                                   displayOption='name'
                                   value={audit.auditee ? audit.auditee.name : null} options={this.state.organization}
                                   customClasses={{input: 'form-control'}} optionsButton={true}
                                   customListComponent={TypeaheadResultList}/>
                    </div>
                </div>

                <div className='row'>
                    <div className='col-xs-4'>
                        <label className='control-label'>{i18n('audit.location.label')}</label>
                        <Typeahead className='form-group form-group-sm' formInputOption='id'
                                   placeholder={i18n('audit.location.placeholder')}
                                   onOptionSelected={(opt) => this._mergeChange({location: opt.id})} filterOption='name'
                                   displayOption='name'
                                   value={this._resolveLocation()} options={this.state.location}
                                   customClasses={{input: 'form-control'}}
                                   customListComponent={TypeaheadResultList}/>
                    </div>
                </div>

                <div className='row'>
                    <div className='picker-container form-group form-group-sm col-xs-4'>
                        <label className='control-label'>{this.i18n('audit.start-date')}</label>
                        <DateTimePicker inputFormat='DD-MM-YY HH:mm' dateTime={audit.startDate.toString()}
                                        onChange={(val) => this._mergeChange({startDate: Number(val)})}
                                        inputProps={{title: this.i18n('audit.start-date.tooltip'), bsSize: 'small'}}/>
                    </div>
                    <div className='picker-container form-group form-group-sm col-xs-4'>
                        <label className='control-label'>{this.i18n('audit.end-date')}</label>
                        <DateTimePicker inputFormat='DD-MM-YY HH:mm' dateTime={audit.endDate.toString()}
                                        onChange={(val) => this._mergeChange({endDate: Number(val)})}
                                        inputProps={{title: this.i18n('audit.end-date.tooltip'), bsSize: 'small'}}/>
                    </div>
                </div>
            </div>
            <AuditFindings audit={audit} auditType={auditType ? auditType.id : ''} onChange={this._mergeChange}/>
        </div>;
    }
}

export default injectIntl(I18nWrapper(Audit));
