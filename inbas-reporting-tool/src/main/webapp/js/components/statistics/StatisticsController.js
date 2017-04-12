'use strict';

import React from "react";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Dashboard, {addWidget} from "react-dazzle";
import EditBar from "./EditBar";
import Container from "./Container";
import CustomFrame from "./CustomFrame";
import EventList from "./widgets/EventList";
import EventFactorChains from "./widgets/EventFactorChains";
import EventFactorGraph from "./widgets/EventFactorGraph";
import EventtypeDashboard from "./widgets/EventtypeDashboard";
import OccurrenceList from "./widgets/OccurrenceList";
import OccurrenceSeverityTrends from "./widgets/OccurrenceSeverityTrends";

class StatisticsController extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            widgets: {
                OccurrenceList: {
                    type: OccurrenceList,
                    title: 'Top 5 Occurrence Categories',
                },
                EventList: {
                    type: EventList,
                    title: 'Top Event types',
                },
                EventFactorChains: {
                    type: EventFactorChains,
                    title: 'Event - Factor Chains',
                },
                EventtypeDashboard: {
                    type: EventtypeDashboard,
                    title: 'Event Type Dashboard',
                },
                EventFactorGraph: {
                    type: EventFactorGraph,
                    title: 'Event Type - Factor Type Graph',
                },
                OccurrenceSeverityTrends: {
                    type: OccurrenceSeverityTrends,
                    title: 'Occurrence Severity Trend',
                }
            },
            layout: {
                rows: [ {
                    columns: [{
                        className: 'col-md-6 col-sm-6 col-xs-6',
                        widgets: [{key: 'OccurrenceSeverityTrends'}],
                    }, {
                        className: 'col-md-6 col-sm-6 col-xs-6',
                        widgets: [{key: 'OccurrenceList'}],
                    }],
                },{
                    columns: [{
                        className: 'col-md-12 col-sm-12 col-xs-12',
                        widgets: [{key: 'EventtypeDashboard'}],
                    }]
                },{
                    columns: [{
                        className: 'col-md-12 col-sm-12 col-xs-12',
                        widgets: [{key: 'EventFactorGraph'}],
                    }]
                },{
                    columns: [{
                        className: 'col-md-12 col-sm-12 col-xs-12',
                        widgets: [{key: 'EventFactorChains'}],
                    }]
                }]
            },
            editMode: false,
            isModalOpen: false,
            addWidgetOptions: null,
        }
    }


    /**
     * When a widget moved, this will be called. Layout should be given back.
     */
    onMove = (layout) => {
        this.setState({
            layout: layout,
        });
    };

    /**
     * Toggles edit mode in dashboard.
     */
    toggleEdit = () => {
        this.setState({
            editMode: !this.state.editMode,
        });
    };

    render() {
        return <div><Container>
            <EditBar onEdit={this.toggleEdit}/>
            <Dashboard
                frameComponent={CustomFrame}
                layout={this.state.layout}
                widgets={this.state.widgets}
                editable={this.state.editMode}
                onMove={this.onMove}
                addWidgetComponentText=""
            />
        </Container></div>
    }
}

export default injectIntl(I18nWrapper(StatisticsController));
