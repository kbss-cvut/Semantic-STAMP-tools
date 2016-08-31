'use strict';

import React from "react";
import JsonLdUtils from "jsonld-utils";
import {Button, Glyphicon, Panel, Table} from "react-bootstrap";
import Actions from "../../../actions/Actions";
import AuditFinding from "./AuditFinding";
import CollapsibleText from "../../CollapsibleText";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import OptionsStore from "../../../stores/OptionsStore";
import Utils from "../../../utils/Utils";

class AuditFindings extends React.Component {
    static propTypes = {
        audit: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            showWindow: false,
            findingType: JsonLdUtils.processTypeaheadOptions(OptionsStore.getOptions('auditType')),
            currentFinding: null
        };
    }

    componentDidMount() {
        if (this.state.findingType.length === 0) {
            Actions.loadOptions('findingType');
        }
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    _onOptionsLoaded(type, data) {
        if (type === 'auditType') {
            this.setState({auditType: JsonLdUtils.processTypeaheadOptions(data)});
        }
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onAddFinding = () => {
        var finding = {
            isNew: true
        };
        this.setState({
            showWindow: true,
            currentFinding: finding
        });
    };

    _onEditFinding = (finding) => {
        this.setState({
            showWindow: true,
            currentFinding: finding
        });
    };

    _onDeleteFinding = (finding) => {
        var findings = this.props.audit.findings.slice(),
            removeIndex = findings.indexOf(finding);
        findings.splice(removeIndex, 1);
        this.props.onChange({findings: findings});
    };

    _onEditFinish = (finding) => {
        var findings = this.props.audit.findings ? this.props.audit.findings.slice() : [],
            updateIndex = findings.indexOf(this.state.currentFinding);
        if (finding.isNew) {
            delete finding.isNew;
            findings.push(finding);
        } else {
            findings.splice(updateIndex, 1, finding);
        }
        this.props.onChange({findings: findings});
        this._onEditClose();
    };

    _onEditClose = () => {
        this.setState({
            showWindow: false,
            currentFinding: null
        });
    };

    _hasFindings() {
        return this.props.audit.findings && this.props.audit.findings.length > 0;
    }

    render() {
        var addClass = this._hasFindings() ? 'float-right' : '';
        return <div>
            <AuditFinding show={this.state.showWindow} finding={this.state.currentFinding} onSave={this._onEditFinish}
                          onClose={this._onEditClose}/>
            <Panel header={<h5>{this.i18n('audit.findings.header')}</h5>} bsStyle='info'>
                {this._renderPanelContent()}
                <div className={addClass}>
                    <Button bsStyle='primary' bsSize='small' onClick={this._onAddFinding}
                            title={this.i18n('audit.findings.add-tooltip')}>
                        <Glyphicon glyph='plus' className='add-icon-glyph'/>
                        {this.i18n('add')}
                    </Button>
                </div>
            </Panel>
        </div>;
    }

    _renderPanelContent() {
        if (!this._hasFindings()) {
            return <div className='italics form-group'>{this.i18n('audit.findings.no-findings-message')}</div>;
        }
        return this._renderFindingsTable();
    }

    _renderFindingsTable() {
        return <Table striped bordered condensed hover>
            <thead>
            <tr>
                <th className='col-xs-7 content-center'>{this.i18n('audit.findings.table.description')}</th>
                <th className='col-xs-2 content-center'>{this.i18n('audit.findings.table.type')}</th>
                <th className='col-xs-1 content-center'>{this.i18n('audit.findings.table.level')}</th>
                <th className='col-xs-2 content-center'>{this.i18n('table-actions')}</th>
            </tr>
            </thead>
            <tbody>
            {this._renderFindingRows()}
            </tbody>
        </Table>;
    }

    _renderFindingRows() {
        var findings = this.props.audit.findings,
            rows = [];
        for (var i = 0, len = findings.length; i < len; i++) {
            rows.push(<FindingRow key={'finding_' + i} finding={findings[i]} onEdit={this._onEditFinding}
                                  onDelete={this._onDeleteFinding} findingType={this.state.findingType}/>);
        }
        return rows;
    }
}

var FindingRow = (props) => {
    var finding = props.finding,
        type = Utils.resolveType(finding.types, props.findingType);
    return <tr>
        <td className='report-row'><CollapsibleText text={finding.description}/></td>
        <td className='report-row content-center'>{type ? type.name : ''}</td>
        <td className='report-row content-center'>{finding.level}</td>
        <td className='report-row actions'>
            <Button bsStyle='primary' bsSize='small' title={props.i18n('audit.findings.table.open-tooltip')}
                    onClick={() => props.onEdit(finding)}>{props.i18n('open')}</Button>
            <Button bsStyle='warning' bsSize='small' title={props.i18n('audit.findings.table.delete-tooltip')}
                    onClick={() => props.onDelete(finding)}>{props.i18n('delete')}</Button>
        </td>
    </tr>
};

FindingRow.propTypes = {
    finding: React.PropTypes.object.isRequired,
    onEdit: React.PropTypes.func.isRequired,
    onDelete: React.PropTypes.func.isRequired,
    findingType: React.PropTypes.array
};

FindingRow = injectIntl(I18nWrapper(FindingRow));

export default injectIntl(I18nWrapper(AuditFindings));
