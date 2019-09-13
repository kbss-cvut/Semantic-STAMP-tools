package cz.cvut.kbss.datatools.xmlanalysis.common.graphml;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import java.util.HashMap;
import java.util.Map;

public class TinkerGraphIdTranslation extends TinkerGraph {
    protected Map<Object, Integer> idMap = new HashMap<>();

    protected int currentNodeId = 0;

    public int getNextNodeId(){ return currentNodeId++; }

    public Integer translateId(Object oid){
        Integer id = idMap.get(oid);
        if(id == null){
            id = getNextNodeId();
            idMap.put(oid, id);
        }
        return id;
    }

    @Override
    public Vertex addVertex(Object oid) {
        int id = translateId(oid);
        Vertex v = super.addVertex(id);
        v.setProperty("userOID", oid);
        return v;
    }

    @Override
    public Vertex getVertex(Object oid) {
        int id = translateId(oid);
        return super.getVertex(id);
    }

}
