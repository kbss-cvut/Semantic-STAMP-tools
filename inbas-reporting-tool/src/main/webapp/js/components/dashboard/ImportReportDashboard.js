'use strict';

import React from "react";
import {Alert, Button, Col, Grid, Row} from "react-bootstrap";
import Dropzone from "react-dropzone";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import Mask from "../Mask";

class ImportReportDashboard extends React.Component {
    static propTypes = {
        goBack: React.PropTypes.func.isRequired,
        import: React.PropTypes.func.isRequired
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
    };

    _dismissMessage = () => {
        this.setState({error: null});
    };

    render() {
        return <div>
            <Grid fluid={true}>
                {this._renderProgress()}
                <Row>
                    <Col xs={6} className='dashboard-sector left'>
                        <Dropzone onDrop={this._onImport} multiple={false} className='dropzone'
                                  activeClassName='dropzone-active' title={this.i18n('dropzone-tooltip')}>
                            <h4>{this.i18n('dashboard.import.import-e5')}</h4>
                            <div className='message'>{this.i18n('dropzone.title')}</div>
                        </Dropzone>
                    </Col>
                </Row>
                <Row>
                    <Col xs={6}>
                        <Button bsSize='large' bsStyle='default'
                                onClick={this.props.goBack}>{this.i18n('back')}</Button>
                    </Col>
                </Row>
            </Grid>
            {this._renderMessage()}
        </div>;
    }

    _renderProgress() {
        return this.state.uploading ? <Mask text={this.i18n('uploading-mask')}/> : null;
    }

    _renderMessage() {
        return this.state.error ?
            <div className='import-error'>
                <Alert bsStyle='danger' onDismiss={this._dismissMessage} dismissAfter={5000}>
                    <div>{this.state.error}</div>
                </Alert>
            </div> : null;
    }
}

export default injectIntl(I18nWrapper(ImportReportDashboard));
