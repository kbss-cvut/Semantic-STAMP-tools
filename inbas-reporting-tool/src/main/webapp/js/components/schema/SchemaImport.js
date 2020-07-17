'use strict';

import React from "react";
import {Button, Modal} from "react-bootstrap";
import Actions from "../../actions/Actions";
import Input from "../Input";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import LoadingWrapper from "../misc/hoc/LoadingWrapper";
import MessageWrapper from "../misc/hoc/MessageWrapper";
// import SchemaStore from "../../stores/SchemaStore";

class SchemaImport extends React.Component {
    static propTypes = {
        onImportFinish: React.PropTypes.func.isRequired,
        onCancel: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            content: null
        };
    }

    // componentDidMount() {
    //     this.input.focus();
    // }

    _onChange = (e) => {
        if(e.target && e.target.files && e.target.files.length > 0)
            this.setState({content: e.target.files[0]});
    };

    _onReplace = () => {
        // This will be replaced by a call to backend, which will execute analysis of the text content
        this.props.loadingOn(this.i18n('schema.import.importing-msg'));
        Actions.replaceSchema({file: this.state.content}, this._onImportSuccess, this._onImportError);
    };

    _onMerge = () => {
        // This will be replaced by a call to backend, which will execute analysis of the text content
        this.props.loadingOn(this.i18n('schema.import.importing-msg'));
        Actions.mergeSchema({file: this.state.content}, this._onImportSuccess, this._onImportError);
    };

    _onImportSuccess = (report) => {
        this.props.loadingOff();
        this.props.onImportFinish(report);
    };

    _onImportError = (err) => {
        this.props.loadingOff();
        this.props.showErrorMessage(err.message);
        this.setState({content : null} )
    };

    render() {
        return <Modal show={true} bsSize="large" onHide={this.props.onCancel} animation={true}>
            <Modal.Header closeButton>
                <Modal.Title>{this.i18n('schema.import.title')}</Modal.Title>
            </Modal.Header>
            <Modal.Body ref={c => this.modalBody = c}>
                <input type="file" name="file" onChange={this._onChange}/>
            </Modal.Body>
            <Modal.Footer ref={c => this.modalFooter = c}>
                <Button onClick={this._onReplace} bsStyle='primary' bsSize='small'
                        disabled={!this.state.content}>
                    {/*{this.i18n('schema.import.run')}*/}
                    {this.i18n('schema.import.replace')}
                </Button>
                <Button onClick={this._onMerge} bsStyle='primary' bsSize='small'
                        disabled={!this.state.content}>
                    {this.i18n('schema.import.merge')}
                </Button>
                <Button onClick={this.props.onCancel} bsSize='small'>{this.i18n('cancel')}</Button>
            </Modal.Footer>
        </Modal>;
    }
}

export default injectIntl(MessageWrapper(LoadingWrapper(I18nWrapper(SchemaImport))));
