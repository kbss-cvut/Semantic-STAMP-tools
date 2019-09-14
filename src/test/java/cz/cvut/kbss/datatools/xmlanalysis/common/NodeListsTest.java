/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.common;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

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
    }

    /**
     * Test of asIterator method, of class XMLCollections.
     */
    @Test
    public void testAsIterator() {
    }

    /**
     * Test of asStream method, of class XMLCollections.
     */
    @Test
    public void testAsStream_NodeList() {
    }

    /**
     * Test of asStream method, of class XMLCollections.
     */
    @Test
    public void testAsStream_List() {
        System.out.println("asStream");
        List<String> list = Arrays.asList("a","weBuilder","c");
        Stream<String> result = XMLCollections.asStream(list);
        List<String> list2 = result.collect(Collectors.toList());
        assertTrue(list.equals(list2));
    }
    
    
    
    
}
