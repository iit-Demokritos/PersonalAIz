/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.api.pserver;

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
public class PersonalRESTTest {
    
    public PersonalRESTTest() {
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
     * Test of addUsers method, of class PersonalREST.
     */
    @Test
    public void testAddUsers() {
        System.out.println("addUsers");
        String userKey = "";
        String JSONUsers = "";
        PersonalREST instance = new PersonalREST();
        String expResult = "";
        String result = instance.addUsers(userKey, JSONUsers);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteUsers method, of class PersonalREST.
     */
    @Test
    public void testDeleteUsers() {
        System.out.println("deleteUsers");
        String userKey = "";
        String pattern = "";
        PersonalREST instance = new PersonalREST();
        String expResult = "";
        String result = instance.deleteUsers(userKey, pattern);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsers method, of class PersonalREST.
     */
    @Test
    public void testGetUsers() {
        System.out.println("getUsers");
        String userKey = "";
        String pattern = "";
        String page = "";
        PersonalREST instance = new PersonalREST();
        String expResult = "";
        String result = instance.getUsers(userKey, pattern, page);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserAttributes method, of class PersonalREST.
     */
    @Test
    public void testSetUserAttributes() {
        System.out.println("setUserAttributes");
        String userKey = "";
        String user = "";
        String JSONUserAttributes = "";
        PersonalREST instance = new PersonalREST();
        String expResult = "";
        String result = instance.setUserAttributes(userKey, user, JSONUserAttributes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserFeatures method, of class PersonalREST.
     */
    @Test
    public void testSetUserFeatures() {
        System.out.println("setUserFeatures");
        String userKey = "";
        String user = "";
        String JSONUserFeatures = "";
        PersonalREST instance = new PersonalREST();
        String expResult = "";
        String result = instance.setUserFeatures(userKey, user, JSONUserFeatures);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of modifyUserFeatures method, of class PersonalREST.
     */
    @Test
    public void testModifyUserFeatures() {
        System.out.println("modifyUserFeatures");
        String userKey = "";
        String user = "";
        String JSONUserFeatures = "";
        PersonalREST instance = new PersonalREST();
        String expResult = "";
        String result = instance.modifyUserFeatures(userKey, user, JSONUserFeatures);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserProfile method, of class PersonalREST.
     */
    @Test
    public void testGetUserProfile() {
        System.out.println("getUserProfile");
        String userKey = "";
        String user = "";
        String pattern = "";
        String page = "";
        PersonalREST instance = new PersonalREST();
        String expResult = "";
        String result = instance.getUserProfile(userKey, user, pattern, page);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserAttributes method, of class PersonalREST.
     */
    @Test
    public void testGetUserAttributes() {
        System.out.println("getUserAttributes");
        String userKey = "";
        String user = "";
        String pattern = "";
        String page = "";
        PersonalREST instance = new PersonalREST();
        String expResult = "";
        String result = instance.getUserAttributes(userKey, user, pattern, page);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
