/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class NodeListsTest {
    
    public NodeListsTest() {
    }

    /**
     * Test of asList method, of class XMLCollections.
     */
    @Test
    public void testAsList() {
//        System.out.println("asList");
//        NodeList nodeList = null;
//        List<Node> expResult = null;
//        List<Node> result = XMLCollections.asList(nodeList);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of asIterator method, of class XMLCollections.
     */
    @Test
    public void testAsIterator() {
//        System.out.println("asIterator");
//        NodeList nodeList = null;
//        Iterator<Node> expResult = null;
//        Iterator<Node> result = XMLCollections.asIterator(nodeList);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of asStream method, of class XMLCollections.
     */
    @Test
    public void testAsStream_NodeList() {
//        System.out.println("asStream");
//        NodeList nodeList = null;
//        Stream<Node> expResult = null;
//        Stream<Node> result = XMLCollections.asStream(nodeList);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of asStream method, of class XMLCollections.
     */
    @Test
    public void testAsStream_List() {
        System.out.println("asStream");
        List<String> list = Arrays.asList("a","b","c");
        Stream<String> result = XMLCollections.asStream(list);
        List<String> list2 = result.collect(Collectors.toList());
        assertTrue(list.equals(list2));
    }
    
    
    
    
}
