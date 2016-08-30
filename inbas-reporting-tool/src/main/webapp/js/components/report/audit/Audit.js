'use strict';

import React from "react";
import DateTimePicker from "kbss-react-bootstrap-datetimepicker";
import assign from "object-assign";
import Typeahead from "react-bootstrap-typeahead";
import AuditFindings from "./AuditFindings";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Input from "../../Input";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";

class Audit extends React.Component {
    static propTypes = {
        audit: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            auditTypes: [],
            organizations: [],
            locations: []
        };
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

    };

    _resolveAuditType() {
        var types = this.props.audit.types;
    }

    _resolveAuditee() {

    }

    _resolveLocation() {

    }

    render() {
        var audit = this.props.audit,
            i18n = this.i18n;
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
                                   value={this._resolveAuditType()} options={this.state.auditTypes}
                                   customClasses={{input: 'form-control'}} optionsButton={true}
                                   customListComponent={TypeaheadResultList}/>
                    </div>
                </div>

                <div className='row'>
                    <div className='col-xs-4'>
                        <label className='control-label'>{i18n('audit.auditee.label') + '*'}</label>
                        <Typeahead className='form-group form-group-sm' formInputOption='id'
                                   placeholder={i18n('audit.auditee.placeholder')}
                                   onOptionSelected={(opt) => this._mergeChange({auditee: opt.id})} filterOption='name'
                                   displayOption='name'
                                   value={this._resolveAuditee()} options={this.state.organizations}
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
                                   value={this._resolveLocation()} options={this.state.locations}
                                   customClasses={{input: 'form-control'}} optionsButton={true}
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
            <AuditFindings audit={audit} onChange={this._mergeChange}/>
        </div>;
    }
}

export default injectIntl(I18nWrapper(Audit));
