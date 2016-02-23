/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Jumbotron = require('react-bootstrap').Jumbotron;
var Grid = require('react-bootstrap').Grid;
var Col = require('react-bootstrap').Col;
var Row = require('react-bootstrap').Row;

var injectIntl = require('../../utils/injectIntl');
var FormattedMessage = require('react-intl').FormattedMessage;

var Constants = require('../../constants/Constants');
var Tile = require('./DashboardTile');
var ReportTypeahead = require('../typeahead/ReportTypeahead');
var RecentlyEdited = require('./RecentlyEditedReports');
var I18n = require('../../i18n/I18nMixin');

var Dashboard = React.createClass({
    mixins: [I18n],

    propTypes: {
        userFirstName: React.PropTypes.string,
        createEmptyReport: React.PropTypes.func.isRequired,
        importInitialReport: React.PropTypes.func.isRequired,
        showAllReports: React.PropTypes.func.isRequired,
        openReport: React.PropTypes.func.isRequired,
        dashboard: React.PropTypes.string,
        statistics: React.PropTypes.func.isRequired
    },

    getInitialState: function () {
        return {
            dashboard: this.props.dashboard ? this.props.dashboard : Constants.DASHBOARDS.MAIN,
            search: false
        }
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    goBack: function () {
        this.setState({dashboard: Constants.DASHBOARDS.MAIN});
    },

    createReport: function () {
        this.setState({dashboard: Constants.DASHBOARDS.CREATE_REPORT});
    },

    toggleSearch: function () {
        this.setState({search: !this.state.search});
    },


    render: function () {
        return (
            <div style={{margin: '0 -15px 0 -15px'}}>
                <div className='col-xs-8'>
                    <Jumbotron>
                        {this.renderTitle()}
                        {this.renderDashboardContent()}
                    </Jumbotron>
                </div>
                <div className='col-xs-4'>
                    <RecentlyEdited reports={this.props.reports} onOpenReport={this.props.openReport}/>
                </div>
            </div>

        );
    },

    renderTitle: function () {
        if (this.state.dashboard === Constants.DASHBOARDS.MAIN) {
            return (<h3><FormattedMessage id='dashboard.welcome'
                                          values={{name: <span className='bold'>{this.props.userFirstName}</span>}}/>
            </h3>);
        } else {
            return (<h3>{this.i18n('dashboard.create-tile')}</h3>);
        }
    },

    renderDashboardContent: function () {
        if (this.state.dashboard === Constants.DASHBOARDS.MAIN) {
            return this.renderMainDashboard();
        } else {
            return this.renderCreateReportDashboard();
        }
    },

    renderMainDashboard: function () {
        var search = this.state.search ? (
            <ReportTypeahead name='reportSearch' onChange={this.props.openReport}/>) : null;
        return (
            <Grid fluid={true}>
                <Row>
                    <Col xs={4} className='dashboard-sector'>
                        <Tile onClick={this.createReport}>{this.i18n('dashboard.create-tile')}</Tile>
                    </Col>
                    <Col xs={4} className='dashboard-sector'>
                        <Tile onClick={this.toggleSearch}>{this.i18n('dashboard.search-tile')}</Tile>
                    </Col>
                    <Col xs={4} className='dashboard-sector'>
                        <Tile
                            onClick={this.props.showAllReports}>{this.i18n('dashboard.view-all-tile')}</Tile>
                    </Col>
                </Row>
                <Row>
                    <Col xs={12} className='dashboard-sector-search'>

                        {search}
                    </Col>
                </Row>
            </Grid>
        );
    },

    renderCreateReportDashboard: function () {
        return (
            <Grid fluid={true}>
                <Row>
                    <Col xs={6} className='dashboard-sector left'>
                        <Tile
                            onClick={this.props.createEmptyReport}>{this.i18n('dashboard.create-empty-tile')}</Tile>
                    </Col>
                    <Col xs={6} className='dashboard-sector right'>
                        <Tile onClick={this.props.importInitialReport}
                              disabled={true}>{this.i18n('dashboard.create-import-tile')}</Tile>
                    </Col>
                </Row>
                <Row>
                    <Col xs={6}>
                        <Button bsSize='large' bsStyle='default'
                                onClick={this.goBack}>{this.i18n('back')}</Button>
                    </Col>
                </Row>
            </Grid>
        );
    }
});

module.exports = injectIntl(Dashboard);
