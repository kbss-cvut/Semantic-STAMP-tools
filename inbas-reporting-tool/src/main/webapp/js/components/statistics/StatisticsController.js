'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import EventFactorChains from "./widgets/EventFactorChains";
import EventtypeDashboard from "./widgets/event-type-dashboard/EventtypeDashboard";
import OccurrenceList from "./widgets/OccurrenceList";
import OccurrenceSeverityTrends from "./widgets/OccurrenceSeverityTrends";
import {Responsive, WidthProvider} from 'react-grid-layout';

class StatisticsController extends React.Component {

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
    }

    render() {
        const sm = [
            {i: 'top-left', x: 0, y: 0, w: 4, h: 2, isResizable: false, isDraggable: false},
            {i: 'top-right', x: 0, y: 2, w: 4, h: 2, isResizable: false, isDraggable: false},
            {i: 'main', x: 0, y: 4, w: 4, h: 4, isResizable: false, isDraggable: false},
            {i: 'bottom', x: 0, y: 8, w: 4, h: 2, isResizable: false, isDraggable: false}
        ];

        const lg = [
            {i: 'top-left', x: 0, y: 0, w: 4, h: 2, isResizable: false, isDraggable: false},
            {i: 'top-right', x: 4, y: 0, w: 4, h: 2, isResizable: false, isDraggable: false},
            {i: 'main', x: 0, y: 2, w: 8, h: 6, isResizable: false, isDraggable: false},
            {i: 'bottom', x: 0, y: 8, w: 8, h: 2, isResizable: false, isDraggable: false}
        ];

        const layouts = {lg: lg, md: lg, sm: sm, xs: sm, xxs: sm};
        const cols = {lg: 8, md: 8, sm: 4, xs: 4, xxs: 4};
        const ResponsiveReactGridLayout = WidthProvider(Responsive);
        return (<div>
            <ResponsiveReactGridLayout
                draggableCancel="input,textarea"
                className="layout"
                layouts={layouts}
                cols={cols}
                rowHeight={200}>
                <div key="top-left"><Panel header={this.i18n('statistics.panel.occurrence-severity-trends.label')}><OccurrenceSeverityTrends/></Panel></div>
                <div key="top-right"><Panel header={this.i18n('statistics.panel.occurrence-categories-top.label')}><OccurrenceList/></Panel></div>
                <div key="main"><Panel header={this.i18n('statistics.panel.root-events.label')}><EventtypeDashboard/></Panel></div>
                <div key="bottom"><Panel header={this.i18n('statistics.panel.event-factor-chains.label')}><EventFactorChains/></Panel></div>
            </ResponsiveReactGridLayout>
        </div>);
    }
}

export default injectIntl(I18nWrapper(StatisticsController));
