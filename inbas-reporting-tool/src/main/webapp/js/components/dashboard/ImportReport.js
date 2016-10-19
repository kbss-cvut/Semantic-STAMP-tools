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
        import: React.PropTypes.func.isRequired
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

    _onImport = (files) => {
        this.setState({uploading: true});
        this.props.import(files[0], this.onImportSuccess, this.onImportError);
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
                        <Col xs={12} className='dashboard-sector'>
                            <Dropzone onDrop={this._onImport} multiple={false} className='dropzone'
                                      activeClassName='dropzone-active' title={this.i18n('dropzone-tooltip')}>
                                <h4>{this.i18n('dashboard.import.import-e5')}</h4>
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
