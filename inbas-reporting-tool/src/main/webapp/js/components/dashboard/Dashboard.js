'use strict';

import React from "react";
import {Button, Col, Grid, Jumbotron, Modal, Row} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import I18nWrapper from "../../i18n/I18nWrapper";
import InitialReportImport from "../report/initial/InitialReportImport";
import SchemaImport from "../schema/SchemaImport";
import injectIntl from "../../utils/injectIntl";
import Tile from "./DashboardTile";
import RecentlyEdited from "./RecentlyEditedReports";

class Dashboard extends React.Component {

    static propTypes = {
        createEmptyReport: React.PropTypes.func.isRequired,
        onImportFinish: React.PropTypes.func.isRequired,
        showAllReports: React.PropTypes.func.isRequired,
        openReport: React.PropTypes.func.isRequired,
        userFirstName: React.PropTypes.string
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            showImportSchema: true
        };
    }


    _onImportSchema = (file) => {
        this._closeImportSchemaDialog();
        // send file to server for import
        // this.props.onImportSchemaFinish(report);
    };

    _openImportSchemaDialog = () => {
        this.setState({showImportSchema: true});
    };

    _closeImportSchemaDialog = () => {
        this.setState({showImportSchema: false});
    };

    render() {
        return <div className='row'>
            {this.state.showImportSchema &&
            <SchemaImport onImportFinish={this._onImportSchema} onCancel={this._closeImportSchemaDialog} />}
            <div className='dashboard-left'>
                <Jumbotron>
                    {this.renderTitle()}
                    {this.renderDashboardContent()}
                </Jumbotron>
            </div>
            <div className='dashboard-right'>
                <div>
                    <RecentlyEdited reports={this.props.reports} onOpenReport={this.props.openReport}/>
                </div>
            </div>
        </div>;
    }

    renderTitle() {
        return <h3><FormattedMessage id='dashboard.welcome'
                                     values={{name: <span className='bold'>{this.props.userFirstName}</span>}}/>
        </h3>;
    }

    renderDashboardContent() {
        return this._renderMainDashboard();
    }

    _renderMainDashboard() {
        return <Grid fluid={true}>
            <Row>
                <Col xs={4} className='dashboard-sector'>
                    <Tile onClick={this.props.createEmptyReport}>{this.i18n('dashboard.create-tile')}</Tile>
                </Col>
                <Col xs={4} className='dashboard-sector'>
                    <Tile onClick={this._openImportSchemaDialog}>{this.i18n('dashboard.import-schema')}</Tile>
                </Col>
                <Col xs={4} className='dashboard-sector'>
                    <Tile
                        onClick={this.props.showAllReports}>{this.i18n('dashboard.view-all-tile')}</Tile>
                </Col>
            </Row>
        </Grid>;
    }
}

export default injectIntl(I18nWrapper(Dashboard));
