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
public class AdminRESTTest {

    public AdminRESTTest() {
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
     * Test of addClient, getClients, deleteClient method, of class Admin.
     */
    @Test
    public void testAddGetDeleteClients() {

        String userKey,
                clientName,
                clientPass,
                clientInfo,
                expResult,
                result;
        AdminREST instance;

        System.out.println("* JUnitTest: addClient() method");

        //Add Client | test-1
        instance = new AdminREST();
        userKey = "";
        clientName = "";
        clientPass = "";
        clientInfo = "";
        expResult = "";

        System.out.println("-- test-1 : userKey: " + userKey
                + " ,clientName: " + clientName
                + " ,clientPass: " + clientPass
                + " ,clientInfo: " + clientInfo);

        result = instance.addClient(userKey, clientName, clientPass, clientInfo);
        assertEquals(expResult, result);

        //Add Client | test-2
        instance = new AdminREST();
        userKey = "";
        clientName = "";
        clientPass = "";
        clientInfo = "";
        expResult = "";

        System.out.println("-- test-2 : userKey: " + userKey
                + " ,clientName: " + clientName
                + " ,clientPass: " + clientPass
                + " ,clientInfo: " + clientInfo);

        result = instance.addClient(userKey, clientName, clientPass, clientInfo);
        assertEquals(expResult, result);


        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getClients() method");
        //Get Clients | test-1
        instance = new AdminREST();
        userKey = "";
        expResult = "";
        result = instance.getClients(userKey);
        System.out.println("-- test-1 | getClients: ");
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
        

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteClients() method");
        
        
        //Delete Client | test-1
        instance = new AdminREST();
        userKey = "";
        clientName = "";
        expResult = "";
        System.out.println("-- test-1 | corrent delete client - DeleteClient: " 
                                                                + clientName);
        result = instance.deleteClient(userKey, clientName);
        assertEquals(expResult, result);
        
        
        
        
        
        
        
        
        
        
        
        
        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getClients() method");
        //Get Clients | test-2
        instance = new AdminREST();
        userKey = "";
        expResult = "";
        result = instance.getClients(userKey);
        System.out.println("-- test-2 | getClients: ");
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
        
        

    }

    /**
     * Test of deleteClient method, of class AdminREST.
     */
    @Test
    public void testDeleteClient() {
        System.out.println("deleteClient");
        String userKey = "";
        String clientName = "";
        AdminREST instance = new AdminREST();
        String expResult = "";
        String result = instance.deleteClient(userKey, clientName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getClients method, of class AdminREST.
     */
    @Test
    public void testGetClients() {
        System.out.println("getClients");
        String userKey = "";
        AdminREST instance = new AdminREST();
        String expResult = "";
        String result = instance.getClients(userKey);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSettings method, of class AdminREST.
     */
    @Test
    public void testSetSettings() {
        System.out.println("setSettings");
        String userKey = "";
        String JSONSettings = "";
        AdminREST instance = new AdminREST();
        String expResult = "";
        String result = instance.setSettings(userKey, JSONSettings);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSettings method, of class AdminREST.
     */
    @Test
    public void testGetSettings() {
        System.out.println("getSettings");
        String userKey = "";
        AdminREST instance = new AdminREST();
        String expResult = "";
        String result = instance.getSettings(userKey);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
