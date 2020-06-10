'use strict';

export default class GraphUtils {

    static addEdge(from, to, relation_type, count, fromToCount, edges) {
        // let found = edges.filter((item) => {
        //     return (item.from == from) && (item.to == to) && (item.label == relation_type)
        // })
        let x = fromToCount[from + to];
        if (!x) {
            x = 0;
        }

        let y = ( ( x == 0 ) ? ( x ) : ( (x % 2) - 0.5) );

        let edge = {
            arrowStrikethrough: false,
            from: from,
            to: to,
            title: "" + count,
            // label: relation_type,
            value: count,
            chosen: {
                label: count
            },
            color: {
                color: (relation_type == 'causes') ? 'rgb(255,153,153)' :
                    (relation_type == 'mitigates') ? 'rgb(153,255,153)' :
                        (relation_type == 'contributes to') ? 'rgb(255,204,153)' :
                            'rgb(200,200,200)',
                hover: (relation_type == 'causes') ? 'red' :
                    (relation_type == 'mitigates') ? 'green' :
                        (relation_type == 'contributes to') ? 'orange' :
                            'black'
            },
            highlight: 'black',
            smooth: {
                enabled: true,
                type: 'curvedCW',
                roundness: y
            }
            // length: 1 / (count * count * count)
        }
        fromToCount[from + to] = x + 1;
        edges.push(edge);
        // }
    }

    static addNode(event_type, nodes, maxNodeHolder) {
        let found = nodes.filter((item) => {
            return (item.label == event_type)
        })
        let node;
        if (found.length == 1) {
            node = found[0];
            node.size = node.size + 1;
            node.mass = node.mass + 1;
            node.title = node.label + " ( "+ (node.mass - 5)+" )";
        } else {
            node = {
                id: maxNodeHolder.value++,
                label: event_type,
                shape: 'dot',
                title: event_type + " ( 1 )",
                mass: 5,
                size: 5
            }
            nodes.push(node);
        }
        return node.id;
    }

    static validEventType( nodeLabel ) {
        return nodeLabel && (nodeLabel != "NONE");
    }
}