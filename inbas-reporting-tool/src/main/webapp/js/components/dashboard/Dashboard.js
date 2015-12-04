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

var IntlMixin = require('react-intl').IntlMixin;
var FormattedMessage = require('react-intl').FormattedMessage;

var Tile = require('./DashboardTile');
var ReportTypeahead = require('../typeahead/ReportTypeahead');
var RecentlyEdited = require('./RecentlyEditedReports');

var Dashboard = React.createClass({
    mixins: [IntlMixin],

    propTypes: {
        userFirstName: React.PropTypes.string,
        createEmptyReport: React.PropTypes.func.isRequired,
        importInitialReport: React.PropTypes.func.isRequired,
        showAllReports: React.PropTypes.func.isRequired,
        openReport: React.PropTypes.func.isRequired
    },

    getInitialState: function () {
        return {
            dashboard: 'main',
            search: false
        }
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    goBack: function () {
        this.setState({dashboard: 'main'});
    },

    createReport: function () {
        this.setState({dashboard: 'createReport'});
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
        if (this.state.dashboard === 'main') {
            // TODO This does not work now, bug in FormatJS
            //return (<h3><FormattedMessage message={this.getIntlMessage('dashboard.welcome')}
            //                              name={<span className='bold'>{this.props.userFirstName}</span>}/></h3>);
            var msg = this.getIntlMessage('dashboard.welcome');
            return <h3>{msg.replace('{name}', this.props.userFirstName)}</h3>;
        } else {
            return (<h3>{this.getIntlMessage('dashboard.create-tile')}</h3>);
        }
    },

    renderDashboardContent: function () {
        if (this.state.dashboard === 'main') {
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
                        <Tile onClick={this.createReport}>{this.getIntlMessage('dashboard.create-tile')}</Tile>
                    </Col>
                    <Col xs={4} className='dashboard-sector'>
                        <Tile onClick={this.toggleSearch}>{this.getIntlMessage('dashboard.search-tile')}</Tile>
                    </Col>
                    <Col xs={4} className='dashboard-sector'>
                        <Tile
                            onClick={this.props.showAllReports}>{this.getIntlMessage('dashboard.view-all-tile')}</Tile>
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
                            onClick={this.props.createEmptyReport}>{this.getIntlMessage('dashboard.create-empty-tile')}</Tile>
                    </Col>
                    <Col xs={6} className='dashboard-sector right'>
                        <Tile onClick={this.props.importInitialReport}
                              disabled={true}>{this.getIntlMessage('dashboard.create-import-tile')}</Tile>
                    </Col>
                </Row>
                <Row>
                    <Col xs={6}>
                        <Button bsSize='large' bsStyle='default'
                                onClick={this.goBack}>{this.getIntlMessage('back')}</Button>
                    </Col>
                </Row>
            </Grid>
        );
    }
});

module.exports = Dashboard;
