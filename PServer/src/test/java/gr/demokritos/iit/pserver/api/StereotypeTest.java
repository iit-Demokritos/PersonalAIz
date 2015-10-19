/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

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

    @Test
    public void testStereotype() {
        //init users
        testInitAddUsers();
        //get System Attributes and adds two more users
        testGetSystemAttributes();
        //create two stereotypes
        testAddStereotype();
        //get stereotype names
        testGetStereotypes();
        //get stereotype users
        testGetStereotypeUsers();
        //get stereotype profile
        testGetStereotypeFeatures();
        //delete stereotype features
        testDeleteStereotypeFeatures();
        //update stereotype features
        testUpdateStereotypeFeatures();
        //set stereotype features
        testSetStereotypeFeatures();
        //update stereotype features
        testUpdateStereotypeFeatures();
        //modify stereotype features
        testModifyStereotypeFeatures();
        //update stereotype features
        testUpdateStereotypeFeatures();
        //get user stereotypes
        testGetUserStereotypes();
        // delete user form a stereotype
        testDeleteUserFromStereotype();
        // find users who satisfies the stereotype rule 
        testFindStereotypeUsers();
        //Add user on stereotype
        testAddUserOnStereotype();
        // Check users who not satisfies the stereotype rule 
        testCheckStereotypeUsers();

        //Remake stereotype
        testRemakeStereotype();
        //update stereotype users
        testUpdateStereotypeUsers();
        //delete stereotypes
        testDeleteStereotypes();

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        Personal instance = new Personal(new PServerHBase(), cl);
        instance.deleteUsers(null);

    }

    /**
     * Add 5 users on the system
     */
    public void testInitAddUsers() {

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new personal instance
        Personal instance = new Personal(new PServerHBase(), cl);

        Random r = new Random(50);
        ArrayList<User> usersList = new ArrayList<>();
        String username;
        User user;
        HashMap attributes, info, features;

        for (int i = 1; i <= 5; i++) {

            //Create new User
            username = "TestUser" + i;
            user = new User(username);

            attributes = new HashMap<>();

            if ((i & 1) == 0) {
                attributes.put("country", "en");
                attributes.put("gender", "male");
            } else {
                attributes.put("country", "gr");
                attributes.put("gender", "female");
            }
            attributes.put("age", Integer.toString(i * 10));

            info = new HashMap<>();
            info.put("Username", username);
            user.setInfo(info);
            user.setAttributes(attributes);

            features = new HashMap<>();
            features.put("category.sport", Integer.toString(i * 5));
            features.put("category.eco", Integer.toString(i * 2));
            features.put("txt.basket", Integer.toString(i * 2));
            features.put("txt.hellas", Integer.toString(i * 1));
            user.setFeatures(features);
            System.out.println("#adduser: " + username + " --> " + attributes);
            usersList.add(user);
        }
        instance.addUsers(usersList);
    }

    /**
     * Test of getSystemAttributes method, of class Stereotype.
     */
    public void testGetSystemAttributes() {
        Stereotype instance;
        Personal pInstance;
        Set<String> expResult = new HashSet<>();
        expResult.add("age");
        expResult.add("country");
        expResult.add("gender");
        Client cl;

        System.out.println("* JUnitTest: getSystemAttributes() method");
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        System.out.println("-- test-1 | Get Sytem Attributes -"
                + "clientKey: root");
        Set<String> result = instance.getSystemAttributes();
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        ArrayList<User> usersList = new ArrayList<>();

        System.out.println("* JUnitTest: add two new users with extra attribute occupation");
        expResult.add("occupation");
        pInstance = new Personal(new PServerHBase(), cl);
        //Create new User
        String username = "TestUser6";
        User user = new User(username);

        HashMap<String, String> attributes = new HashMap<>();

        attributes.put("gender", "male");
        attributes.put("age", Integer.toString(-15));
        attributes.put("country", "gr");

        HashMap<String, String> info = new HashMap<>();
        info.put("Username", username);
        user.setInfo(info);

        attributes.put("occupation", Integer.toString(3));

        user.setAttributes(attributes);

        HashMap<String, String> features = new HashMap<>();
        features.put("category.sport", Integer.toString(10));
        features.put("category.eco", Integer.toString(10));
        features.put("txt.basket", Integer.toString(10));
        features.put("txt.hellas", Integer.toString(10));
        user.setFeatures(features);
        System.out.println("#adduser: " + username + " --> " + attributes);
        usersList.add(user);

        //Create new User
        username = "TestUser7";
        user = new User(username);

        attributes = new HashMap<>();

        attributes.put("gender", "female");
        attributes.put("age", Integer.toString(-5));
        attributes.put("country", "en");

        info = new HashMap<>();
        info.put("Username", username);
        user.setInfo(info);

        attributes.put("occupation", Integer.toString(2));

        user.setAttributes(attributes);

        features = new HashMap<>();
        features.put("category.sport", Integer.toString(5));
        features.put("category.eco", Integer.toString(5));
        features.put("txt.basket", Integer.toString(5));
        features.put("txt.hellas", Integer.toString(5));
        user.setFeatures(features);
        System.out.println("#adduser: " + username + " --> " + attributes);
        usersList.add(user);

        pInstance.addUsers(usersList);

        System.out.println("* JUnitTest: getSystemAttributes() method");
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        System.out.println("-- test-2 | Get Sytem Attributes -"
                + "clientKey: root");
        result = instance.getSystemAttributes();
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

    }

    /**
     * Test of addStereotype method, of class Stereotype.
     */
    public void testAddStereotype() {

//#adduser: TestUser6 --> {country=gr, occupation=3, gender=male, age=-15}
//#adduser: TestUser7 --> {country=en, occupation=2, gender=female, age=-5}
//#adduser: TestUser1 --> {country=gr, gender=female, age=10}
//#adduser: TestUser2 --> {country=en, gender=male, age=20}
//#adduser: TestUser3 --> {country=gr, gender=female, age=30}
//#adduser: TestUser4 --> {country=en, gender=male, age=40}
//#adduser: TestUser5 --> {country=gr, gender=female, age=50}
        Stereotype instance;
        Personal pInstance;
        boolean expResult, result;
        Client cl;
        String rule = "";
        String stereotypeName = "";

        System.out.println("* JUnitTest: addStereotype() method");
        //Crete new client
        cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        //Test 1
        rule = "age<0";
        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-1 | Create Stereotype - Ster name:"
                + stereotypeName
                + " rule:"
                + rule
                + " clientKey: root");

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.addStereotype(stereotypeName, rule);
        assertEquals(expResult, result);
        //Test 2
        rule = "(age>25|AND|gender:male)|OR|country:el";
        stereotypeName = "SterMaleOver25";
        System.out.println("-- test-2 | Create Stereotype - Ster name:"
                + stereotypeName
                + " rule:"
                + rule
                + " clientKey: root");

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.addStereotype(stereotypeName, rule);
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteStereotypes method, of class Stereotype.
     */
    public void testDeleteStereotypes() {
        System.out.println("* JUnitTest: deleteStereotype() method");
        String pattern;
        Stereotype instance;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        pattern = "";
        System.out.println("-- test-1 | Delete Stereotype - pattern:"
                + pattern
                + " clientKey: root");

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.deleteStereotypes(pattern);
        assertEquals(expResult, result);

        pattern = null;
        System.out.println("-- test-2 | Delete all Stereotypes - pattern:"
                + pattern
                + " clientKey: root");

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.deleteStereotypes(pattern);
        assertEquals(expResult, result);

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        HashSet<String> getExpResult = new HashSet<>();
        HashSet<String> getResult = new HashSet<>(instance.getStereotypes(null, null));
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + getResult);
        assertEquals(getExpResult, getResult);
    }

    /**
     * Test of getStereotypes method, of class Stereotype.
     */
    public void testGetStereotypes() {
        System.out.println("* JUnitTest: getStereotypes() method");
        String pattern = null;
        Integer page = null;
        Set<String> expResult;
        Set<String> result;
        Stereotype instance;
        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = new HashSet<>();
        expResult.add("SterWithNegativeAge");
        expResult.add("SterMaleOver25");
        System.out.println("-- test-1 | Get Stereotypes - pattern:"
                + pattern
                + " page:"
                + page
                + " clientKey: root");
        result = instance.getStereotypes(pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = new HashSet<>();
        expResult.add("SterWithNegativeAge");
        expResult.add("SterMaleOver25");
        pattern = "Ster";
        System.out.println("-- test-2 | Get Stereotypes - pattern:"
                + pattern
                + " page:"
                + page
                + " clientKey: root");
        result = instance.getStereotypes(pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = new HashSet<>();
        expResult.add("SterMaleOver25");
        pattern = "SterMale";
        System.out.println("-- test-3 | Get Stereotypes - pattern:"
                + pattern
                + " page:"
                + page
                + " clientKey: root");
        result = instance.getStereotypes(pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of remakeStereotype method, of class Stereotype.
     */
    public void testRemakeStereotype() {
        System.out.println("* JUnitTest: remakeStereotype() method");
        String stereotypeName;
        Stereotype instance;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        stereotypeName = "ΝοSterWithNegativeAge";
        System.out.println("-- test-1 | Remake Stereotype with no existed ster name - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.remakeStereotype(stereotypeName);
        assertEquals(expResult, result);

        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-2 | Remake Stereotype - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.remakeStereotype(stereotypeName);
        assertEquals(expResult, result);
    }

    /**
     * Test of updateStereotypeFeatures method, of class Stereotype.
     */
    public void testUpdateStereotypeFeatures() {
        System.out.println("* JUnitTest: updateStereotypeFeatures() method");
        String stereotypeName;
        Stereotype instance;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        stereotypeName = "ΝοSterWithNegativeAge";
        System.out.println("-- test-1 | Update Stereotype features with no existed ster name - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.updateStereotypeFeatures(stereotypeName);
        assertEquals(expResult, result);

        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-2 | Update Stereotype features - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.updateStereotypeFeatures(stereotypeName);
        assertEquals(expResult, result);

        instance = new Stereotype(new PServerHBase(), cl);
        Map<String, String> gexpResult = new HashMap<>();
        gexpResult.put("category.sport", "7");
        gexpResult.put("category.eco", "7");
        gexpResult.put("txt.basket", "7");
        gexpResult.put("txt.hellas", "7");
        Map<String, String> gresult = instance.getStereotypeFeatures(stereotypeName, null, null);
        System.out.println("|expect| " + gexpResult);
        System.out.println("|result| " + gresult);
        assertEquals(gexpResult, gresult);

    }

    /**
     * Test of updateStereotypeUsers method, of class Stereotype.
     */
    public void testUpdateStereotypeUsers() {
        System.out.println("* JUnitTest: updateStereotypeUsers() method");
        String stereotypeName;
        Stereotype instance;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        stereotypeName = "ΝοSterWithNegativeAge";
        System.out.println("-- test-1 | Update Stereotype Users with no existed ster name - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.updateStereotypeUsers(stereotypeName);
        assertEquals(expResult, result);

        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-2 | Update Stereotype Users - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.updateStereotypeUsers(stereotypeName);
        assertEquals(expResult, result);

    }

    /**
     * Test of findStereotypeUsers method, of class Stereotype.
     */
    public void testFindStereotypeUsers() {
        System.out.println("* JUnitTest: findStereotypeUsers() method");
        String stereotypeName;
        Stereotype instance;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        stereotypeName = "ΝοSterWithNegativeAge";
        System.out.println("-- test-1 | Find Stereotype Users with no existed ster name - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.findStereotypeUsers(stereotypeName);
        assertEquals(expResult, result);

        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-2 | Find Stereotype Users - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.findStereotypeUsers(stereotypeName);
        assertEquals(expResult, result);

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        List<String> getExpResult = new ArrayList<>();
        getExpResult.add("TestUser7");
        getExpResult.add("TestUser6");
        List<String> getResult = instance.getStereotypeUsers(stereotypeName, null, null);
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + getResult);
        assertEquals(getExpResult, getResult);

    }

    /**
     * Test of checkStereotypeUsers method, of class Stereotype.
     */
    public void testCheckStereotypeUsers() {
        System.out.println("* JUnitTest: checkStereotypeUsers() method");
        String stereotypeName;
        Stereotype instance;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        stereotypeName = "ΝοSterWithNegativeAge";
        System.out.println("-- test-1 | Check Stereotype Users with no existed ster name - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.checkStereotypeUsers(stereotypeName);
        assertEquals(expResult, result);

        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-2 | Check Stereotype Users - ster name:"
                + stereotypeName
                + " clientKey: root");

        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.checkStereotypeUsers(stereotypeName);
        assertEquals(expResult, result);

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        List<String> getExpResult = new ArrayList<>();
        getExpResult.add("TestUser7");
        getExpResult.add("TestUser6");
        List<String> getResult = instance.getStereotypeUsers(stereotypeName, null, null);
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + getResult);
        assertEquals(getExpResult, getResult);

    }

    /**
     * Test of setStereotypeFeatures method, of class Stereotype.
     */
    public void testSetStereotypeFeatures() {
        System.out.println("* JUnitTest: setStereotypeFeatures() method");
        String stereotypeName;
        Map<String, String> features = new HashMap<>();
        features.put("category.sport", "78");
        features.put("category.eco", "37");
        features.put("txt.basket", "5");
        features.put("txt.hellas", "17");
        features.put("tag.hellas", "7");
        Stereotype instance;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        stereotypeName = "NoSterWithNegativeAge";
        System.out.println("-- test-1 | Set Stereotype features no existed ster name - ster name:"
                + stereotypeName
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.setStereotypeFeatures(stereotypeName, features);
        assertEquals(expResult, result);

        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-2 | Set Stereotype features - ster name:"
                + stereotypeName
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.setStereotypeFeatures(stereotypeName, features);
        assertEquals(expResult, result);

        instance = new Stereotype(new PServerHBase(), cl);
        Map<String, String> gexpResult = new HashMap<>();
        gexpResult.put("category.sport", "78");
        gexpResult.put("category.eco", "37");
        gexpResult.put("txt.basket", "5");
        gexpResult.put("txt.hellas", "17");
        gexpResult.put("tag.hellas", "7");
        Map<String, String> gresult = instance.getStereotypeFeatures(stereotypeName, null, null);
        System.out.println("|expect| " + gexpResult);
        System.out.println("|result| " + gresult);
        assertEquals(gexpResult, gresult);
    }

    /**
     * Test of modifyStereotypeFeatures method, of class Stereotype.
     */
    public void testModifyStereotypeFeatures() {
        System.out.println("* JUnitTest: modifyStereotypeFeatures() method");
        String stereotypeName;
        Map<String, String> features = new HashMap<>();
        features.put("category.sport", "10");
        features.put("category.eco", "-7");
        features.put("txt.basket", "3");
        features.put("txt.hellas", "20");
        features.put("tag.hellas", "7");
        Stereotype instance;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        stereotypeName = "NoSterWithNegativeAge";
        System.out.println("-- test-1 | Modify Stereotype features no existed ster name - ster name:"
                + stereotypeName
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.modifyStereotypeFeatures(stereotypeName, features);
        assertEquals(expResult, result);

        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-2 | Modify Stereotype features - ster name:"
                + stereotypeName
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.modifyStereotypeFeatures(stereotypeName, features);
        assertEquals(expResult, result);

        instance = new Stereotype(new PServerHBase(), cl);
        Map<String, String> gexpResult = new HashMap<>();
        gexpResult.put("category.sport", "17");
        gexpResult.put("category.eco", "0");
        gexpResult.put("txt.basket", "10");
        gexpResult.put("txt.hellas", "27");
        gexpResult.put("tag.hellas", "7");
        Map<String, String> gresult = instance.getStereotypeFeatures(stereotypeName, null, null);
        System.out.println("|expect| " + gexpResult);
        System.out.println("|result| " + gresult);
        assertEquals(gexpResult, gresult);
    }

    /**
     * Test of getStereotypeFeatures method, of class Stereotype.
     */
    public void testGetStereotypeFeatures() {
        System.out.println("* JUnitTest: getStereotypeFeatures() method");
        String stereotypeName;
        String pattern;
        Integer page = null;
        Stereotype instance;
        Map<String, String> expResult;
        Map<String, String> result;
        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        stereotypeName = "SterWithNegativeAge";
        pattern = null;
        System.out.println("-- test-1 | Get Stereotype profile - ster name:"
                + stereotypeName
                + " pattern: "
                + pattern
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = new HashMap<>();
        expResult.put("category.sport", "7");
        expResult.put("category.eco", "7");
        expResult.put("txt.basket", "7");
        expResult.put("txt.hellas", "7");
        result = instance.getStereotypeFeatures(stereotypeName, pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        stereotypeName = "NoSterWithNegativeAge";
        pattern = null;
        System.out.println("-- test-2 | Get Stereotype profile no existed ster name- ster name:"
                + stereotypeName
                + " pattern: "
                + pattern
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = null;
        result = instance.getStereotypeFeatures(stereotypeName, pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteStereotypeFeatures method, of class Stereotype.
     */
    public void testDeleteStereotypeFeatures() {
        System.out.println("* JUnitTest: deleteStereotypeFeatures() method");
        String stereotypeName;
        String pattern;
        Stereotype instance = null;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        stereotypeName = "NoSterWithNegativeAge";
        pattern = "txt.";
        System.out.println("-- test-1 | Delete Stereotype features no existed ster name - ster name:"
                + stereotypeName
                + " pattern: "
                + pattern
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.deleteStereotypeFeatures(stereotypeName, pattern);
        assertEquals(expResult, result);

        stereotypeName = "SterWithNegativeAge";
        pattern = "txt.";
        System.out.println("-- test-2 | Delete Stereotype features - ster name:"
                + stereotypeName
                + " pattern: "
                + pattern
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.deleteStereotypeFeatures(stereotypeName, pattern);
        assertEquals(expResult, result);

        instance = new Stereotype(new PServerHBase(), cl);
        Map<String, String> gexpResult = new HashMap<>();
        gexpResult.put("category.sport", "7");
        gexpResult.put("category.eco", "7");
        Map<String, String> gresult = instance.getStereotypeFeatures(stereotypeName, null, null);
        System.out.println("|expect| " + gexpResult);
        System.out.println("|result| " + gresult);
        assertEquals(gexpResult, gresult);
    }

    /**
     * Test of getStereotypeUsers method, of class Stereotype.
     */
    public void testGetStereotypeUsers() {
        System.out.println("* JUnitTest: getStereotypeUsers() method");
        String stereotypeName = "SterWithNegativeAge";
        String pattern = "";
        Integer page = null;
        Stereotype instance;
        List<String> result;
        List<String> expResult;
        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        System.out.println("-- test-1 | Get Stereotype Users - pattern:"
                + pattern
                + " page:"
                + page
                + " clientKey: root");
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = new ArrayList<>();
        expResult.add("TestUser7");
        expResult.add("TestUser6");
        result = instance.getStereotypeUsers(stereotypeName, pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        pattern = "TestUser0";
        System.out.println("-- test-2 | Get Stereotype Users - pattern:"
                + pattern
                + " page:"
                + page
                + " clientKey: root");
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = new ArrayList<>();
        result = instance.getStereotypeUsers(stereotypeName, pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        pattern = "TestUser6";
        System.out.println("-- test-3 | Get Stereotype Users - pattern:"
                + pattern
                + " page:"
                + page
                + " clientKey: root");
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = new ArrayList<>();
        expResult.add("TestUser6");
        result = instance.getStereotypeUsers(stereotypeName, pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUserStereotypes method, of class Stereotype.
     */
    public void testGetUserStereotypes() {

        System.out.println("* JUnitTest: getUserStereotypes() method");
        String username = "TestUser6";
        String pattern = null;
        Integer page = null;
        Stereotype instance;
        List<String> result;
        List<String> expResult;
        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        System.out.println("-- test-1 | Get User Stereotypes - un:" + username
                + " pattern:"
                + pattern
                + " page:"
                + page
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = new ArrayList<>();
        expResult.add("SterWithNegativeAge");
        result = instance.getUserStereotypes(username, pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        pattern = "NoExistedPattern";
        System.out.println("-- test-2 | Get User Stereotypes - un:" + username
                + " pattern:"
                + pattern
                + " page:"
                + page
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = new ArrayList<>();
        result = instance.getUserStereotypes(username, pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        pattern = null;
        username = "wrongname";
        System.out.println("-- test-3 | Get User Stereotypes wrong username - un:" + username
                + " pattern:"
                + pattern
                + " page:"
                + page
                + " clientKey: root");
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = null;
        result = instance.getUserStereotypes(username, pattern, page);
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of addUserOnStereotype method, of class Stereotype.
     */
    public void testAddUserOnStereotype() {
        System.out.println("* JUnitTest: addUserOnStereotype() method");
        String username, stereotypeName;
        Stereotype instance;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        username = "noUser";
        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-1 | Add user on Stereotype with no existed user - username:"
                + username
                + " ster name:"
                + stereotypeName
                + " clientKey: root");

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.addUserOnStereotype(username, stereotypeName);
        assertEquals(expResult, result);

        username = "TestUser5";
        stereotypeName = "noValidSter";
        System.out.println("-- test-2 | Add user on Stereotype with no existed ster - username:"
                + username
                + " ster name:"
                + stereotypeName
                + " clientKey: root");

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.addUserOnStereotype(username, stereotypeName);
        assertEquals(expResult, result);

        username = "TestUser5";
        stereotypeName = "SterWithNegativeAge";
        System.out.println("-- test-3 | Add user on Stereotype - username:"
                + username
                + " ster name:"
                + stereotypeName
                + " clientKey: root");

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.addUserOnStereotype(username, stereotypeName);
        assertEquals(expResult, result);

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        List<String> getExpResult = new ArrayList<>();
        getExpResult.add("TestUser7");
        getExpResult.add("TestUser6");
        getExpResult.add("TestUser5");
        List<String> getResult = instance.getStereotypeUsers(stereotypeName, null, null);
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + getResult);
        assertEquals(getExpResult, getResult);
    }

    /**
     * Test of DeleteUserFromStereotype method, of class Stereotype.
     */
    public void testDeleteUserFromStereotype() {
        System.out.println("* JUnitTest: DeleteUserFromStereotype() method");
        String stereotypeName = "SterWithNegativeAge";
        String username = "TestUser7";
        Stereotype instance = null;
        boolean expResult, result;

        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        username = "NoTestUser7";
        System.out.println("-- test-1 | Delete user from Stereotype with no existed username - username:"
                + username
                + " ster name:"
                + stereotypeName
                + " clientKey: root");
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = false;
        result = instance.deleteUserFromStereotype(username, stereotypeName);
        assertEquals(expResult, result);

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        List<String> getExpResult = new ArrayList<>();
        getExpResult.add("TestUser7");
        getExpResult.add("TestUser6");
        List<String> getResult = instance.getStereotypeUsers(stereotypeName, null, null);
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + getResult);
        assertEquals(getExpResult, getResult);

        username = "TestUser7";
        System.out.println("-- test-2 | Delete user from Stereotype - username:"
                + username
                + " ster name:"
                + stereotypeName
                + " clientKey: root");
        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        expResult = true;
        result = instance.deleteUserFromStereotype(username, stereotypeName);
        assertEquals(expResult, result);

        //Create new stereotype instance
        instance = new Stereotype(new PServerHBase(), cl);
        getExpResult = new ArrayList<>();
        getExpResult.add("TestUser6");
        getResult = instance.getStereotypeUsers(stereotypeName, null, null);
        System.out.println("|expect| " + getExpResult);
        System.out.println("|result| " + getResult);
        assertEquals(getExpResult, getResult);

    }

}
