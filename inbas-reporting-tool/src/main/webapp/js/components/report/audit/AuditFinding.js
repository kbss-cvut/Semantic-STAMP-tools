'use strict';

import React from "react";
import JsonLdUtils from "jsonld-utils";
import assign from "object-assign";
import {Button, ButtonToolbar, DropdownButton, MenuItem, Modal} from "react-bootstrap";
import Typeahead from "react-bootstrap-typeahead";
import Constants from "../../../constants/Constants";
import FindingFactors from "./FindingFactors";
import FindingMeasures from "./FindingMeasures";
import HelpIcon from "../../misc/HelpIcon";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Input from "../../Input";
import OptionsStore from "../../../stores/OptionsStore";
import Routes from "../../../utils/Routes";
import Routing from "../../../utils/Routing";
import SafetyIssueSelector from "../safetyissue/SafetyIssueSelector";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";
import Utils from "../../../utils/Utils";
import Vocabulary from "../../../constants/Vocabulary";

const INDEX_PROPERTY = 'http://onto.fel.cvut.cz/ontologies/aviation/cz/caa/cat/audit/checklist/has_full_order';

class AuditFinding extends React.Component {
    static propTypes = {
        audit: React.PropTypes.object,
        finding: React.PropTypes.object,
        onSave: React.PropTypes.func.isRequired,
        onClose: React.PropTypes.func.isRequired,
        report: React.PropTypes.object
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            finding: props.finding ? assign({}, props.finding) : null,
            findingType: this._processOptions(OptionsStore.getOptions('findingType')),
            showSafetyIssueSelector: false
        };
    }

    _processOptions(options) {
        // TODO Find a generic solution to option sorting
        options.sort((a, b) => {
            return a[INDEX_PROPERTY]['@value'].localeCompare(b[INDEX_PROPERTY]['@value']);
        });
        return JsonLdUtils.processTypeaheadOptions(options);
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    _onOptionsLoaded = (type, data) => {
        if (type === 'findingType') {
            this.setState({findingType: this._processOptions(data)});
        }
    };

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

    _onOpenSafetyIssueSelector = () => {
        this.setState({showSafetyIssueSelector: true});
    };

    _onCreateSafetyIssue = () => {
        Routing.transitionTo(Routes.createReport, {
            payload: {
                reportType: Vocabulary.SAFETY_ISSUE_REPORT,
                basedOn: {
                    event: this.props.finding,
                    report: this.props.report
                }
            }
        });
    };

    render() {
        var finding = this.state.finding;
        if (!finding) {
            return null;
        }
        var findingType = this._resolveFindingType();
        return <Modal show={this.props.show} bsSize='large' animation={true} dialogClassName='large-modal'
                      onHide={this.props.onClose}>
            <Modal.Header closeButton>
                <Modal.Title>{this.i18n('audit.finding.header')}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className='row'>
                    <div className='col-xs-12'>
                        <label className='control-label'>{this.i18n('audit.finding.type.label')}</label>
                        <Typeahead formInputOption='id' size='small'
                                   placeholder={this.i18n('audit.finding.type.placeholder')}
                                   onOptionSelected={this._onTypeSelected} filterOption='name' displayOption='name'
                                   value={findingType} options={this.state.findingType}
                                   optionsButton={true} inputProps={{title: findingType}}
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
                <div className='row'>
                    <div className='col-xs-12'>
                        <FindingMeasures audit={this.props.audit} finding={finding}
                                         correctiveMeasures={finding.correctiveMeasures} onChange={this._mergeChange}/>
                    </div>
                </div>
            </Modal.Body>
            <Modal.Footer ref={comp => this._modalFooter = comp}>
                <ButtonToolbar className='pull-right'>
                    <Button bsSize='small' bsStyle='success'
                            onClick={() => this.props.onSave(finding)}>{this.i18n('save')}</Button>
                    <Button bsSize='small' onClick={this.props.onClose}>{this.i18n('cancel')}</Button>
                    {this._renderCreateSafetyIssueButton()}
                </ButtonToolbar>
                <div style={{clear: 'both'}}/>
                {this._renderSafetyIssueSelector()}
            </Modal.Footer>
        </Modal>;
    }

    _renderLevels() {
        var finding = this.state.finding,
            levels = [];
        for (var i = 1; i <= Constants.FINDING_LEVEL_MAX; i++) {
            levels.push(<div className='col-xs-2' key={'level_' + i}>
                <div className='row'>
                    <div className='col-xs-8'>
                        <Input type='radio' name='level' value={i} checked={finding.level === i}
                               onChange={this._onLevelSelected}
                               label={this.props.formatMessage('audit.finding.level', {level: i})}/>
                    </div>
                    <div className='col-xs-1'>
                        <HelpIcon text={this.i18n('audit.finding.level-' + i + '.help')}/>
                    </div>
                </div>
            </div>);
        }
        return levels;
    }

    _renderCreateSafetyIssueButton() {
        if (this.props.report.isNew || (this.props.finding && !this.props.finding.uri)) {
            return null;
        }
        return <DropdownButton id='safetyIssueSelector' bsStyle='primary' bsSize='small'
                               title={this.i18n('safetyissuereport.label')}
                               pullRight={true}>
            <MenuItem onClick={this._onCreateSafetyIssue}
                      title={this.i18n('occurrencereport.create-safety-issue-tooltip')}>{this.i18n('occurrencereport.create-safety-issue')}</MenuItem>
            <MenuItem onClick={this._onOpenSafetyIssueSelector}
                      title={this.i18n('occurrencereport.add-as-safety-issue-base-tooltip')}>{this.i18n('occurrencereport.add-as-safety-issue-base')}</MenuItem>
        </DropdownButton>;
    }

    _renderSafetyIssueSelector() {
        return this.state.showSafetyIssueSelector ?
            <div className='float-right row col-xs-3 issue-selector'>
                <SafetyIssueSelector report={this.props.report} event={this.props.finding}/>
            </div> : null;
    }
}

export default injectIntl(I18nWrapper(AuditFinding));
