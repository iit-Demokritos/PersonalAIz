/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import gr.demokritos.iit.pserver.storage.interfaces.IAdminStorage;
import gr.demokritos.iit.security.SecurityLayer;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
public class AdminTest {

    private IAdminStorage dbAdmin;

    public AdminTest() {
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
     * Test of setSecurity method, of class Admin.
     */
//    @Test
//    public void testSetSecurity() {
//        System.out.println("* JUnitTest: setSecurity() method");
//        SecurityLayer security = new SecurityLayer();
//        Admin instance = new Admin(dbAdmin, new Client("root", "root"));
//        instance.setSecurity(security);
//    }
    /**
     * Test of addClient, getClients, deleteClient method, of class Admin.
     */
    @Test
    public void testAddGetDeleteClients() {
        System.out.println("* JUnitTest: addClient() method");
        String clientName, password;
        HashMap<String, String> info = new HashMap<>();
        Admin instance;
        boolean expResult;

        //Add Client | test-1
        Client cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        clientName = "client1";
        password = "client1";
        info.clear();
        info.put("mail", "info@mail.com");
        expResult = true;
        System.out.println("-- test-1 : un: " + clientName
                + " ,pw: " + password
                + " ,info: " + info.toString()
                + " ,clientKey: root");
        assertEquals(expResult, instance.addClient(clientName, password, info));

        //Add Client | test-2
        instance = new Admin(new PServerHBase(), cl);
        clientName = "client2";
        password = "client2";
        expResult = true;
        System.out.println("-- test-2 : un: " + clientName
                + " ,pw: " + password
                + " ,info: " + null
                + " ,clientKey: root");
        assertEquals(expResult, instance.addClient(clientName, password, null));

        //Add Client | test-3
        cl = new Client("root", "root");
        instance = new Admin(new PServerHBase(), cl);
        instance.setSecurity(new SecurityLayer());
        clientName = "client";
        password = "client";
        info.clear();
        info.put("mail", "info@mail.com");
        expResult = false;
        System.out.println("-- test-3 : un: " + clientName
                + " ,pw: " + password
                + " ,info: " + info.toString()
                + " ,clientKey: root without premmision");
        assertEquals(expResult, instance.addClient(clientName, password, info));

        //Add Client | test-4
        instance = new Admin(new PServerHBase(), new Client("0000000000"));
        instance.setSecurity(new SecurityLayer());
        clientName = "client";
        password = "client";
        info.clear();
        info.put("mail", "info@mail.com");
        expResult = false;
        System.out.println("-- test-4 : un: " + clientName
                + " ,pw: " + password
                + " ,info: " + info.toString()
                + " ,clientKey: 0000000000");
        assertEquals(expResult, instance.addClient(clientName, password, info));

        //Add Client | test-5
        cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        clientName = "client3";
        password = "client3";
        info.clear();
        info.put("mail", "info@mail.com");
        expResult = true;
        System.out.println("-- test-5 : un: " + clientName
                + " ,pw: " + password
                + " ,info: " + info.toString()
                + " ,clientKey: root");
        assertEquals(expResult, instance.addClient(clientName, password, info));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getClients() method");

        //get Clients | test-1
        cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        instance.setSecurity(new SecurityLayer());
        Set<String> getExpResult = new HashSet<>();
        getExpResult.add("client1");
        getExpResult.add("client2");
        getExpResult.add("client3");
        getExpResult.add("root");
        Set<String> result = instance.getClients();
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteClient() method");

        //Delete Client | test-1
        cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        instance.setSecurity(new SecurityLayer());
        clientName = "client1";
        expResult = true;
        System.out.println("-- test-1 | corrent delete client - DeleteClient: " 
                + clientName
                + " ,clientKey: root");
        assertEquals(expResult, instance.deleteClient(clientName));
        clientName = "client2";
        expResult = true;
        System.out.println("-- test-1 | corrent delete client - DeleteClient: " 
                + clientName
                + " ,clientKey: root");
        assertEquals(expResult, instance.deleteClient(clientName));
        clientName = "client3";
        expResult = true;
        System.out.println("-- test-1 | corrent delete client - DeleteClient: " 
                + clientName
                + " ,clientKey: root");
        assertEquals(expResult, instance.deleteClient(clientName));

        //Delete Client | test-2
        cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        instance.setSecurity(new SecurityLayer());
        clientName = "client3";
        expResult = true;
        System.out.println("-- test-2 | User Not exist - DeleteClient: " 
                + clientName
                + " ,clientKey: root");
        assertEquals(expResult, instance.deleteClient(clientName));

        //Delete Client | test-3
        cl = new Client("0000000000");
        instance = new Admin(new PServerHBase(), cl);
        instance.setSecurity(new SecurityLayer());
        clientName = "client3";
        expResult = false;
        System.out.println("-- test-3 | wrong clientKey - DeleteClient: " 
                + clientName
                + " ,clientKey: 0000000000");
        assertEquals(expResult, instance.deleteClient(clientName));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getClients() method");

        //get Clients | test-1
        cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        instance.setSecurity(new SecurityLayer());
        getExpResult = new HashSet<>();
        getExpResult.add("root");
        result = instance.getClients();
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);

    }

    /**
     * Test of getSettings and setSettings method, of class Admin.
     */
    @Test
    public void testSetGetSettings() {
        Admin instance;
        Client cl;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setSettings() method");
        //set settings | test-1
        HashMap<String, String> settings = new HashMap<>();
        boolean expResult = true;
        settings.put("LogLevel", "debug");
        settings.put("test", "info");

        cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        System.out.println("-- test-1 | setSettings: " + settings);
        boolean result = instance.setSettings(settings);

        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getSettings() method");
        //get settings | test-1
        cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        System.out.println("-- test-1 | getSettings: ");
        System.out.println("|expect| " + settings);
        System.out.println("|result| " + instance.getSettings());
        assertEquals(settings, instance.getSettings());

        
        
        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setSettings() method");
        //set settings | test-2
        expResult = true;
        settings = new HashMap<>();
        settings.put("LogLevel", "info");

        cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        System.out.println("-- test-2 | setSettings: " + settings);
        result = instance.setSettings(settings);

        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getSettings() method");
        //get settings | test-2
        cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        instance = new Admin(new PServerHBase(), cl);
        System.out.println("-- test-2 | getSettings: ");
        System.out.println("|expect| " + settings);
        System.out.println("|result| " + instance.getSettings());
        assertEquals(settings, instance.getSettings());

    }
    
    
    
//    /**
//     * Test of setClientRoles method, of class Admin.
//     */
//    @Test
//    public void testSetClientRoles() {
//        System.out.println("* JUnitTest: setClientRoles() method");
//        Admin instance = null;
//        boolean expResult = false;
//        boolean result = instance.setClientRoles();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getClientRoles method, of class Admin.
//     */
//    @Test
//    public void testGetClientRoles() {
//        System.out.println("* JUnitTest: getClientRoles() method");
//        String clientName = "";
//        Admin instance = null;
//        List<String> expResult = null;
//        List<String> result = instance.getClientRoles(clientName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
