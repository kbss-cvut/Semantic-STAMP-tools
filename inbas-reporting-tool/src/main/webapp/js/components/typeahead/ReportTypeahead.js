'use strict';

var React = require('react');
var Reflux = require('reflux');
var Typeahead = require('react-typeahead').Typeahead;

var Actions = require('../../actions/Actions');
var ReportsStore = require('../../stores/ReportsStore');
var TypeaheadResultList = require('./TypeaheadResultList');
var Utils = require('../../utils/Utils');

var ReportTypeahead = React.createClass({
    propTypes: {
        name: React.PropTypes.string,
        onChange: React.PropTypes.func
    },

    mixins: [Reflux.ListenerMixin],

    getInitialState: function () {
        return {
            options: []
        };
    },

    componentWillMount: function () {
        this.listenTo(ReportsStore, this.onReportsLoaded);
        Actions.loadReports();
    },

    componentDidMount: function() {
        this.refs.reportTypeahead.focus();
    },

    onReportsLoaded: function () {
        this.setState({options: ReportsStore.getReports()});
    },

    onOptionSelected: function (option) {
        this.props.onChange(option);
    },

    render: function () {
        var classes = {
            input: 'form-control dashboard-report-search',
            results: 'dashboard-report-search-results'
        };
        var optionLabel = function (option) {
            return option.name + ' (' + Utils.formatDate(new Date(option.occurrenceTime)) + ')';
        };
        return (
            <Typeahead ref='reportTypeahead' className='form-group form-group-sm' name={this.props.name}
                       formInputOption='id' placeholder='Occurrence Summary'
                       onOptionSelected={this.onOptionSelected} filterOption='name' displayOption={optionLabel}
                       options={this.state.options} customClasses={classes}
                       customListComponent={TypeaheadResultList}/>
        );
    }
});

module.exports = ReportTypeahead;
