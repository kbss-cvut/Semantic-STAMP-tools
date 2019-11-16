"use strict";

import React from "react";
import PropTypes from "prop-types";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import StatisticsStore from "../../../stores/StatisticsStore";
import Actions from "../../../actions/Actions";
import FrequencyListRow from "./FrequencyListRow";
import Utils from "../Utils";
import Table from "react-bootstrap/lib/Table";
import PagingMixin from "../../mixin/PagingMixin";
import I18nMixin from "../../../i18n/I18nMixin";
import LoadingWrapper from "../../misc/hoc/LoadingWrapper";

const FrequencyList = React.createClass({
    mixins: [PagingMixin, I18nMixin],

    propTypes: {
        query: PropTypes.string.isRequired,
        allowZeros: PropTypes.bool.isRequired,
        onSelect: PropTypes.func.isRequired,
        activeItem: PropTypes.string
    },

    getInitialState() {
        return {
            eventTypes: [],
            rows: []
        };
    },

    componentWillMount() {
        this.props.loadingOn();
        Actions.loadStatistics(this.props.query);
        this.unsubscribe = StatisticsStore.listen(this._onStatisticsLoaded);
    },

    componentWillUnmount() {
        this.unsubscribe();
    },

    _onStatisticsLoaded(data) {
        if (!data || (data.queryName !== this.props.query)) {
            return;
        }

        const rowData = Utils.sparql2table(data.queryResults.results.bindings);
        const eventTypesIris = Utils.unique(rowData.map((item) => item.event_type_iri));

        const {minDate, maxDate} = Utils.getMonthRangeFromNow(12);
        let rows = [];
        for (let i in eventTypesIris) {
            const eventTypeIri = eventTypesIris[i];
            const vals = rowData.filter((item2) => (item2.event_type_iri === eventTypeIri));
            let data = Utils.generateMonthTimeAxis(minDate, maxDate).map((item) => {
                const match = vals.filter((item2) => (Number(item2.year) * 100 + Number(item2.month)) === item);
                let count = 0;
                if (match && match[0]) {
                    count = count + Number(match[0].count)
                }

                return {
                    date: item,
                    count: count,
                }
            });

            const sum = data.reduce((memo, val) => memo + Number(val.count), 0);

            if (this.props.allowZeros || (sum > 0)) {
                rows.push({
                    key: i,
                    data: data,
                    totalSum: sum,
                    eventType: vals[0].event_type,
                    eventTypeIri: eventTypeIri
                });
            }
        }

        rows = rows.sort((a, b) => b.totalSum - a.totalSum);

        this.setState(
            {
                rows: rows
            }
        );
        this.props.loadingOff();
    },

    render() {
        const topList = this.state.rows.map(row => <FrequencyListRow key={row.key} row={row}
                                                                     active={this.props.activeItem === row.eventTypeIri}
                                                                     onClick={this.props.onSelect}/>);

        return <div className="event-type-table">
            <Table striped bordered condensed hover>
                <thead>
                <tr>
                    <th className='col-xs-4 content-center'>{this.i18n('statistics.frequencylist.eventtype.label')}</th>
                    <th className='col-xs-1 content-center'>{this.i18n('count')}</th>
                    <th className='col-xs-2 content-center'>{this.i18n('statistics.frequencylist.annualtrend.label')}</th>
                </tr>
                </thead>
                <tbody>
                {this.getCurrentPage(topList)}
                </tbody>
            </Table>
            {this.renderPagination(topList)}
        </div>;
    }
});
module.exports = injectIntl(I18nWrapper(LoadingWrapper(FrequencyList, {maskClass: 'mask-container'})));
