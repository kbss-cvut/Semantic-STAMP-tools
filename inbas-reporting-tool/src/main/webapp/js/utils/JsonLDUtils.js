import Vocabulary from "../constants/Vocabulary";


class JsonLDUtils {
    static toTree(nodes, edges, relProp, rel='children'){
        let nodeMap = new Map();
        nodes.forEach((n) => {
            let node = {
                id: n['@id'],
                type: n['@type'],
                name: n[Vocabulary.RDFS_LABEL],
                toggled: false,
            };
            nodeMap.set(node.id, node);
        });

        let childNodes = [];
        edges.forEach((e) => {
            let n = nodeMap.get(e['@id']);
            if (n && e[relProp]) {
                let obj = e[relProp];
                if (!n[rel])
                    n[rel] = [];
                let newChildren = [];
                if (obj.length) {
                    newChildren = obj.map((n) => nodeMap.get(n['@id']));
                } else if (obj['@id']) {
                    newChildren.push(nodeMap.get(obj['@id']));
                }
                newChildren = newChildren.filter(n => n);
                n[rel].push(...newChildren);
                childNodes.push(...newChildren);
            }
        });

        // find roots
        let roots = [...nodeMap.values()].filter(n => !childNodes.includes(n));
        return {roots: roots, nodeMap: nodeMap};
    }
}

export default JsonLDUtils;