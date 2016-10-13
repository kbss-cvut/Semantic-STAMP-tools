'use strict';

var React = require('react');
var Well = require('react-bootstrap').Well;

var injectIntl = require('../../utils/injectIntl');
var FormattedMessage = require('react-intl').FormattedMessage;

var CreateReportDashboard = require('./CreateReportDashboard').default;
var DashboardStatistics = require('../statistics/DashboardStatistics').default;
var ImportReport = require('./ImportReport').default;
var RecentlyEdited = require('./RecentlyEditedReports');
var UnprocessedReports = require('./UnprocessedReports').default;
var I18nMixin = require('../../i18n/I18nMixin');

var Dashboard = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        createEmptyReport: React.PropTypes.func.isRequired,
        importE5Report: React.PropTypes.func.isRequired,
        showAllReports: React.PropTypes.func.isRequired,
        openReport: React.PropTypes.func.isRequired,
        userFirstName: React.PropTypes.string,
        statistics: React.PropTypes.func
    },

    getInitialState: function () {
        return {
            showImport: false
        }
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    importReport: function () {
        this.setState({showImport: true});
    },

    cancelImport: function () {
        this.setState({showImport: false});
    },


    render: function () {
        return <div style={{margin: '0 -15px 0 -15px'}}>
            <ImportReport import={this.props.importE5Report} onClose={this.cancelImport} show={this.state.showImport}/>
            <div className='col-xs-8'>
                {this._renderDashboard()}
                <DashboardStatistics/>
            </div>
            <div className='col-xs-4'>
                <div>
                    <RecentlyEdited reports={this.props.reports} onOpenReport={this.props.openReport}/>
                </div>
                <div>
                    <UnprocessedReports />
                </div>
            </div>
        </div>;
    },

    _renderDashboard: function () {
        return <Well>
            <h3 className='dashboard-welcome'>
                <FormattedMessage id='dashboard.welcome'
                                  values={{name: <span className='bold'>{this.props.userFirstName}</span>}}/>
            </h3>
            <CreateReportDashboard createReport={this.props.createEmptyReport} importReport={this.importReport}/>
        </Well>;
    }
});

module.exports = injectIntl(Dashboard);
