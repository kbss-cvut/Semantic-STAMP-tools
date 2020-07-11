/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.bpm2stampo.common;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class XMLCollections {
    
    /**
     * Returns an unmodifiable list constructed from the input nodeList.
     * @param nodeList
     * @return 
     */
    public static List<Node> asList(NodeList nodeList){
        return new AbstractList<Node>() {
            @Override
            public Node get(int index) {
                return nodeList.item(index);
            }

            @Override
            public int size() {
                return nodeList.getLength();
            }
        };
    }
    
    /**
     * Returns an unmodifiable list constructed from the input map.
     * @param map
     * @return 
     */
    public static List<Node> asList(NamedNodeMap map){
        return new AbstractList<Node>() {
            @Override
            public Node get(int index) {
                return map.item(index);
            }

            @Override
            public int size() {
                return map.getLength();
            }
        };
    }

    /**
     * Creates a new iterator from the input nodeList
     * @param nodeList
     * @return 
     */
    public static Iterator<Node> asIterator(NodeList nodeList){
        return new Iterator<Node>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }

            @Override
            public Node next() {
                Node node = nodeList.item(index);
                index++;
                return node;
            }
        };
    }
    
    /**
     * Creates a new iterator from the input map
     * @param map
     * @return 
     */
    public static Iterator<Node> asIterator(NamedNodeMap map){
        return new Iterator<Node>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < map.getLength();
            }

            @Override
            public Node next() {
                Node node = map.item(index);
                index++;
                return node;
            }
        };
    }
    
    /**
     * Creates a stream from the nodeList. This implementation creates an Iterator 
     * and then converts it to a stream using the <code>asStream</code> method
     * @param nodeList
     * @return 
     * @see XMLCollections#asStream(Iterator)
     */
    public static Stream<Node> asStream(NodeList nodeList){
        return asStream(asIterator(nodeList), nodeList.getLength());
    }
    
    /**
     * Creates a stream from the map. This implementation creates an Iterator 
     * and then converts it to a stream using the <code>asStream</code> method
     * @param map
     * @return 
     * @see XMLCollections#asStream(Iterator)
     */
    public static Stream<Node> asStream(NamedNodeMap map){
        return asStream(asIterator(map), map.getLength());
    }
    

    /**
     * Creates a stream from the input iterator using StreamSupport.stream and 
     * Spliterators.spliteratorUnknownSize.
     * @param <T>
     * @param iterator
     * @return 
     */
    public static <T> Stream<T> asStream(Iterator<T> iterator){
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }
    
    /**
     * Creates a stream from the input iterator using StreamSupport.stream and 
     * Spliterators.spliteratorUnknownSize.
     * @param <T>
     * @param iterator
     * @param size
     * @return 
     */
    public static <T> Stream<T> asStream(Iterator<T> iterator, long size){
        return StreamSupport.stream(Spliterators.spliterator(iterator, size, 0), false);
    }
    
    /**
     * Creates a new HashMap and populates it with key,value pairs from the input
     * map. The key of a node is determined by the keyFunction.
     * @param map
     * @param keyFucntion
     * @return 
     */
    public static Map<String, Node> asNameMap(NamedNodeMap map, Function<Node,String> keyFucntion){
        Map<String, Node> retmap = new HashMap<>();
        for(int i = 0; i < map.getLength(); i++){
            Node n = map.item(i);
            retmap.put(keyFucntion.apply(n), n);
        }
        return retmap;
    }
    
    
    /**
     * This method will be removed.
     * @param <T>
     * @param nodeList
     * @return
     * @deprecated
     */
    @Deprecated
    public static <T> Stream<T> asStream(List<T> nodeList){
        if(nodeList.isEmpty())
            return Stream.<T>empty();
        
        final int[] index = new int[]{0};
        
        return asStream(nodeList.iterator(), nodeList.size());
    }
    
}
