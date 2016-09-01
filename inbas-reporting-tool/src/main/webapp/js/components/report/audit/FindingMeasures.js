'use strict';

import React from "react";
import {Button, Glyphicon, Panel, Table} from "react-bootstrap";
import CollapsibleText from "../../CollapsibleText";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Utils from "../../../utils/Utils";

class FindingMeasures extends React.Component {
    static propTypes = {
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
            currentMeasure: null
        };
    }

    _onAddMeasure = () => {
        var measure = {
            isNew: true
        };
        this._onEditMeasure(measure);
    };

    _onEditMeasure = (measure) => {
        this.setState({showWindow: true, currentMeasure: measure});
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
        return <Panel header={<h5>{this.i18n('report.corrective.panel-title')}</h5>} bsStyle='info'>
            <div className={this._hasMeasures() > 0 ? 'float-right' : ''}>
                {this._renderPanelContent()}
                <Button bsStyle='primary' bsSize='small' onClick={this._onAddMeasure}
                        title={this.props.i18n('report.corrective.add-tooltip')}>
                    <Glyphicon glyph='plus' className='add-icon-glyph'/>
                    {this.props.i18n('add')}
                </Button>
            </div>
        </Panel>;
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
}

var MeasureRow = (props) => {
    var measure = props.measure,
        formattedDeadline = Utils.formatDate(measure.deadline ? new Date(measure.deadline) : null);
    return <tr>
        <td className='report-row'><CollapsibleText text={measure.description}/></td>
        <td className='report-row content-center'>{formattedDeadline}</td>
        <td className='report-row content-center'>
            <Glyphicon glyph={measure.implemented ? 'ok-sign' : 'remove-sign'}/>
        </td>
        <td className='report-row actions'>
            <Button bsStyle='primary' bsSize='small' title={props.i18n('audit.finding.measures.open-tooltip')}
                    onClick={() => props.onEdit(measure)}>{props.i18n('open')}</Button>
            <Button bsStyle='warning' bsSize='small' title={props.i18n('audit.finding.measures.delete-tooltip')}
                    onClick={() => props.onDelete(measure)}>{props.i18n('delete')}</Button>
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
