'use strict';

import React from "react";
import JsonLdUtils from "jsonld-utils";
import assign from "object-assign";
import {Button, Modal} from "react-bootstrap";
import Typeahead from "react-bootstrap-typeahead";
import Constants from "../../../constants/Constants";
import FindingFactors from "./FindingFactors";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Input from "../../Input";
import OptionsStore from "../../../stores/OptionsStore";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";
import Utils from "../../../utils/Utils";

class AuditFinding extends React.Component {
    static propTypes = {
        finding: React.PropTypes.object,
        onSave: React.PropTypes.func.isRequired,
        onClose: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            finding: props.finding ? assign({}, props.finding) : null,
            findingType: JsonLdUtils.processTypeaheadOptions(OptionsStore.getOptions('findingType'))
        };
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    _onOptionsLoaded(type, data) {
        if (type === 'findingType') {
            this.setState({findingType: JsonLdUtils.processTypeaheadOptions(data)});
        }
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.finding && this.props.finding !== nextProps.finding) {
            this.setState({finding: assign({}, nextProps.finding)});
        }
    }

    _resolveFindingType() {
        var type = Utils.resolveType(this.state.finding.types, this.state.findingType);
        return type ? type.name : '';
    }

    _onTypeSelected = (option) => {
        var types = this.state.finding.types ? this.state.finding.types.slice() : [],
            origType = Utils.resolveType(types, this.state.findingType);
        if (origType) {
            types.splice(types.indexOf(origType.id), 1);
        }
        types.push(option.id);
        var finding = assign({}, this.state.finding, {types: types});
        this.setState({finding: finding});
    };

    _onLevelSelected = (e) => {
        var change = {level: Number(e.target.value)};
        this._mergeChange(change);
    };

    _mergeChange = (change) => {
        var finding = assign({}, this.state.finding, change);
        this.setState({finding: finding});
    };

    render() {
        var finding = this.state.finding;
        if (!finding) {
            return null;
        }
        return <Modal show={this.props.show} bsSize='large' animation={true} dialogClassName='large-modal'
                      onHide={this.props.onClose}>
            <Modal.Header closeButton>
                <Modal.Title>{this.i18n('audit.finding.header')}</Modal.Title>
            </Modal.Header>
            <div className='modal-body'>
                <div className='row'>
                    <div className='col-xs-4'>
                        <label className='control-label'>{this.i18n('audit.finding.type.label')}</label>
                        <Typeahead className='form-group form-group-sm' formInputOption='id'
                                   placeholder={this.i18n('audit.finding.type.placeholder')}
                                   onOptionSelected={this._onTypeSelected} filterOption='name' displayOption='name'
                                   value={this._resolveFindingType()} options={this.state.findingType}
                                   customClasses={{input: 'form-control'}} optionsButton={true}
                                   customListComponent={TypeaheadResultList}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='form-group'>
                        {this._renderLevels()}
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-12'>
                        <Input type='textarea' label={this.i18n('description')} rows={8} name='description'
                               value={finding.description ? finding.description : ''}
                               placeholder={this.i18n('audit.finding.description-tooltip')}
                               title={this.i18n('audit.finding.description-tooltip')}
                               onChange={e => this._mergeChange({description: e.target.value})}/>
                    </div>
                </div>
                <div className='row'>
                    <FindingFactors factors={finding.factors} onChange={this._mergeChange}/>
                    </div>
            </div>
            <Modal.Footer>
                <Button bsSize='small' bsStyle='success'
                        onClick={() => this.props.onSave(finding)}>{this.i18n('save')}</Button>
                <Button bsSize='small' onClick={this.props.onClose}>{this.i18n('cancel')}</Button>
            </Modal.Footer>
        </Modal>;
    }

    _renderLevels() {
        var finding = this.state.finding,
            levels = [];
        for (var i = 1; i <= Constants.FINDING_LEVEL_MAX; i++) {
            levels.push(<div className='col-xs-2' key={'level_' + i}>
                <Input type='radio' name='level' value={i} checked={finding.level === i}
                       onChange={this._onLevelSelected}
                       label={this.props.formatMessage('audit.finding.level', {level: i})}/>
            </div>);
        }
        return levels;
    }
}

export default injectIntl(I18nWrapper(AuditFinding));
