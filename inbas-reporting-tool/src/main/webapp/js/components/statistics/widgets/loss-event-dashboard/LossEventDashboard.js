import React from "react";
import {injectIntl} from "react-intl";
import FrequencyList from "../FrequencyList";
import EventtypeGraph from "./EventtypeGraph";
import StatisticsStore from "../../../../stores/StatisticsStore";
import Actions from "../../../../actions/Actions";
import LoadingWrapper from "../../../misc/hoc/LoadingWrapper";
import LossEventProcesses from "./LossEventProcesses";
import I18nWrapper from "../../../../i18n/I18nWrapper";

class LossEventDashboard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            lossEvent: null,
            lossEventProcesses: null,
            processFactors: null
        }
    }

    componentDidMount() {
        this.unsubscribe = StatisticsStore.listen(this._onStatisticsLoaded);
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    onEventSelect(id) {
        this.props.loadingOn();
        this.setState({lossEvent: id});
        Actions.loadStatistics("loss_event_processes", {loss_event: id});
    }

    onProcessSelect = (process) => {
        this.props.loadingOn();
        Actions.loadStatistics("process_factors", {central_type: encodeURI(process.id)});
    };

    _onStatisticsLoaded = (data) => {
        this.props.loadingOff();
        if (data && data.queryName === "loss_event_processes") {
            this.setState({lossEventProcesses: data.queryResults.results.bindings});
        } else if (data && (data.queryName === "process_factors")) {
            this.setState({processFactors: data.queryResults.results.bindings});
        }
    };

    render() {
        return <table style={{width: '100%', height: '700px'}}>
            <tbody>
            <tr>
                <td className="col-xs-4 vtop">
                    <h4>{this.props.i18n("statistics.panel.loss-events.events.label")}</h4>
                    <FrequencyList query="loss_events_top" allowZeros={true}
                                   onSelect={(data) => this.onEventSelect(data)}
                                   loadingOn={this.props.loadingOn} loadingOff={this.props.loadingOff}/>
                    <LossEventProcesses data={this.state.lossEventProcesses} onSelect={this.onProcessSelect}/>
                </td>
                <td className='col-xs-8 vtop'>
                    <EventtypeGraph eventTypeGraph={this.state.processFactors} loadingOn={this.props.loadingOn}
                                    loadingOff={this.props.loadingOff}/>
                </td>
            </tr>
            </tbody>
        </table>;
    }
}

export default injectIntl(I18nWrapper(LoadingWrapper(LossEventDashboard, {maskClass: 'mask-container'})));
