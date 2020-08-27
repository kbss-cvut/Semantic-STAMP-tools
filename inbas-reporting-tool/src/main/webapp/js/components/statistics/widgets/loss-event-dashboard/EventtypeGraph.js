'use strict';

import React from "react";
import Graph from "react-graph-vis";

import I18nWrapper from "../../../../i18n/I18nWrapper";
import injectIntl from "../../../../utils/injectIntl";
import Utils from "../../Utils";
import GraphUtils from "./GraphUtils";
import LoadingWrapper from "../../../misc/hoc/LoadingWrapper";
import GraphOptions from "./GraphOptions";
import Vocabulary from "../../../../constants/Vocabulary";
import Actions from "../../../../actions/Actions";
import Constants from "../../../../constants/Constants";
import OptionsStore from "../../../../stores/OptionsStore";

class EventtypeGraph extends React.Component {

    constructor(props) {
        super(props);
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
        Actions.loadOptions(Constants.OPTIONS.EVENT_TYPE);
        Actions.loadOptions(Constants.OPTIONS.FACTOR_TYPE);
        Actions.loadOptions(Constants.OPTIONS.LOSS_EVENT_TYPE);
        this.state = {
            data: {},
            eventTypes: null,
            factorTypes: null,
            lossEventTypes: null,
            labelMap: null,
            nodeColorMap: null,
            graphOptions: GraphOptions
        }
    }

    _onOptionsLoaded = (type, options) => {
        if(type === Constants.OPTIONS.EVENT_TYPE && options){
            this.setState({eventTypes: options});
        }else if(type === Constants.OPTIONS.FACTOR_TYPE){
            this.setState({factorTypes: options});
        }else if(type === Constants.OPTIONS.LOSS_EVENT_TYPE){
            this.setState({lossEventTypes: options});
        }
        if(this.state.eventTypes && this.state.factorTypes && this.state.lossEventTypes)
            this.unsubscribe();
    }

     load() {
        const NO_PARENT_PROCESS =  "no parent process";
        if (this.props.eventTypeGraph) {
            const rowsAll = Utils.sparql2table(this.props.eventTypeGraph);
            // perform group by
            let groupCols = ['event_type', 'factor_type', 'relation_type', 'process_event_type', 'process_factor_type'];
            let groups = rowsAll.reduce(function(grouped, row){
                let key = '';
                let p = {};
                groupCols.forEach((k) => key = key + row[k]);
                groupCols.forEach((k) => p[k] = row[k]);
                grouped.set(key, grouped.get(key) || {proj: p, group: []});
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
                rows.map(x => x.process_event_type)
                    .concat(rows.map(x => x.process_factor_type)))
                    // .filter(x => x))
            ].forEach((val, i, arr) => nodeColorMap[val] = 'hsl(' + (i+1)*(360./(arr.length+1)) + ', 50%, 50%)');//"#ff0000"
            nodeColorMap["LOSS EVENT"] = 'hsl(0,50%,50%)';


            // resolve labels of event types
            let labelMap = new Map();
            [this.state.eventTypes, this.state.factorTypes, this.state.lossEventTypes]
                .filter(f => f).forEach(voc => voc.forEach(
                e =>
                    labelMap.set(
                        e['@id'],
                        e[Vocabulary.RDFS_LABEL] ?
                            (e[Vocabulary.RDFS_LABEL]['@value'] ? e[Vocabulary.RDFS_LABEL]['@value'] : e[Vocabulary.RDFS_LABEL])
                            : e['@id']
                    )
                )
            );

            let nodeCounts = new Map();
            ['event_type', 'factor_type'].forEach(function (el){
                rowsAll.forEach(function(row){
                    let key = labelMap.get(row[el]);
                    nodeCounts.set(key, (nodeCounts.get(key)  || 0) + 1 );
                });
            });


            let lossEventTypeIrs = this.state.lossEventTypes.map(i => i["@id"]);

            let nodes = []
            let edges = []
            let legend = {};
            let maxNodeHolder = {value:1}

            let fromToCount={};
            rows.forEach((item) => {
                let ft,et;
                if (GraphUtils.validEventType(item.factor_type)) {
                    let l = labelMap.get(item.factor_type);
                    ft = GraphUtils.addNode(l ? l : item.factor_type, nodes, maxNodeHolder)

                    nodes[ft - 1].process = lossEventTypeIrs.indexOf(item.factor_type) >= 0 ?
                        "LOSS EVENT" :
                        item.process_factor_type;
                }

                if (GraphUtils.validEventType(item.event_type)) {
                    let l = labelMap.get(item.event_type);
                    et = GraphUtils.addNode(l ? l : item.event_type, nodes, maxNodeHolder);
                    nodes[et - 1].process = lossEventTypeIrs.indexOf(item.event_type) >= 0 ?
                        "LOSS EVENT" :
                        item.process_event_type;
                }

                if (et && ft) {
                    let l = labelMap.get(item.relation_type);
                    GraphUtils.addEdge(ft, et, l ? l : item.relation_type, item.count, fromToCount, edges,maxNodeHolder);
                }
            });
            nodes.forEach(n => {
                // fix sizes in and count in node title
                let nodeCount = nodeCounts.get(n.label);
                n.size = nodeCount + 5;
                n.mass = nodeCount + 5;
                n.title = n.label + "(" + nodeCount + ")";

                n.color = nodeColorMap[n.process];
                if(n.process === "NONE"){
                    n.process = NO_PARENT_PROCESS;
                    legend[NO_PARENT_PROCESS] = n.color;
                }else {
                    let l = labelMap.get(n.process);
                    l = l ? l : n.process;
                    if (l) {
                        if(l !== "LOSS EVENT")
                            n.title = n.title + " in " + l;
                        legend[l] = n.color;
                    }
                }
            });


            // let nodeFiltered = [];
            // edges = edges.filter((edge) => { if ( edge.value > 5 ) {nodeFiltered.push(edge.from); nodeFiltered.push(edge.to); return true} else return false})
            // nodes = nodes.filter((node) => { return nodeFiltered.indexOf(node.id) >= 0});

            return {nodes, edges, legend};
        } else {
            return null;
        }
    }

    render() {
        const data = this.load();

        if ( data === null) {
            return <h4 className="content-center">{this.props.i18n("statistics.panel.loss-events.no-graph")}</h4>;
        } else {
            return <div>
                <h4 className="content-center">{this.props.title}</h4>
                <Graph graph={data} options={this.state.graphOptions} style={{ width : '100%', height:'625px'}}/>

                {this._renderGraphLegend(data.legend)}
            </div>;
        }
    }

    _renderGraphLegend(legend){
        return <div><h4 className="content-center">Legend</h4>
            <table style={{float: 'right'}}>
                {Object.keys(legend).sort().map(l =>
                    <tr>
                        <th style={{background:legend[l], width:'10px'}}></th>
                        <th>{l}</th>
                    </tr>
                )
                }
            </table>
        </div>
    }
}

export default injectIntl(I18nWrapper(LoadingWrapper(EventtypeGraph, {maskClass: 'mask-container'})));
