'use strict';

import React from "react";
import {Button, Col, Grid, Row} from "react-bootstrap";
import Dropzone from "react-dropzone";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";

const ImportReportDashboard = (props) => {
        var i18n = props.i18n;
        return <Grid fluid={true}>
            <Row>
                <Col xs={6} className='dashboard-sector left'>
                    <Dropzone onDrop={props.import} multiple={false} className='dropzone'
                              activeClassName='dropzone-active' title={i18n('dropzone-tooltip')}>
                        <h4>{i18n('dashboard.import.import-e5')}</h4>
                        <div className='message'>{i18n('dropzone.title')}</div>
                    </Dropzone>
                </Col>
            </Row>
            <Row>
                <Col xs={6}>
                    <Button bsSize='large' bsStyle='default' onClick={props.goBack}>{i18n('back')}</Button>
                </Col>
            </Row>
        </Grid>;
};

ImportReportDashboard.propTypes = {
    goBack: React.PropTypes.func.isRequired,
    import: React.PropTypes.func.isRequired
};

export default injectIntl(I18nWrapper(ImportReportDashboard));
