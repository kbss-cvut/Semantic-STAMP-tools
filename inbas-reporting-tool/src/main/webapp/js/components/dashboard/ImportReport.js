'use strict';

import React from "react";
import {Alert, Col, Grid, Modal, Row} from "react-bootstrap";
import Dropzone from "react-dropzone";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import Mask from "../Mask";

class ImportReport extends React.Component {
    static propTypes = {
        show: React.PropTypes.bool,
        onClose: React.PropTypes.func.isRequired,
        importHandlers: React.PropTypes.object.isRequired
    };

    static defaultProps = {
        show: false
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            uploading: false,
            error: null
        }
    }

    _onImportE5 = (files) => {
        this.setState({uploading: true});
        this.props.importHandlers.importE5(files[0], this.onImportSuccess, this.onImportError);
    };

    _onImportSafa = (files) => {
        this.setState({uploading: true});
        this.props.importHandlers.importSafa(files[0], this.onImportSuccess, this.onImportError);
    };

    onImportSuccess = () => {
        this.setState({uploading: false});
    };

    onImportError = (err) => {
        this.setState({uploading: false, error: err.message});
        setTimeout(() => this._dismissMessage(), 5000);
    };

    _dismissMessage = () => {
        this.setState({error: null});
    };

    render() {
        return <Modal show={this.props.show} animation={true} onHide={this.props.onClose}>
            <Modal.Header closeButton>
                <Modal.Title>{this.i18n('dashboard.create-import-tile')}</Modal.Title>
            </Modal.Header>
            <div className='modal-body'>
                <Grid fluid={true}>
                    {this._renderProgress()}
                    <Row>
                        <Col xs={6} className='dashboard-sector'>
                            <Dropzone onDrop={this._onImportE5} multiple={false} className='dropzone'
                                      activeClassName='dropzone-active' title={this.i18n('dropzone-tooltip')}>
                                <h4>{this.i18n('dashboard.import.import-e5')}</h4>
                                <div className='message'>{this.i18n('dropzone.title')}</div>
                            </Dropzone>
                        </Col>
                        <Col xs={6} className='dashboard-sector'>
                            <Dropzone onDrop={this._onImportSafa} multiple={false} className='dropzone'
                                      activeClassName='dropzone-active' title={this.i18n('dropzone-tooltip')}>
                                <h4>{this.i18n('dashboard.import.import-safa')}</h4>
                                <div className='message'>{this.i18n('dropzone.title')}</div>
                            </Dropzone>
                        </Col>
                    </Row>
                </Grid>
            </div>
            <Modal.Footer>
                {this._renderMessage()}
            </Modal.Footer>
        </Modal>;
    }

    _renderProgress() {
        return this.state.uploading ? <Mask text={this.i18n('uploading-mask')}/> : null;
    }

    _renderMessage() {
        return this.state.error ?
            <Alert bsStyle='danger' onDismiss={this._dismissMessage}>
                <div>{this.state.error}</div>
            </Alert> : null;
    }
}

export default injectIntl(I18nWrapper(ImportReport));
