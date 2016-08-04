'use strict';

import React from "react";
import {Button, Col, Grid, Row} from "react-bootstrap";
import DashboardTile from "./DashboardTile";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";
import Vocabulary from "../../constants/Vocabulary";

const CreateReportDashboard = (props) => {
    var i18n = props.i18n;
    return <Grid fluid={true}>
        <Row>
            <Col xs={4} className='dashboard-sector left'>
                <DashboardTile onClick={() => props.createReport(Vocabulary.OCCURRENCE_REPORT)}>
                    {i18n('dashboard.create-new-occurrence-report-tile')}
                </DashboardTile>
            </Col>
            <Col xs={4} className='dashboard-sector'>
                <DashboardTile onClick={() => props.createReport(Vocabulary.SAFETY_ISSUE_REPORT)}>
                    {i18n('dashboard.create-new-safety-issue-tile')}
                </DashboardTile>
            </Col>
            <Col xs={4} className='dashboard-sector right'>
                <DashboardTile onClick={props.importReport}>{i18n('dashboard.create-import-tile')}</DashboardTile>
            </Col>
        </Row>
        <Row>
            <Col xs={6}>
                <Button bsSize='large' bsStyle='default' onClick={props.goBack}>{i18n('back')}</Button>
            </Col>
        </Row>
    </Grid>;
};

CreateReportDashboard.propTypes = {
    createReport: React.PropTypes.func.isRequired,
    importReport: React.PropTypes.func.isRequired,
    goBack: React.PropTypes.func.isRequired
};

export default injectIntl(I18nWrapper(CreateReportDashboard));
