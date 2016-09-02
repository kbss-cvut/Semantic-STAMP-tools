'use strict';

import React from "react";
import {Button, Glyphicon, Panel} from "react-bootstrap";
import Constants from "../../constants/Constants";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import CorrectiveMeasure from "./CorrectiveMeasure";
import CorrectiveMeasuresTable from "./CorrectiveMeasuresTable";

// TODO Try to unify this class with FindingMeasures
class CorrectiveMeasures extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            isWizardOpen: false,
            currentMeasure: null
        }
    }

    closeWizard = () => {
        this.setState({isWizardOpen: false, currentMeasure: null});
    };

    onAdd = () => {
        var measure = {
            isNew: true,
            description: ''
        };
        this.setState({isWizardOpen: true, currentMeasure: measure});
    };

    updateCorrectiveMeasure = (measure) => {
        var measures = this.props.report.correctiveMeasures ? this.props.report.correctiveMeasures.slice() : [];
        if (measure.isNew) {
            delete measure.isNew;
            measures.push(measure);
        } else {
            measures.splice(measure.index, 1, measure);
        }
        this.props.onChange({correctiveMeasures: measures});
        this.closeWizard();
    };

    onRemove = (index) => {
        var measures = this.props.report.correctiveMeasures.slice();
        measures.splice(index, 1);
        this.props.onChange({correctiveMeasures: measures});
    };

    onEdit = (index) => {
        var measure = this.props.report.correctiveMeasures[index];
        this.setState({isWizardOpen: true, currentMeasure: measure});
    };

    render() {
        return <div>
            <CorrectiveMeasure correctiveMeasure={this.state.currentMeasure}
                               attributes={[Constants.CORRECTIVE_MEASURE.DESCRIPTION]}
                               show={this.state.isWizardOpen} onSave={this.updateCorrectiveMeasure}
                               onClose={this.closeWizard}/>
            {this.renderMeasures()}
        </div>;
    }

    renderMeasures() {
        var data = this.props.report.correctiveMeasures,
            component = null;
        if (data && data.length !== 0) {
            var handlers = {
                onRemove: this.onRemove,
                onEdit: this.onEdit
            };
            component = <CorrectiveMeasuresTable data={data} handlers={handlers}/>;
        }
        var buttonCls = component ? 'float-right' : '';
        return <Panel header={<h5>{this.props.i18n('report.corrective.panel-title')}</h5>} bsStyle='info'
                      key='correctiveMeasures'>
            {component}
            <div className={buttonCls}>
                <Button bsStyle='primary' bsSize='small' onClick={this.onAdd}
                        title={this.props.i18n('report.corrective.add-tooltip')}>
                    <Glyphicon glyph='plus' className='add-icon-glyph'/>
                    {this.props.i18n('add')}
                </Button>
            </div>
        </Panel>;
    }
}

export default injectIntl(I18nWrapper(CorrectiveMeasures));
