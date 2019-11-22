import Vocabulary from "../constants/Vocabulary";


class JsonLDUtils {
    static toTree(nodes, edges, relProp, rel='children'){
        let nodeMap = new Map();
        nodes.forEach((n) => {
            let node = {
                id: n['@id'],
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
                n[rel].push(...newChildren);
                childNodes.push(...newChildren);
            }
        });

        // find roots
        childNodes.forEach(n => nodeMap.delete(n.id));
        return nodeMap.values();
    }
}

export default JsonLDUtils;