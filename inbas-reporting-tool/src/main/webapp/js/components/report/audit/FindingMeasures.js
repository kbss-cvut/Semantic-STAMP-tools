'use strict';

import React from "react";
import {Button, Dropdown, Glyphicon, MenuItem, Panel, Table} from "react-bootstrap";
import CollapsibleText from "../../CollapsibleText";
import Constants from "../../../constants/Constants";
import CorrectiveMeasure from "../../correctivemeasure/CorrectiveMeasure";
import ExistingMeasureSelector from "./ExistingMeasureSelector";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Utils from "../../../utils/Utils";

const MEASURE_ATTRIBUTES = [Constants.CORRECTIVE_MEASURE.DESCRIPTION, Constants.CORRECTIVE_MEASURE.DEADLINE,
    Constants.CORRECTIVE_MEASURE.IMPLEMENTED];

class FindingMeasures extends React.Component {
    static propTypes = {
        audit: React.PropTypes.object,
        finding: React.PropTypes.object,
        correctiveMeasures: React.PropTypes.array,
        onChange: React.PropTypes.func.isRequired
    };

    static defaultProps = {
        correctiveMeasures: []
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            showWindow: false,
            showMeasureSelect: false,
            currentMeasure: null
        };
    }

    _onAddMeasure = () => {
        var measure = {
            deadline: Date.now(),
            isNew: true
        };
        this._onEditMeasure(measure);
    };

    _onSelectMeasure = () => {
        this.setState({showMeasureSelect: true});
    };

    _onMeasureSelected = (measure) => {
        var measures = this.props.correctiveMeasures ? this.props.correctiveMeasures.slice() : [];
        measures.push(measure);
        this.props.onChange({correctiveMeasures: measures});
    };

    _onEditMeasure = (measure) => {
        this.setState({showWindow: true, showMeasureSelect: false, currentMeasure: measure});
    };

    _onEditFinished = (measure) => {
        var measures = this.props.correctiveMeasures ? this.props.correctiveMeasures.slice() : [];
        if (measure.isNew) {
            measures.push(measure);
        } else {
            var indexToUpdate = measures.indexOf(this.state.currentMeasure);
            measures.splice(indexToUpdate, 1, measure);
        }
        this.props.onChange({correctiveMeasures: measures});
        this._onEditClose();
    };

    _onEditClose = () => {
        this.setState({showWindow: false, currentMeasure: null});
    };

    _onDeleteMeasure = (measure) => {
        var measures = this.props.correctiveMeasures.slice(),
            indexToRemove = measures.indexOf(measure);
        measures.splice(indexToRemove, 1);
        this.props.onChange({correctiveMeasures: measures});
    };

    _hasMeasures() {
        return this.props.correctiveMeasures && this.props.correctiveMeasures.length > 0;
    }

    render() {
        return <div>
            <CorrectiveMeasure correctiveMeasure={this.state.currentMeasure} show={this.state.showWindow}
                               onSave={this._onEditFinished} onClose={this._onEditClose}
                               attributes={MEASURE_ATTRIBUTES}/>

            <Panel header={<h5>{this.i18n('report.corrective.panel-title')}</h5>} bsStyle='info'>
                {this._renderPanelContent()}
                <div className={this._hasMeasures() > 0 ? 'float-right' : ''}>
                    <Dropdown id='finding-measures-add'>
                        <Dropdown.Toggle className='btn btn-sm btn-primary'>
                            <Glyphicon glyph='plus' className='add-icon-glyph'/>
                            {this.i18n('add')}
                        </Dropdown.Toggle>
                        <Dropdown.Menu>
                            <MenuItem onClick={this._onSelectMeasure}
                                      title={this.i18n('audit.finding.measures.add.select-existing-tooltip')}>
                                {this.i18n('audit.finding.measures.add.select-existing')}</MenuItem>
                            <MenuItem onClick={this._onAddMeasure}
                                      title={this.props.i18n('audit.finding.measures.add.create-new-tooltip')}>
                                {this.i18n('audit.finding.measures.add.create-new')}
                            </MenuItem>
                        </Dropdown.Menu>
                    </Dropdown>
                </div>
                <div style={{clear: 'both', margin: '0.5em 0 0 0'}}/>
                <div className='float-right row col-xs-3'>
                    {this._renderMeasureSelect()}
                </div>
            </Panel>
        </div>;
    }

    _renderPanelContent() {
        if (!this._hasMeasures()) {
            return <div className='italics form-group'>{this.i18n('audit.finding.measures.no-measures-message')}</div>;
        } else {
            return this._renderMeasuresTable();
        }
    }

    _renderMeasuresTable() {
        return <Table striped bordered condensed hover>
            <thead>
            <tr>
                <th className='col-xs-7 content-center'>{this.i18n('audit.finding.measures.description')}</th>
                <th className='col-xs-2 content-center'>{this.i18n('audit.finding.measures.deadline')}</th>
                <th className='col-xs-1 content-center'>{this.i18n('audit.finding.measures.implemented')}</th>
                <th className='col-xs-2 content-center'>{this.i18n('table-actions')}</th>
            </tr>
            </thead>
            <tbody>
            {this._renderMeasureRows()}
            </tbody>
        </Table>;
    }

    _renderMeasureRows() {
        var measures = this.props.correctiveMeasures,
            rows = [];
        for (var i = 0, len = measures.length; i < len; i++) {
            rows.push(<MeasureRow key={'measure_' + i} measure={measures[i]} onEdit={this._onEditMeasure}
                                  onDelete={this._onDeleteMeasure}/>);
        }
        return rows;
    }

    _renderMeasureSelect() {
        if (!this.state.showMeasureSelect) {
            return null;
        }
        return <ExistingMeasureSelector audit={this.props.audit} finding={this.props.finding}
                                        onChange={this._onMeasureSelected}/>;
    }
}

var MeasureRow = (props) => {
    var measure = props.measure,
        formattedDeadline = Utils.formatDate(measure.deadline ? new Date(measure.deadline) : null);
    return <tr>
        <td className='report-row'><CollapsibleText text={measure.description}/></td>
        <td className='report-row content-center'>{formattedDeadline}</td>
        <td className='report-row content-center'>
            <Glyphicon glyph={measure.implemented ? 'ok-sign' : 'remove-sign'}
                       title={props.i18n(measure.implemented ? 'audit.finding.measures.implemented.yes' : 'audit.finding.measures.implemented.no')}/>
        </td>
        <td className='report-row actions'>
            <Button bsStyle='primary' bsSize='small' title={props.i18n('audit.finding.measures.open-tooltip')}
                    onClick={() => props.onEdit(measure)}>{props.i18n('open')}</Button>
            <Button bsStyle='warning' bsSize='small' title={props.i18n('audit.finding.measures.delete-tooltip')}
                    onClick={() => props.onDelete(measure)}>{props.i18n('remove')}</Button>
        </td>
    </tr>
};

MeasureRow.propTypes = {
    measure: React.PropTypes.object.isRequired,
    onEdit: React.PropTypes.func.isRequired,
    onDelete: React.PropTypes.func.isRequired
};

MeasureRow = injectIntl(I18nWrapper(MeasureRow));

export default injectIntl(I18nWrapper(FindingMeasures));
