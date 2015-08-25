/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.security.authorization.Action;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
     * Test of addUser, addUsers, deleteUsers, getUsers methods, of class
     * Personal.
     */
    @Test
    public void testAddGetDeleteUsers() {
        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: addUser() method");
        User user;
        String username;
        Personal instance;
        boolean expResult;
        Client cl;

        //Add User | test-1
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //Create new User
        username = "user1";
        user = new User(username);

        expResult = true;
        System.out.println("-- test-1 | Add User with only Username - "
                + "un: " + username
                + " ,clientKey: root");
        assertEquals(expResult, instance.addUser(user));

        //Add User | test-2
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //Create new User
        username = "user2";
        user = new User(username);

        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("gender", "male");
        attributes.put("age", "18");
        user.setAttributes(attributes);

        expResult = true;
        System.out.println("-- test-2 | Add User with Username and Attributes - "
                + "un: " + username
                + " ,attributes: " + attributes
                + " ,clientKey: root");
        assertEquals(expResult, instance.addUser(user));

        //Add User | test-3
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //Create new User
        username = "user3";
        user = new User(username);

        HashMap<String, String> features = new HashMap<>();
        features.put("category.sport", "1");
        features.put("age", "18");
        user.setFeatures(features);

        expResult = true;
        System.out.println("-- test-3 | Add User with Username and Features - "
                + "un: " + username
                + " ,features: " + features
                + " ,clientKey: root");
        assertEquals(expResult, instance.addUser(user));

        //Add User | test-4
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //Create new User
        username = "user4";
        user = new User(username);

        user.setAttributes(attributes);
        user.setFeatures(features);

        expResult = true;
        System.out.println("-- test-4 | Add User with Username, Attributes and Features - "
                + "un: " + username
                + " ,attributes: " + attributes
                + " ,features: " + features
                + " ,clientKey: root");
        assertEquals(expResult, instance.addUser(user));

        //Add User | test-5
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //Create new User
        username = "user5";
        user = new User(username);
        String JSONUser = "{\"attributes\":{\"gender\":\"male\",\"age\":\"18\"},\"features\":{\"ftr1\":\"34\",\"ftr3\":\"3\",\"ftr5\":\"4\"}}";
        user.userCreator(JSONUser);

        expResult = true;
        System.out.println("-- test-5 | Add User with JSON userCreator - "
                + "un: " + username
                + " ,JSONUser: " + JSONUser
                + " ,clientKey: root");
        assertEquals(expResult, instance.addUser(user));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUsers() method");
        String pattern;
        Integer page;

        //Get Users | test-1
        pattern = null;
        page = null;
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        Set<String> getExpResult = new HashSet<>();
        getExpResult.add("user1");
        getExpResult.add("user2");
        getExpResult.add("user3");
        getExpResult.add("user4");
        getExpResult.add("user5");

        Set<String> result = instance.getUsers(pattern, page);
        System.out.println("-- test-1 | get users with - "
                + "pattern: " + pattern
                + " ,page: " + page
                + " ,clientKey: root");
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);

        //Get Users | test-3
        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: addUsers() method");
        ArrayList<User> usersList = new ArrayList<>();

        //Add Users | test-1
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        usersList.add(new User("user6"));
        usersList.add(new User("user7"));
        usersList.add(new User("user8"));

        expResult = true;
        System.out.println("-- test-1 | Add Users list with only Username - Un list: "
                + usersList
                + " ,clientKey: root");
        assertEquals(expResult, instance.addUsers(usersList));

        //Add Users | test-2
        //Add Users | test-3
        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUsers() method");
        //Get Users | test-2
        pattern = null;
        page = null;
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        getExpResult = new HashSet<>();
        getExpResult.add("user1");
        getExpResult.add("user2");
        getExpResult.add("user3");
        getExpResult.add("user4");
        getExpResult.add("user5");
        getExpResult.add("user6");
        getExpResult.add("user7");
        getExpResult.add("user8");

        result = instance.getUsers(pattern, page);
        System.out.println("-- test-2 | get users with - "
                + "pattern: " + pattern
                + " ,page: " + page
                + " ,clientKey: root");
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteUsers() method");

        //Delete Users | test-1
        pattern = "user";
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        expResult = true;
        System.out.println("-- test-1 | Delete Users - "
                + " pattern: " + pattern
                + " ,clientKey: root");
        assertEquals(expResult, instance.deleteUsers(pattern));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUsers() method");
        //Get Users | test-3
        pattern = null;
        page = null;
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        getExpResult = new HashSet<>();

        result = instance.getUsers(pattern, page);
        System.out.println("-- test-3 | get users with - "
                + "pattern: " + pattern
                + " ,page: " + page
                + " ,clientKey: root");
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);
        
        
    }
    /**
     * Test of setUserAttributes and getUserAttributes method, of class
     * Personal.
     */
    @Test
    public void testSetGetUserAttributes() {

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: addUser() method");
        User user;
        String username = "TestUserAttribute";
        Personal instance;
        boolean expResult;
        Client cl;

        //Add User | test-1
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //Create new User
        user = new User(username);

        expResult = true;
        System.out.println("-- test-1 | Add User with only Username - "
                + "un: " + username
                + " ,clientKey: root");
        assertEquals(expResult, instance.addUser(user));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setUserAttributes() method");

        //Set User Attributes | test-1
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //add user attributes
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("age", "18");
        attributes.put("gender", "male");

        expResult = true;
        System.out.println("-- test-1 | Set User attributes - "
                + "un: " + username
                + " ,attributes: " + attributes
                + " ,clientKey: root");

        assertEquals(expResult, instance.setUserAttributes(username, attributes));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserAttributes() method");

        //Get User Attributes | test-1
        String pattern = null;
        Integer page = null;
        HashMap<String, String> getExpResult = new HashMap<>();
        getExpResult.put("age", "18");
        getExpResult.put("gender", "male");

        Map<String, String> result = instance.getUserAttributes(username, pattern, page);
        System.out.println("-- test-1 | get user attributes with - "
                + "un: " + username
                + " ,pattern: " + pattern
                + " ,page: " + page
                + " ,clientKey: root");
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);

        
        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setUserAttributes() method");
        //Set User Attributes | test-2
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //add user attributes
        attributes = new HashMap<>();
        attributes.put("age", "25");
        attributes.put("gender", "female");

        expResult = true;
        System.out.println("-- test-2 | Set User attributes - "
                + "un: " + username
                + " ,attributes: " + attributes
                + " ,clientKey: root");

        assertEquals(expResult, instance.setUserAttributes(username, attributes));

        //Set User Attributes | test-3
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //add user attributes
        attributes = new HashMap<>();
        attributes.put("age", "25");
        attributes.put("gender", "female");

        expResult = false;
        System.out.println("-- test-3 | Set User attributes with no valid username - "
                + "un: noUser"
                + " ,attributes: " + attributes
                + " ,clientKey: root");

        assertEquals(expResult, instance.setUserAttributes("noUser", attributes));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserAttributes() method");

        //Get User Attributes | test-1
        pattern = null;
        page = null;
        getExpResult = new HashMap<>();
        getExpResult.put("age", "25");
        getExpResult.put("gender", "female");

        result = instance.getUserAttributes(username, pattern, page);
        System.out.println("-- test-2 | get user attributes with - "
                + "un: " + username
                + " ,pattern: " + pattern
                + " ,page: " + page
                + " ,clientKey: root");
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteUsers() method");

        //Delete Users | test-1
        pattern = username;
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        expResult = true;
        System.out.println("-- test-1 | Delete Users - "
                + " pattern: " + pattern
                + " ,clientKey: root");
        assertEquals(expResult, instance.deleteUsers(pattern));

        System.out.println(instance.getUsers(pattern, page));
    }
    /**
     * Test of setUserFeatures, modifyUserFeatures and getUserFeatures method,
     * of class Personal.
     */
    @Test
    public void testSetModifyGetUserFeatures() {

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: addUser() method");
        User user;
        String username = "TestUserFeature";
        Personal instance;
        boolean expResult;
        Client cl;

        //Add User | test-1
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //Create new User
        user = new User(username);

        expResult = true;
        System.out.println("-- test-1 | Add User with only Username - "
                + "un: " + username
                + " ,clientKey: root");
        assertEquals(expResult, instance.addUser(user));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setUserFeatures() method");

        //Set User Features | test-1
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //add user features
        HashMap<String, String> features = new HashMap<>();
        features.put("category.sport", "10");
        features.put("category.world", "5");
        features.put("en.txt", "test");

        expResult = true;
        System.out.println("-- test-1 | Set User features - "
                + "un: " + username
                + " ,features: " + features
                + " ,clientKey: root");

        assertEquals(expResult, instance.setUserFeatures(username, features));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserFeatures() method");

        //Get User features | test-1
        String pattern = null;
        Integer page = null;
        HashMap<String, String> getExpResult = new HashMap<>();
        getExpResult.put("category.sport", "10");
        getExpResult.put("category.world", "5");
        getExpResult.put("en.txt", "test");

        Map<String, String> result = instance.getUserFeatures(username, pattern, page);
        System.out.println("-- test-1 | get user features with - "
                + "un: " + username
                + " ,pattern: " + pattern
                + " ,page: " + page
                + " ,clientKey: root");
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setUserFeatures() method");

        //Set User features | test-2
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //add user features
        features = new HashMap<>();
        features.put("category.sport", "20");
        features.put("category.world", "35");
        features.put("en.txt", "test2");

        expResult = true;
        System.out.println("-- test-2 | Set User features - "
                + "un: " + username
                + " ,features: " + features
                + " ,clientKey: root");

        assertEquals(expResult, instance.setUserFeatures(username, features));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserFeatures() method");

        //Get User Features | test-2
        pattern = null;
        page = null;
        getExpResult = new HashMap<>();
        getExpResult.put("category.sport", "20");
        getExpResult.put("category.world", "35");
        getExpResult.put("en.txt", "test2");

        result = instance.getUserFeatures(username, pattern, page);
        System.out.println("-- test-2 | get user features with - "
                + "un: " + username
                + " ,pattern: " + pattern
                + " ,page: " + page
                + " ,clientKey: root");
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: modifyUserFeatures() method");

        //Modify User features | test-1
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //add user features
        features = new HashMap<>();
        features.put("category.sport", "15");
        features.put("category.world", "15");

        expResult = true;
        System.out.println("-- test-1 | modify User features - "
                + "un: " + username
                + " ,features: " + features
                + " ,clientKey: root");

        assertEquals(expResult, instance.modifyUserFeatures(username, features));
        
        //Modify User features | test-2
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        //add user features
        features = new HashMap<>();
        features.put("en.txt", "15");

        expResult = false;
        System.out.println("-- test-2 | modify User features no numeric - "
                + "un: " + username
                + " ,features: " + features
                + " ,clientKey: root");

        assertEquals(expResult, instance.modifyUserFeatures(username, features));

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserFeatures() method");

        //Get User Features | test-3
        pattern = null;
        page = null;
        getExpResult = new HashMap<>();
        getExpResult.put("category.sport", "35");
        getExpResult.put("category.world", "50");
        getExpResult.put("en.txt", "test2");

        result = instance.getUserFeatures(username, pattern, page);
        System.out.println("-- test-3 | get user features with - "
                + "un: " + username
                + " ,pattern: " + pattern
                + " ,page: " + page
                + " ,clientKey: root");
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + result);
        assertEquals(getExpResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteUsers() method");

        //Delete Users | test-1
        pattern = username;
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        instance = new Personal(new PServerHBase(), cl);

        expResult = true;
        System.out.println("-- test-1 | Delete Users - "
                + " pattern: " + pattern
                + " ,clientKey: root");
        assertEquals(expResult, instance.deleteUsers(pattern));

        System.out.println(instance.getUsers(pattern, page));

    }
}
