'use strict';

import React from "react";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import StatisticsStore from "../../../stores/StatisticsStore";
import Actions from "../../../actions/Actions";
import Utils from "../Utils";
import {LineChart, Line, XAxis, Tooltip} from 'recharts';


class FrequencyList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            eventTypes: [],
            rows: []
        }
    }

    componentWillMount() {
        Actions.loadStatistics("events_top5_monthsback12");
        this.unsubscribe = StatisticsStore.listen(this._onStatisticsLoaded);
    };

    componentWillUnmount() {
        this.unsubscribe();
    };

    _onStatisticsLoaded = (data) => {
        if (data === undefined) {
            return;
        }

        if (data.queryName != "events_top5_monthsback12") {
            return;
        }

        if (!data.queryResults) {
            return;
        }

        const rows = Utils.sparql2table(data.queryResults.results.bindings);
        const eventTypes = Utils.unique(rows.map((item) => {
            return item.event_type
        }));

        this.setState(
            {
                eventTypes: eventTypes,
                rows: rows
            }
        );
    };

    render() {
        const topList = [];
        const {minDate, maxDate} = Utils.getMonthRangeFromNow(12);
        for (let i in this.state.eventTypes) {
            const et = this.state.eventTypes[i];
            const vals = this.state.rows.filter((item2) => {
                return (item2.event_type == et)
            });
            let data = Utils.generateMonthTimeAxis(vals, minDate, maxDate).map((item) => {
                const match = vals.filter((item2) => {
                    return (Number(item2.year) * 100 + Number(item2.month)) == item
                });
                let count = 0;
                if (match && match[0]) {
                    count = count + Number(match[0].count)
                }

                return {
                    date: item,
                    count: count,
                }
            });

            const totalSum = data.reduce((memo, val) => memo + Number(val.count), 0);

            topList.push(
                <tr key={i}>
                    <td><LineChart width={100} height={50} data={data}>
                        <XAxis dataKey='date' hide={true}/>
                        <Line type='basis' dataKey='count' stroke='#8884d8' strokeWidth={2} dot={false}/>
                        <Tooltip/>
                    </LineChart>
                    </td>
                    <td>{totalSum}</td>
                    <td>{et}</td>
                </tr>
            )
        }

        return (
            <table>
                <thead>
                <tr>
                    <th className='col-xs-2'>Annual Trend</th>
                    <th className='col-xs-1'>Count</th>
                    <th className='col-xs-8'>Event Type</th>
                </tr>
                </thead>
                <tbody>
                {topList}
                </tbody>
            </table>
        );
    }
}

export default injectIntl(I18nWrapper(FrequencyList));