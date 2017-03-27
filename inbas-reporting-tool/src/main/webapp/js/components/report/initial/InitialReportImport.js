'use strict';

import React from "react";
import {Button, Modal} from "react-bootstrap";
import Input from "../../Input";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";

class InitialReportImport extends React.Component {
    static propTypes = {
        onImport: React.PropTypes.func.isRequired,
        onCancel: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            content: ''
        }
    }

    componentDidMount() {
        this.input.focus();
    }

    _onChange = (e) => {
        this.setState({content: e.target.value});
    };

    _onImport = () => {
        this.props.onImport({
            description: this.state.content
        });
    };

    render() {
        return <Modal show={true} bsSize="large" onHide={this.props.onCancel} animation={true}>
            <Modal.Header closeButton>
                <Modal.Title>{this.i18n('report.initial.import.title')}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Input ref={c => this.input = c} type='textarea' rows={12}
                       label={this.i18n('report.initial.text.label')}
                       title={this.i18n('report.initial.import.text.tooltip')}
                       placeholder={this.i18n('report.initial.import.text.tooltip')}
                       value={this.state.content} onChange={this._onChange}/>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={this._onImport} bsStyle='primary' bsSize='small'
                        disabled={this.state.content.length === 0}>
                    {this.i18n('report.initial.import.run')}
                </Button>
                <Button onClick={this.props.onCancel} bsSize='small'>{this.i18n('cancel')}</Button>
            </Modal.Footer>
        </Modal>;
    }
}

export default injectIntl(I18nWrapper(InitialReportImport));
