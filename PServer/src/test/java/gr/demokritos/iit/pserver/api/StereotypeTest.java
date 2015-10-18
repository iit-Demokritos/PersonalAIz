/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class StereotypeTest {
    
    public StereotypeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getSystemAttributes method, of class Stereotype.
     */
    public void testGetSystemAttributes() {
        System.out.println("getSystemAttributes");
        Stereotype instance = null;
        Set<String> expResult = null;
        Set<String> result = instance.getSystemAttributes();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of addStereotype method, of class Stereotype.
     */
    public void testAddStereotype() {
        System.out.println("addStereotype");
        String stereotypeName = "";
        String rule = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.addStereotype(stereotypeName, rule);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteStereotypes method, of class Stereotype.
     */
    public void testDeleteStereotypes() {
        System.out.println("deleteStereotypes");
        String pattern = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.deleteStereotypes(pattern);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStereotypes method, of class Stereotype.
     */
    public void testGetStereotypes() {
        System.out.println("getStereotypes");
        String pattern = "";
        Integer page = null;
        Stereotype instance = null;
        Set<String> expResult = null;
        Set<String> result = instance.getStereotypes(pattern, page);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of remakeStereotype method, of class Stereotype.
     */
    public void testRemakeStereotype() {
        System.out.println("remakeStereotype");
        String stereotypeName = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.remakeStereotype(stereotypeName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateStereotypeFeatures method, of class Stereotype.
     */
    public void testUpdateStereotypeFeatures() {
        System.out.println("updateStereotypeFeatures");
        String stereotypeName = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.updateStereotypeFeatures(stereotypeName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateStereotypeUsers method, of class Stereotype.
     */
    public void testUpdateStereotypeUsers() {
        System.out.println("updateStereotypeUsers");
        String stereotypeName = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.updateStereotypeUsers(stereotypeName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of findStereotypeUsers method, of class Stereotype.
     */
    public void testFindStereotypeUsers() {
        System.out.println("findStereotypeUsers");
        String stereotypeName = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.findStereotypeUsers(stereotypeName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkStereotypeUsers method, of class Stereotype.
     */
    public void testCheckStereotypeUsers() {
        System.out.println("checkStereotypeUsers");
        String stereotypeName = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.checkStereotypeUsers(stereotypeName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStereotypeFeatures method, of class Stereotype.
     */
    public void testSetStereotypeFeatures() {
        System.out.println("setStereotypeFeatures");
        String stereotypeName = "";
        Map<String, String> features = null;
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.setStereotypeFeatures(stereotypeName, features);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of modifyStereotypeFeatures method, of class Stereotype.
     */
    public void testModifyStereotypeFeatures() {
        System.out.println("modifyStereotypeFeatures");
        String stereotypeName = "";
        Map<String, String> features = null;
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.modifyStereotypeFeatures(stereotypeName, features);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStereotypeFeatures method, of class Stereotype.
     */
    public void testGetStereotypeFeatures() {
        System.out.println("getStereotypeFeatures");
        String stereotypeName = "";
        String pattern = "";
        Integer page = null;
        Stereotype instance = null;
        Map<String, String> expResult = null;
        Map<String, String> result = instance.getStereotypeFeatures(stereotypeName, pattern, page);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteStereotypeFeatures method, of class Stereotype.
     */
    public void testDeleteStereotypeFeatures() {
        System.out.println("deleteStereotypeFeatures");
        String stereotypeName = "";
        String pattern = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.deleteStereotypeFeatures(stereotypeName, pattern);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStereotypeUsers method, of class Stereotype.
     */
    public void testGetStereotypeUsers() {
        System.out.println("getStereotypeUsers");
        String stereotypeName = "";
        String pattern = "";
        Integer page = null;
        Stereotype instance = null;
        List<String> expResult = null;
        List<String> result = instance.getStereotypeUsers(stereotypeName, pattern, page);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserStereotypes method, of class Stereotype.
     */
    public void testGetUserStereotypes() {
        System.out.println("getUserStereotypes");
        String username = "";
        String pattern = "";
        Integer page = null;
        Stereotype instance = null;
        List<String> expResult = null;
        List<String> result = instance.getUserStereotypes(username, pattern, page);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of addUserOnStereotype method, of class Stereotype.
     */
    public void testAddUserOnStereotype() {
        System.out.println("addUserOnStereotype");
        String username = "";
        String stereotypeName = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.addUserOnStereotype(username, stereotypeName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of DeleteUserFromStereotype method, of class Stereotype.
     */
    public void testDeleteUserFromStereotype() {
        System.out.println("DeleteUserFromStereotype");
        String username = "";
        String stereotypeName = "";
        Stereotype instance = null;
        boolean expResult = false;
        boolean result = instance.DeleteUserFromStereotype(username, stereotypeName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

}
