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
            process: null,
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
        this.setState({lossEvent: id, lossEventProcess: null, processFactors: null});
        Actions.loadStatistics("loss_event_processes", {loss_event: encodeURI(id)});
    }
    onClickCount(lossEventTypeIri){
        return "reports?lossEventType=" + lossEventTypeIri;
    }

    onProcessSelect = (process) => {
        this.props.loadingOn();
        this.setState({process: process});
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
        const activeProcess = this.state.process;
        return <table style={{width: '100%', height: '700px'}}>
            <tbody>
            <tr>
                <td className="col-xs-4 vtop">
                    <h4>{this.props.i18n("statistics.panel.loss-events.events.label")}</h4>
                    <div className="autoscroll" style={{maxHeight: "350px"}}>
                        <FrequencyList query="loss_events_top" allowZeros={true} activeItem={this.state.lossEvent}
                                       onSelect={(data) => this.onEventSelect(data)}
                                       onClickCount={(data) => this.onClickCount(data)}
                                       loadingOn={this.props.loadingOn} loadingOff={this.props.loadingOff}/>
                    </div>
                    <LossEventProcesses data={this.state.lossEventProcesses}
                                        activeItem={activeProcess ? activeProcess.id : null}
                                        onSelect={this.onProcessSelect}/>
                </td>
                <td className='col-xs-8 vtop'>
                    <EventtypeGraph eventTypeGraph={this.state.processFactors} loadingOn={this.props.loadingOn}
                                    loadingOff={this.props.loadingOff}
                                    title={activeProcess ? activeProcess.label : null}/>
                </td>
            </tr>
            </tbody>
        </table>;
    }
}

export default injectIntl(I18nWrapper(LoadingWrapper(LossEventDashboard, {maskClass: 'mask-container'})));
