import React from "react";
import {Panel} from "react-bootstrap";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import EventControlLoop from "./widgets/EventControlLoop";
import LossEventDashboard from "./widgets/loss-event-dashboard/LossEventDashboard";
import OccurrenceSeverityTrends from "./widgets/OccurrenceSeverityTrends";
import {Responsive, WidthProvider} from 'react-grid-layout';

class StatisticsController extends React.Component {

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
    }

    render() {
        const sm = [
            {i: 'top', x: 0, y: 0, w: 8, h: 2, isResizable: false, isDraggable: false},
            {i: 'main', x: 0, y: 2, w: 8, h: 4, isResizable: false, isDraggable: false},
            {i: 'bottom', x: 0, y: 8, w: 8, h: 3, isResizable: false, isDraggable: false}
        ];

        const lg = [
            {i: 'top', x: 0, y: 0, w: 8, h: 2, isResizable: false, isDraggable: false},
            {i: 'main', x: 0, y: 2, w: 8, h: 4, isResizable: false, isDraggable: false},
            {i: 'bottom', x: 0, y: 8, w: 8, h: 3, isResizable: false, isDraggable: false}
        ];

        const layouts = {lg: lg, md: lg, sm: sm, xs: sm, xxs: sm};
        const cols = {lg: 8, md: 8, sm: 4, xs: 4, xxs: 4};
        const ResponsiveReactGridLayout = WidthProvider(Responsive);
        return <div>
            <ResponsiveReactGridLayout
                draggableCancel="input,textarea"
                className="layout"
                layouts={layouts}
                cols={cols}
                rowHeight={200}>
                <div key="top">
                    <Panel header={<h3>{this.i18n('statistics.panel.occurrence-severity-trends.label')}</h3>}
                           className="statistics-panel"><OccurrenceSeverityTrends/></Panel>
                </div>
                <div key="main">
                    <Panel header={<h3>{this.i18n('statistics.panel.loss-events.label')}</h3>}
                           className="statistics-panel"><LossEventDashboard/></Panel>
                </div>
                <div key="bottom">
                    <Panel header={<h3>{this.i18n('statistics.panel.event-control-loop.label')}</h3>}
                           className="statistics-panel"><EventControlLoop/></Panel>
                </div>
            </ResponsiveReactGridLayout>
        </div>;
    }
}

export default injectIntl(I18nWrapper(StatisticsController));
