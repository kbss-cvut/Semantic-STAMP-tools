'use strict';

import React from "react";
import {Button, Modal} from "react-bootstrap";
import DateTimePicker from "kbss-react-bootstrap-datetimepicker";
import assign from "object-assign";
import Constants from "../../constants/Constants";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Input from "../Input";


const ALL_ATTRIBUTES = [Constants.CORRECTIVE_MEASURE.DESCRIPTION, Constants.CORRECTIVE_MEASURE.DEADLINE,
    Constants.CORRECTIVE_MEASURE.IMPLEMENTED];

class CorrectiveMeasure extends React.Component {
    static propTypes = {
        correctiveMeasure: React.PropTypes.object,
        show: React.PropTypes.bool.isRequired,
        onSave: React.PropTypes.func.isRequired,
        onClose: React.PropTypes.func.isRequired,
        attributes: React.PropTypes.array
    };
    //attributes specify selection of attributes to display. If the array is empty, all attributes are displayed

    static defaultProps = {
        correctiveMeasure: {},
        attributes: ALL_ATTRIBUTES
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            correctiveMeasure: this.props.correctiveMeasure ? assign({}, this.props.correctiveMeasure) : {
                deadline: Date.now()
            }
        };
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.correctiveMeasure && nextProps.correctiveMeasure !== this.props.correctiveMeasure) {
            this.setState({correctiveMeasure: assign({}, nextProps.correctiveMeasure)});
        }
    }

    _isAttributeEnabled(att) {
        return this.props.attributes.indexOf(att) !== -1;
    }

    _onChange = (change) => {
        var measure = assign({}, this.state.correctiveMeasure, change);
        this.setState({correctiveMeasure: measure});
    };

    render() {
        var measure = this.state.correctiveMeasure;
        return <Modal show={this.props.show} bsSize='large' animation={true} dialogClassName='large-modal'
                      onHide={this.props.onClose}>
            <Modal.Header closeButton>
                <Modal.Title>{this.i18n('correctivemeasure.title')}</Modal.Title>
            </Modal.Header>
            <div className='modal-body'>
                {this._renderDescription()}
                {this._renderImplemented()}
                {this._renderDeadline()}
            </div>
            <Modal.Footer>
                <Button bsSize='small' bsStyle='success'
                        onClick={() => this.props.onSave(measure)}>{this.i18n('save')}</Button>
                <Button bsSize='small' onClick={this.props.onClose}>{this.i18n('cancel')}</Button>
            </Modal.Footer>
        </Modal>;
    }

    _renderDescription() {
        var measure = this.state.correctiveMeasure;
        if (!this._isAttributeEnabled(Constants.CORRECTIVE_MEASURE.DESCRIPTION)) {
            return null;
        }
        return <div className='row'>
            <div className='col-xs-12'>
                <Input type='textarea' value={measure.description ? measure.description : ''}
                       label={this.i18n('description') + '*'} rows={8}
                       placeholder={this.i18n('correctivemeasure.description.placeholder')}
                       title={this.i18n('correctivemeasure.description.tooltip')}
                       onChange={(e) => this._onChange({description: e.target.value})}/>
            </div>
        </div>;
    }

    _renderImplemented() {
        var measure = this.state.correctiveMeasure;
        if (!this._isAttributeEnabled(Constants.CORRECTIVE_MEASURE.IMPLEMENTED)) {
            return null;
        }
        return <div className='row'>
            <div className='col-xs-4'>
                <Input type='checkbox' value='implemented' checked={measure.implemented}
                       label={this.i18n('audit.finding.measures.implemented')}
                       onChange={(e) => this._onChange({implemented: e.target.checked})}/>
            </div>
        </div>;
    }

    _renderDeadline() {
        var measure = this.state.correctiveMeasure;
        if (!this._isAttributeEnabled(Constants.CORRECTIVE_MEASURE.DEADLINE)) {
            return null;
        }
        return <div className='row'>
            <div className='picker-container form-group form-group-sm col-xs-4'>
                <label className='control-label'>{this.i18n('correctivemeasure.deadline')}</label>
                <DateTimePicker inputFormat='DD-MM-YY HH:mm' dateTime={measure.deadline.toString()}
                                onChange={(val) => this._onChange({endDate: Number(val)})}
                                inputProps={{title: this.i18n('correctivemeasure.deadline.tooltip'), bsSize: 'small'}}/>
            </div>
        </div>;
    }
}

export default injectIntl(I18nWrapper(CorrectiveMeasure));
