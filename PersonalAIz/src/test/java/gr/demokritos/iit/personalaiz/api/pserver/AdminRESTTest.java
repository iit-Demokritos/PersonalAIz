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
 * @author Panagiotis Giotis <giotis.p@gmail.com>
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
     * Test of addClient method, of class AdminREST.
     */
//    @Test
//    public void testAddClient() {
//        System.out.println("* JUnitTest: addClient() method");
//        String userAuthe, clientName, clientPass, clientInfo, expResult;
//        //Create Admin Instance
//        AdminREST instance = new AdminREST();
//
//        userAuthe = "root|root";
//        clientName = "client1";
//        clientPass = "12345";
//        clientInfo = "{'mail':'info@mail.com'}";
//
//        System.out.println("-- test-1 |  - AddClient:");
//
//        String result = instance.addClient(
//                userAuthe,
//                clientName,
//                clientPass,
//                clientInfo);
//        expResult = "{\"outputMessage\":\"Add Client Complete\"}";
//        System.out.println("|expect| " + expResult);
//        System.out.println("|result| " + result);
//        assertEquals(expResult, result);
//
//    }
    /**
     * Test of deleteClient method, of class AdminREST.
     */
//    @Test
//    public void testDeleteClient() {
//        System.out.println("* JUnitTest: DeleteClient() method");
//        String userAuthe, clientName, expResult;
//        AdminREST instance = new AdminREST();
//        
//        System.out.println("-- test-1 |  - deletClient:");
//        userAuthe = "root|root";
//        clientName="client1";
//
//        String result = instance.deleteClient(userAuthe, clientName);
//
//        expResult = "{\"outputMessage\":\"Delete Client Complete\"}";
//        System.out.println("|expect| " + expResult);
//        System.out.println("|result| " + result);
//        assertEquals(expResult, result);
//    }
//    /**
//     * Test of getClients method, of class AdminREST.
//     */
//    @Test
//    public void testGetClients() {
//        System.out.println("* JUnitTest: getClients() method");
//        String userAuthe, expResult;
//        //Create Admin Instance
//        AdminREST instance = new AdminREST();
//
//        userAuthe = "root|root";
//        System.out.println("-- test-1 |  - getClients:");
//
//        String result = instance.getClients(userAuthe);
//        expResult = "{\"output\":[\"user1\",\"root\"]}";
//        System.out.println("|expect| " + expResult);
//        System.out.println("|result| " + result);
//        assertEquals(expResult, result);
//    }
    /**
     * Test of setSettings getSettings method, of class AdminREST.
     */
//    @Test
//    public void testSetGetSettings() {
//
//        //----------------------------------------------------------------------
//        System.out.println("* JUnitTest: getSettings() method");
//        String userAuthe, expResult;
//        AdminREST instance = new AdminREST();
//        userAuthe = "root|root";
//        System.out.println("-- test-1 |  - getSettings:");
//        String result = instance.getSettings(userAuthe);
//        expResult = "{\"outputMessage\":\"Get PServer settings complete\","
//                + "\"output\":{\"LogLevel\":\"info\"}}";
//        System.out.println("|expect| " + expResult);
//        System.out.println("|result| " + result);
//        assertEquals(expResult, result);
//
//        //----------------------------------------------------------------------
//        System.out.println("* JUnitTest: setSettings() method");
//        String JSONSettings;
//        System.out.println("-- test-1 |  - setSettings:");
//        userAuthe = "root|root";
//        JSONSettings = "{\"LogLevel\":\"debug\"}";
//
//        result = instance.setSettings(userAuthe, JSONSettings);
//        expResult = "{\"outputMessage\":\"Set PServer settings complete\"}";
//
//        System.out.println("|expect| " + expResult);
//        System.out.println("|result| " + result);
//        assertEquals(expResult, result);
//
//        //----------------------------------------------------------------------
//        System.out.println("* JUnitTest: getSettings() method");
//        userAuthe = "root|root";
//        System.out.println("-- test-2 |  - getSettings:");
//        result = instance.getSettings(userAuthe);
//        expResult = "{\"outputMessage\":\"Get PServer settings complete\","
//                + "\"output\":{\"LogLevel\":\"debug\"}}";
//        System.out.println("|expect| " + expResult);
//        System.out.println("|result| " + result);
//        assertEquals(expResult, result);
//
//        //----------------------------------------------------------------------
//        System.out.println("* JUnitTest: setSettings() method");
//
//        System.out.println("-- test-2 |  - setSettings:");
//        userAuthe = "root|root";
//        JSONSettings = "{\"LogLevel\":\"info\"}";
//
//        result = instance.setSettings(userAuthe, JSONSettings);
//        expResult = "{\"outputMessage\":\"Set PServer settings complete\"}";
//
//        System.out.println("|expect| " + expResult);
//        System.out.println("|result| " + result);
//        assertEquals(expResult, result);
//
//    }

//    /**
//     * Test of setSettings method, of class AdminREST.
//     */
//    @Test
//    public void testSetSettings() {
//        System.out.println("* JUnitTest: setSettings() method");
//        String userAuthe, JSONSettings, expResult;
//        AdminREST instance = new AdminREST();
//
//        System.out.println("-- test-1 |  - setSettings:");
//        userAuthe = "root|root";
//        JSONSettings = "{\"LogLevel\":\"debug\"}";
//
//        String result = instance.setSettings(userAuthe, JSONSettings);
//        expResult = "{\"outputMessage\":\"Set PServer settings complete\"}";
//
//        System.out.println("|expect| " + expResult);
//        System.out.println("|result| " + result);
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of getSettings method, of class AdminREST.
//     */
//    @Test
//    public void testGetSettings() {
//        System.out.println("* JUnitTest: getSettings() method");
//        String userAuthe, expResult;
//        AdminREST instance = new AdminREST();
//        userAuthe = "root|root";
//        System.out.println("-- test-1 |  - getSettings:");
//        String result = instance.getSettings(userAuthe);
//        expResult = "{\"outputMessage\":\"Get PServer settings complete\","
//                + "\"output\":{\"LogLevel\":\"debug\"}}";
//        System.out.println("|expect| " + expResult);
//        System.out.println("|result| " + result);
//        assertEquals(expResult, result);
//    }
}
