'use strict';

import React from "react";
import {Button, Glyphicon} from "react-bootstrap";
import Typeahead from "react-bootstrap-typeahead";
import Actions from "../../actions/Actions";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import ReportType from "../../model/ReportType";
import ReportStore from "../../stores/ReportStore";
import Routes from "../../utils/Routes";
import Routing from "../../utils/Routing";
import ReportSearchResultList from "../typeahead/ReportSearchResultList";
import Utils from "../../utils/Utils";

const OPTION_IDENTIFICATION_THRESHOLD = 45;

class NavSearch extends React.Component {
    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            options: NavSearch._processReports(ReportStore.getReports()),
            fullTextDisabled: true
        }
    }

    componentDidMount() {
        if (this.state.options.length === 0) {
            Actions.loadAllReports();
        }
        this.unsubscribe = ReportStore.listen(this._onReportsLoaded);
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onReportsLoaded = (data) => {
        if (data.action !== Actions.loadAllReports) {
            return;
        }
        this.setState({options: NavSearch._processReports(data.reports)});
    };

    static _processReports(reports) {
        if (!reports) {
            return [];
        }
        var options = [],
            option;
        for (var i = 0, len = reports.length; i < len; i++) {
            option = ReportType.getReport(reports[i]);
            option.description = option.identification;
            options.push(option);
        }
        return options;
    }

    _onOptionSelected = (report) => {
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: report.key},
            handlers: {onCancel: Routes.reports}
        });
        this.typeahead.setEntryText('');
    };

    _onSearchTextChange = (e) => {
        this.setState({fullTextDisabled: e.target.value.length === 0});
    };

    render() {
        var classes = {
                input: 'navbar-search-input',
                results: 'navbar-search-results list-unstyled'
            },
            optionLabel = this._getOptionLabelFunction();

        return <div className='navbar-search-panel'>
            <div className='col-xs-11 navbar-search'>
                <Typeahead ref={(c) => this.typeahead = c} size='small' name={this.props.name}
                           formInputOption='id'
                           placeholder={this.i18n('main.search-placeholder')}
                           onOptionSelected={this._onOptionSelected} onChange={this._onSearchTextChange}
                           filterOption='identification'
                           displayOption={optionLabel} options={this.state.options}
                           customClasses={classes}
                           customListComponent={ReportSearchResultList}/>
            </div>
            <div className='col-xs-1 navbar-search'>
                <Button bsSize='small' title={this.i18n('main.search.fulltext-tooltip')}
                        disabled={this.state.fullTextDisabled}>
                    <Glyphicon glyph='search'/>
                </Button>
            </div>
        </div>;
    }

    _getOptionLabelFunction() {
        return function (option) {
            var date = option.date ? Utils.formatDate(new Date(option.date)) : '',
                label = option.identification.length > OPTION_IDENTIFICATION_THRESHOLD ?
                option.identification.substring(0, OPTION_IDENTIFICATION_THRESHOLD) + '...' : option.identification;

            return label + ' (' + date + ')';
        }.bind(this);
    }
}

export default injectIntl(I18nWrapper(NavSearch));
