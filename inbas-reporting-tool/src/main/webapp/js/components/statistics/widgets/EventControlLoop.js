import React from "react";
import ReactPivot from "react-pivot";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import StatisticsStore from "../../../stores/StatisticsStore";
import Actions from "../../../actions/Actions";
import Utils from "../Utils";
import LoadingWrapper from "../../misc/hoc/LoadingWrapper";

class EventControlLoop extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            rows: [],
            dimensions: [],
            reduce: (row, memo) => {
                return memo
            },
            calculations: []
        }
    }

    componentWillMount() {
        this.props.loadingOn();
        Actions.loadStatistics("eventcontrolloop");
        this.unsubscribe = StatisticsStore.listen(this._onStatisticsLoaded);
    };

    componentWillUnmount() {
        this.unsubscribe();
    };

    _onStatisticsLoaded = (data) => {
        if (!data || (data.queryName !== "eventcontrolloop")) {
            return;
        }

        const dimensions = data.queryResults.head.vars.filter(
            (varName) => !varName.startsWith('count')).map(
            (varName) => {
                const vn = varName.charAt(0).toUpperCase() + varName.substring(1);
                let title = this.props.i18n('statistics.panel.event-control-loop.dimension.' + varName);
                if(!title){
                    title = vn.replace('_', ' ');
                }
                return {value: varName, title: title}
            }
        );
        const calculations = [{
            title: this.props.i18n('statistics.panel.event-control-loop.calculation.count'), value: 'count',
            template: val => val
        }];

        this.setState(
            {
                rows: Utils.sparql2table(data.queryResults.results.bindings),
                dimensions: dimensions,
                calculations: calculations,
                reportKey: Date.now()
            }
        );
        this.props.loadingOff();
    };

    reduce = (row, memo) => {
        memo.count = (memo.count || 0) + Number(row.count);
        return memo;
    };

    render() {
        return <div className='centered'>
            <ReactPivot
                key={this.state.reportKey}
                rows={this.state.rows}
                dimensions={this.state.dimensions}
                reduce={this.reduce}
                calculations={this.state.calculations}
                activeDimensions={this.state.dimensions[0]}
                nPaginateRows={10}
                compact={true}/>
        </div>;
    }
}

export default injectIntl(I18nWrapper(LoadingWrapper(EventControlLoop, {maskClass: 'mask-container'})));
