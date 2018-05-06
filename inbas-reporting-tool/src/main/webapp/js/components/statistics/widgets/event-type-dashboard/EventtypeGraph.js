'use strict';
import React from "react";
import Graph from "react-graph-vis";

import I18nWrapper from "../../../../i18n/I18nWrapper";
import injectIntl from "../../../../utils/injectIntl";
import Utils from "../../Utils";
import GraphUtils from "./GraphUtils";
import LoadingWrapper from "../../../misc/hoc/LoadingWrapper";

class EventtypeGraph extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            data: {},
            graphOptions: {
                layout: {
                    randomSeed: 1,
                    improvedLayout: true
                },
                physics: {
                    enabled: false,
                    hierarchicalRepulsion: {
                        damping:0.5

                    }
                //     barnesHut: {
                //         avoidOverlap: 0.1
                //     }
                },
                configure: {
                    enabled: true,
                    filter: (option,path) => {
                        if ( path.indexOf('layout') !== -1 ) {
                            if (
                                (path.length == 1)
                                || (option == 'enabled')
                                || (option == 'levelSeparation')
                                || (option == 'treeSpacing')
                                || (option == 'direction')
                                || (option == 'sortMethod')

                            ) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        return false;
                    },//, "edges","interaction", "manipulation", "selection", "renderer", "physics"]
                    showButton: false
                },

                interaction: {
                    hover: true,
                    multiselect: true,
                    selectConnectedEdges: true,
                    hoverConnectedEdges: true,
                    zoomView: true,
                    tooltipDelay: 300
                },
                edges: {
                    smooth: {
                        enabled: true,
                        type: 'continuous'
                    }
                },
                // autoResize: true,
            }
        }
    }

     load() {
        if (this.props.eventTypeGraph) {
            const rows = Utils.sparql2table(this.props.eventTypeGraph);

            //   ?event_type ?relation_type ?factor_type (COUNT(*) AS ?count)

            let nodes = []
            let edges = []

            let maxNodeHolder = {value:1}

            let fromToCount={};

            rows.forEach((item) => {
                let ft,et;
                if (GraphUtils.validEventType(item.factor_type)) {
                    ft = GraphUtils.addNode(item.factor_type, nodes, maxNodeHolder)
                }

                if (GraphUtils.validEventType(item.event_type)) {
                    et = GraphUtils.addNode(item.event_type, nodes, maxNodeHolder)
                }

                if (et && ft) {
                    GraphUtils.addEdge(GraphUtils.addNode(item.factor_type, nodes,maxNodeHolder), GraphUtils.addNode(item.event_type, nodes), item.relation_type, item.count, fromToCount, edges,maxNodeHolder);
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

        if ( data == null) {
            return <div style={{ textAlign: "center", verticalAlign:"center"}}>No Graph Available. Select an Event Type.</div>;
        } else {
            return <div>{this.props.title}<br/><Graph graph={data} options={this.state.graphOptions} style={{ width : '100%', height:'400px'}}/></div>;
        }
    }
}

export default injectIntl(I18nWrapper(LoadingWrapper(EventtypeGraph, {maskClass: 'mask-container'})));