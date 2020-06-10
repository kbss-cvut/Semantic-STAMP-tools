'use strict';
import React from "react";
import Graph from "react-graph-vis";

import I18nWrapper from "../../../../i18n/I18nWrapper";
import injectIntl from "../../../../utils/injectIntl";
import Utils from "../../Utils";
import GraphUtils from "./GraphUtils";
import LoadingWrapper from "../../../misc/hoc/LoadingWrapper";
import GraphOptions from "./GraphOptions";

class EventtypeGraph extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            data: {},
            graphOptions: GraphOptions
        }
    }

     load() {
        if (this.props.eventTypeGraph) {
            const rowsAll = Utils.sparql2table(this.props.eventTypeGraph);
            // perform group by
            let groupCols = ['event_type', 'factor_type', 'relation_type', 'process_event_label', 'process_factor_label'];
            let groups = rowsAll.reduce(function(grouped, row){
                let key = '';
                let p = {};
                groupCols.forEach((k) => key = key + row[k]);
                groupCols.forEach((k) => p[k] = row[k]);
                grouped.set(key, grouped[key] || {proj: p, group: []});
                grouped.get(key).group.push(row);
                return grouped;
            }, new Map());

            //
            const rows = Array.from(groups.values()).map((row) => {
                let newRow = row.proj;
                newRow.count = row.group.length;
                return newRow;
            });

            //   ?event_type ?relation_type ?factor_type (COUNT(*) AS ?count)
            let nodeColorMap = [];
            [...new Set(
                rows.map(x => x.process_event_label)
                    .concat(rows.map(x => x.process_factor_label))
                    .filter(x => x))
            ].forEach((val, i, arr) => nodeColorMap[val] = 'hsl(' + i*(360./arr.length) + ', 50%, 50%)');//"#ff0000"


            let nodes = []
            let edges = []

            let maxNodeHolder = {value:1}

            let fromToCount={};

            rows.forEach((item) => {
                let ft,et;
                if (GraphUtils.validEventType(item.factor_type)) {
                    ft = GraphUtils.addNode(item.factor_type, nodes, maxNodeHolder)
                    nodes[ft - 1].color = nodeColorMap[item.process_factor_label];
                }

                // assumes that the query will return NONE from root factor(the factor that does not have factors)
                if (GraphUtils.validEventType(item.event_type) && GraphUtils.validEventType(item.factor_type)) {
                    et = GraphUtils.addNode(item.event_type, nodes, maxNodeHolder)
                    nodes[et - 1].color = nodeColorMap[item.process_event_label];
                }

                if (et && ft) {
                    ft = GraphUtils.addNode(item.factor_type, nodes,maxNodeHolder);
                    nodes[ft - 1].title = nodes[ft - 1].title + " in " + item.process_factor_label;
                    et = GraphUtils.addNode(item.event_type, nodes);
                    nodes[et - 1].title = nodes[et - 1].title + " in " + item.process_event_label;
                    GraphUtils.addEdge(ft, et, item.relation_type, item.count, fromToCount, edges,maxNodeHolder);
                }
            });

            // let nodeFiltered = [];
            // edges = edges.filter((edge) => { if ( edge.value > 5 ) {nodeFiltered.push(edge.from); nodeFiltered.push(edge.to); return true} else return false})
            // nodes = nodes.filter((node) => { return nodeFiltered.indexOf(node.id) >= 0});

            return {nodes, edges};
        } else {
            return null;
        }
    };

    render() {
        const data = this.load();

        if ( data === null) {
            return <h4 className="content-center">{this.props.i18n("statistics.panel.loss-events.no-graph")}</h4>;
        } else {
            return <div>
                <h4 className="content-center">{this.props.title}</h4>
                <Graph graph={data} options={this.state.graphOptions} style={{ width : '100%', height:'625px'}}/>
            </div>;
        }
    }
}

export default injectIntl(I18nWrapper(LoadingWrapper(EventtypeGraph, {maskClass: 'mask-container'})));
