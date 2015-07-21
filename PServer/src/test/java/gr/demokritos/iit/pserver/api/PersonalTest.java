/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.security.authorization.Action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class PersonalTest {
    
    public PersonalTest() {
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
     * Test of addUser method, of class Personal.
     */
    @Test
    public void testAddUser() {
        System.out.println("addUser");
        User user = null;
        Personal instance = null;
        boolean expResult = false;
//        boolean result = instance.addUser(user);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
//
//    /**
//     * Test of addUsers method, of class Personal.
//     */
//    @Test
//    public void testAddUsers() {
//        System.out.println("addUsers");
//        List<User> usersList = null;
//        Personal instance = null;
//        boolean expResult = false;
//        boolean result = instance.addUsers(usersList);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteUsers method, of class Personal.
//     */
//    @Test
//    public void testDeleteUsers() {
//        System.out.println("deleteUsers");
//        String pattern = "";
//        Personal instance = null;
//        boolean expResult = false;
//        boolean result = instance.deleteUsers(pattern);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUsers method, of class Personal.
//     */
//    @Test
//    public void testGetUsers() {
//        System.out.println("getUsers");
//        String pattern = "";
//        Integer page = null;
//        Personal instance = null;
//        Set<String> expResult = null;
//        Set<String> result = instance.getUsers(pattern, page);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setUserAttributes method, of class Personal.
//     */
//    @Test
//    public void testSetUserAttributes() {
//        System.out.println("setUserAttributes");
//        String username = "";
//        HashMap<String, String> attributes = null;
//        Personal instance = null;
//        boolean expResult = false;
//        boolean result = instance.setUserAttributes(username, attributes);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUserAttributes method, of class Personal.
//     */
//    @Test
//    public void testGetUserAttributes() {
//        System.out.println("getUserAttributes");
//        String user = "";
//        String pattern = "";
//        Integer page = null;
//        Personal instance = null;
//        Map<String, String> expResult = null;
//        Map<String, String> result = instance.getUserAttributes(user, pattern, page);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setUserFeatures method, of class Personal.
//     */
//    @Test
//    public void testSetUserFeatures() {
//        System.out.println("setUserFeatures");
//        String username = "";
//        HashMap<String, String> features = null;
//        Personal instance = null;
//        boolean expResult = false;
//        boolean result = instance.setUserFeatures(username, features);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of modifyUserFeatures method, of class Personal.
//     */
//    @Test
//    public void testModifyUserFeatures() {
//        System.out.println("modifyUserFeatures");
//        String username = "";
//        HashMap<String, String> features = null;
//        Personal instance = null;
//        boolean expResult = false;
//        boolean result = instance.modifyUserFeatures(username, features);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUserFeatures method, of class Personal.
//     */
//    @Test
//    public void testGetUserFeatures() {
//        System.out.println("getUserFeatures");
//        String user = "";
//        String pattern = "";
//        Integer page = null;
//        Personal instance = null;
//        Map<String, String> expResult = null;
//        Map<String, String> result = instance.getUserFeatures(user, pattern, page);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getPermissionFor method, of class Personal.
//     */
//    @Test
//    public void testGetPermissionFor() {
//        System.out.println("getPermissionFor");
//        Action a = null;
//        String sAccessType = "";
//        Personal instance = null;
//        boolean expResult = false;
//        boolean result = instance.getPermissionFor(a, sAccessType);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
