/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;

var injectIntl = require('../../utils/injectIntl');
var Ajax = require('../../utils/Ajax');

//var ReportsFilter = require('./ReportsFilter');
//var ReportsTable = require('./StatisticsTable');
var Mask = require('./../Mask');
var Routing = require('../../utils/Routing');
var I18nMixin = require('../../i18n/I18nMixin');
var Logger = require('../../utils/Logger');

var request = require('superagent');

var Statistics = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
        return {
            result: 'xxxxxxx'
        }
    },

    componentDidMount: function () {
        var sparqlQuery =
            "PREFIX : <http://krizik.felk.cvut.cz/ontologies/inbas-2015#> SELECT ?cause ?result (COUNT(*) AS ?count) { {SELECT ?f1 ?result {?f1 a :Factor ; :hasEventTypeAssessment/:hasEventType/rdfs:label ?result . }} {SELECT ?f2 ?cause {?f2 a :Factor ; :hasEventTypeAssessment/:hasEventType/rdfs:label ?cause . }} ?f1 :hasCause ?f2 .} GROUP BY ?cause ?result";

        request.get("http://martin.inbas.cz/openrdf-sesame/repositories/reports-fd-2016-02-02?query=" + encodeURIComponent(sparqlQuery)).end(function (err, resp) {
            var data = resp.text;
            Logger.log(data);
            this.setState({result: data});
        }.bind(this));
    },

    //createReport: function () {
    //    Routing.transitionToHome();
    //},


    render: function () {
        //var reports = this.props.reports;
        //if (reports === null) {
        //    return (
        //        <Mask text={this.i18n('reports.loading-mask')}/>
        //    );
        //}
        //return (
        //    <Panel header={<h3>{this.i18n('reports.panel-title')}</h3>} bsStyle='primary'>
        //        <ReportsFilter onFilterChange={this.props.actions.onFilterChange}/>
        //        {this.renderReports()}
        //    </Panel>);


        return <div>{this.state.result}</div>;

    },

    //renderReports: function () {
    //    if (this.props.reports.length === 0) {
    //        if (this.props.filter) {
    //            return <div className='no-reports-notice
    // italics'>{this.i18n('reports.filter.no-matching-found')}</div>; } else { return ( <div
    // className='no-reports-notice italics'> {this.i18n('reports.no-reports')} <a href='#' onClick={this.createReport}
    // title={this.i18n('reports.no-reports.link-tooltip')}> {this.i18n('reports.no-reports.link')} </a> </div>); } }
    // return <ReportsTable {...this.props}/> }
});

module.exports = injectIntl(Statistics);
