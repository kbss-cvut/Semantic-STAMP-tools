'use strict';

import React from "react";
import Typeahead from "react-bootstrap-typeahead";
import Actions from "../../../actions/Actions";
import DataFilter from "../../../utils/DataFilter";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";
import ReportStore from "../../../stores/ReportStore";
import Routing from "../../../utils/Routing";
import Routes from "../../../utils/Routes";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";
import Vocabulary from "../../../constants/Vocabulary";

class SafetyIssueSelector extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            reports: SafetyIssueSelector._filterReports(ReportStore.getReports())
        };
    }

    /**
     * Returns only safety issue reports.
     * @private
     */
    static _filterReports(allReports) {
        if (!allReports) {
            return [];
        }
        var filter = {
            'types': Vocabulary.SAFETY_ISSUE_REPORT
        };
        return DataFilter.filterData(allReports, filter);
    }

    componentDidMount() {
        Actions.loadAllReports();
        this.unsubscribe = ReportStore.listen(this._onReportsLoaded);
    }

    _onReportsLoaded = (data) => {
        if (data.action === Actions.loadAllReports)
            this.setState({reports: SafetyIssueSelector._filterReports(data.reports)});
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onOptionSelected = (option) => {
        var issueKey = option.key,
            report = this.props.report;
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: issueKey},
            payload: {basedOn: report}
        });
    };

    render() {
        if (this.state.reports.length === 0) {
            // Do not render until there are options. Prevents issues with the typeahead
            return null;
        }
        var i18n = this.props.i18n;
        return <Typeahead ref='safetyIssueTypeahead' className='form-group form-group-sm' optionsButton={true}
                          formInputOption='id'
                          placeholder={i18n('occurrencereport.add-as-safety-issue-base-placeholder')}
                          onOptionSelected={this._onOptionSelected} filterOption='identification'
                          displayOption='identification' options={this.state.reports}
                          customListComponent={TypeaheadResultList}/>;
    }
}

export default injectIntl(I18nWrapper(SafetyIssueSelector));
