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
        this.state = {
            data: {},
            eventTypes: null,
            factorTypes: null,
            labelMap: null,
            nodeColorMap: null,
            graphOptions: GraphOptions
        }
    }

    _onOptionsLoaded = (type, options) => {
        let unsub = false;
        if(type === Constants.OPTIONS.EVENT_TYPE && options){
            unsub = this.state.factorTypes;
            this.setState({eventTypes: options});
        }else if(type === Constants.OPTIONS.FACTOR_TYPE){
            unsub = this.state.eventTypes;
            this.setState({factorTypes: options});
        }
        if(unsub)
            this.unsubscribe();
    }

     load() {
        if (this.props.eventTypeGraph) {
            const rowsAll = Utils.sparql2table(this.props.eventTypeGraph);
            // perform group by
            let groupCols = ['event_type', 'factor_type', 'relation_type', 'process_event_type', 'process_factor_type'];
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
                rows.map(x => x.process_event_type)
                    .concat(rows.map(x => x.process_factor_type)))
                    // .filter(x => x))
            ].forEach((val, i, arr) => nodeColorMap[val] = 'hsl(' + i*(360./arr.length) + ', 50%, 50%)');//"#ff0000"

            let labelMap = new Map();
            [this.state.eventTypes, this.state.factorTypes].forEach(voc => voc.forEach(
                e =>
                    labelMap.set(
                        e['@id'],
                        e[Vocabulary.RDFS_LABEL] ?
                            (e[Vocabulary.RDFS_LABEL]['@value'] ? e[Vocabulary.RDFS_LABEL]['@value'] : e[Vocabulary.RDFS_LABEL])
                            : e['@id']
                    )
                )
            );

            let nodes = []
            let edges = []
            let legend = {};
            let maxNodeHolder = {value:1}

            let fromToCount={};
            rows.forEach((item) => {
                let ft,et;
                if (GraphUtils.validEventType(item.factor_type)) {
                    ft = GraphUtils.addNode(labelMap.get(item.factor_type), nodes, maxNodeHolder)
                    nodes[ft - 1].process = item.process_factor_type;
                }

                if (GraphUtils.validEventType(item.event_type)) {
                    et = GraphUtils.addNode(labelMap.get(item.event_type), nodes, maxNodeHolder);
                    nodes[et - 1].process = item.process_event_type;
                }

                if (et && ft) {
                    GraphUtils.addEdge(ft, et, labelMap.get(item.relation_type), item.count, fromToCount, edges,maxNodeHolder);
                }
            });
            nodes.forEach(n => {
                n.color = nodeColorMap[n.process];
                let l = labelMap.get(n.process);
                l = l ? l : n.process;
                if(l)
                    n.title = n.title + " in " + l;
                legend[l] = n.color;
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
