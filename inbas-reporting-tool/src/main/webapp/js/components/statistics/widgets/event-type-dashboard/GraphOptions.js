'use strict';

export default {
    layout: {
        randomSeed: 1,
        improvedLayout: true
    },
    physics: {
        enabled: false,
        hierarchicalRepulsion: {
            damping: 0.5

        }
        //     barnesHut: {
        //         avoidOverlap: 0.1
        //     }
    },
    configure: {
        enabled: true,
        filter: (option, path) => {
            if (path.indexOf('layout') !== -1) {
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
        showButton: false,

    },

    interaction: {
        hover: true,
        multiselect: true,
        selectConnectedEdges: true,
        hoverConnectedEdges: true,
        navigationButtons: true,
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
};
