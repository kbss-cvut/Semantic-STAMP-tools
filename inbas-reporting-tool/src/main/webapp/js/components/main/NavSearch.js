'use strict';

import React from "react";
import {Label} from "react-bootstrap";
import Typeahead from "react-bootstrap-typeahead";
import Actions from "../../actions/Actions";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import ReportType from "../../model/ReportType";
import ReportStore from "../../stores/ReportStore";
import Routes from "../../utils/Routes";
import Routing from "../../utils/Routing";
import TypeaheadResultList from "../typeahead/TypeaheadResultList";
import Utils from "../../utils/Utils";

const OPTION_IDENTIFICATION_THRESHOLD = 45;

class NavSearch extends React.Component {
    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            options: NavSearch._processReports(ReportStore.getReports())
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

    render() {
        var classes = {
            input: 'navbar-search-input',
            results: 'navbar-search-results list-unstyled'
        };
        var optionLabel = this._getOptionLabelFunction();
        return <Typeahead ref={(c) => this.typeahead = c} size='small' name={this.props.name} className='navbar-search'
                          formInputOption='id' placeholder={this.i18n('main.search-placeholder')}
                          onOptionSelected={this._onOptionSelected} filterOption='identification'
                          displayOption={optionLabel} options={this.state.options} customClasses={classes}
                          customListComponent={TypeaheadResultList}/>;
    }

    _getOptionLabelFunction() {
        return function (option) {
            var date = option.date ? Utils.formatDate(new Date(option.date)) : '',
                label = option.identification.length > OPTION_IDENTIFICATION_THRESHOLD ?
                option.identification.substring(0, OPTION_IDENTIFICATION_THRESHOLD) + '...' : option.identification;
            return <span>
                <Label className='item-label'>{this.i18n(option.getLabel())}</Label>
                {label + ' (' + date + ')'}
            </span>;
        }.bind(this);
    }
}

export default injectIntl(I18nWrapper(NavSearch));
