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
        System.out.println("* JUnitTest: setUp() method");
        this.dbAdmin = new PServerHBase();
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
     * Test of addClient method, of class Admin.
     */
    @Test
    public void testAddClient() {
        System.out.println("* JUnitTest: addClient() method");
        String clientName, password;
        HashMap<String, String> info = new HashMap<>();
        Admin instance;
        boolean expResult;

        // test-1
        instance = new Admin(new PServerHBase(), new Client("root", "root"));
        clientName = "client1";
        password = "client1";
        info.clear();
        info.put("mail", "info@mail.com");
        expResult = true;
        System.out.println("** test-1 : un: " + clientName
                + " ,pw: " + password
                + " ,info: " + info.toString()
                + " ,clientKey: root");
        assertEquals(expResult, instance.addClient(clientName, password, info));

        // test-2
        instance = new Admin(dbAdmin, new Client("root","root"));
        clientName = "client2";
        password = "client2";
        expResult = true;
        System.out.println("** test-2 : un: " + clientName
                + " ,pw: " + password
                + " ,info: " + null
                + " ,clientKey: root");
        assertEquals(expResult, instance.addClient(clientName, password, null));

        // test-3
        instance = new Admin(dbAdmin, new Client("0000000000"));
        instance.setSecurity(new SecurityLayer());
        clientName = "client3";
        password = "client3";
        info.clear();
        info.put("mail", "info@mail.com");
        expResult = false;
        System.out.println("- test-3 : un: " + clientName
                + " ,pw: " + password
                + " ,info: " + info.toString()
                + " ,clientKey: 0000000000");
        assertEquals(expResult, instance.addClient(clientName, password, info));

    }

//    /**
//     * Test of deleteClient method, of class Admin.
//     */
//    @Test
//    public void testDeleteClient() {
//        System.out.println("* JUnitTest: deleteClient() method");
//        String clientName = "";
//        Admin instance = null;
//        boolean expResult = false;
//        boolean result = instance.deleteClient(clientName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getClients method, of class Admin.
//     */
//    @Test
//    public void testGetClients() {
//        System.out.println("* JUnitTest: getClients() method");
//        Admin instance = null;
//        Set<String> expResult = null;
//        Set<String> result = instance.getClients();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
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
//
//    /**
//     * Test of getSettings method, of class Admin.
//     */
//    @Test
//    public void testGetSettings() {
//        System.out.println("* JUnitTest: getSettings() method");
//        Admin instance = null;
//        Map<String, String> expResult = null;
//        Map<String, String> result = instance.getSettings();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setSettings method, of class Admin.
//     */
//    @Test
//    public void testSetSettings() {
//        System.out.println("* JUnitTest: setSettings() method");
//        Map<String, String> settings = null;
//        Admin instance = null;
//        boolean expResult = false;
//        boolean result = instance.setSettings(settings);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getPermissionFor method, of class Admin.
//     */
//    @Test
//    public void testGetPermissionFor() {
//        System.out.println("* JUnitTest: getPermissionFor() method");
//        Action a = null;
//        String sAccessType = "";
//        Admin instance = null;
//        boolean expResult = false;
//        boolean result = instance.getPermissionFor(a, sAccessType);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
