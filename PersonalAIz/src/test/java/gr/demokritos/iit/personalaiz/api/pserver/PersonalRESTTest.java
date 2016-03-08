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

    private final String clientAuthe = "root|root";

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
     * Test of addUsers, getUsers, deleteUsers method, of class PersonalREST.
     */
    @Test
    public void testAddGetDeleteUsers() {

        String expResult, result, pattern, page, JSONUsers;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUsers() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;

        System.out.println("-- test-1 | - getUsers: "
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUsers(clientAuthe, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get Users\",\"output\":[]}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: addUsers() method");
        instance = new PersonalREST();
        JSONUsers = "{\"user1\": {"
                + "\"features\": {\"category.sport\": \"3\" },"
                + "\"attributes\": {\"gender\": \"male\",\"age\": \"18\"}}}";

        System.out.println("-- test-1 | Single user - addUser:"
                + " JSONUsers: " + JSONUsers);

        result = instance.addUsers(clientAuthe, JSONUsers);

        expResult = "{\"outputMessage\":\"Add Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUsers() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;

        System.out.println("-- test-2 | - getUsers: "
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUsers(clientAuthe, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get Users\",\"output\":[\"user1\"]}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: addUsers() method");
        instance = new PersonalREST();
        JSONUsers = "{\"user2\": {"
                + "\"features\": {\"category.sport\": \"3\" },"
                + "\"attributes\": {\"gender\": \"male\",\"age\": \"18\"}},"
                + "\"user3\": {"
                + "\"features\": {\"category.sport\": \"3\" },"
                + "\"attributes\": {\"gender\": \"fmale\",\"age\": \"25\"}}"
                + "}";

        System.out.println("-- test-2 | Multi user - addUser:"
                + " JSONUsers: " + JSONUsers);

        result = instance.addUsers(clientAuthe, JSONUsers);

        expResult = "{\"outputMessage\":\"Add Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUsers() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;

        System.out.println("-- test-3 | - getUsers: "
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUsers(clientAuthe, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get Users\",\"output\":[\"user1\",\"user2\",\"user3\"]}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteUsers() method");
        instance = new PersonalREST();
        pattern = "user2";

        System.out.println("-- test-1 | - deleteUsers: "
                + " pattern: " + pattern);

        result = instance.deleteUsers(clientAuthe, pattern);

        expResult = "{\"outputMessage\":\"Delete Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUsers() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;

        System.out.println("-- test-4 | - getUsers: "
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUsers(clientAuthe, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get Users\",\"output\":[\"user1\",\"user3\"]}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteUsers() method");
        instance = new PersonalREST();
        pattern = null;

        System.out.println("-- test-2 |Delete all users - deleteUsers: "
                + " pattern: " + pattern);

        result = instance.deleteUsers(clientAuthe, pattern);

        expResult = "{\"outputMessage\":\"Delete Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUsers() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;

        System.out.println("-- test-5 | - getUsers: "
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUsers(clientAuthe, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get Users\",\"output\":[]}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

    }

    /**
     * Test of addUsers method, of class PersonalREST.
     */
    public void testAddUsers() {

        String JSONUsers, expResult, result;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: addUsers() method");
        instance = new PersonalREST();
        JSONUsers = "{\"user1\": {"
                + "\"features\": {\"category.sport\": \"3\" },"
                + "\"attributes\": {\"gender\": \"male\",\"age\": \"18\"}}}";

        System.out.println("-- test-1 | Single user - addUser:"
                + " JSONUsers: " + JSONUsers);

        result = instance.addUsers(clientAuthe, JSONUsers);

        expResult = "{\"outputMessage\":\"Add Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

    }

    /**
     * Test of deleteUsers method, of class PersonalREST.
     */
    public void testDeleteUsers() {
        String expResult, result, pattern;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteUsers() method");
        instance = new PersonalREST();
        pattern = "user2";

        System.out.println("-- test-1 | - deleteUsers: "
                + " pattern: " + pattern);

        result = instance.deleteUsers(clientAuthe, pattern);

        expResult = "{\"outputMessage\":\"Delete Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

    }

    /**
     * Test of getUsers method, of class PersonalREST.
     */
    public void testGetUsers() {
        String expResult, result, pattern, page;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUsers() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;

        System.out.println("-- test-1 | - getUsers: "
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUsers(clientAuthe, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get Users\",\"output\":[\"user1\"]}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

    }

    /**
     * Test of setUserAttributes, getUserAttributes, setUserFeatures method,
     * mofifyUserFeatures, getUserFeatures methods of class PersonalREST.
     */
    @Test
    public void testSetGetUserAttributes() {

        String expResult, result, pattern, page, JSONUsers, user, JSONUserAttributes;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: addUsers() method");
        instance = new PersonalREST();
        JSONUsers = "{\"AttributeTestUser\": {\"features\": { },\"attributes\": { }}}";

        System.out.println("-- test-1 | Single user - addUser:"
                + " JSONUsers: " + JSONUsers);

        result = instance.addUsers(clientAuthe, JSONUsers);

        expResult = "{\"outputMessage\":\"Add Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserAttributes() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;
        user = "AttributeTestUser";

        System.out.println("-- test-1 | - getUserAttributes: "
                + " user: " + user
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUserAttributes(clientAuthe, user, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get User Attributes\",\"output\":{}}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setUserAttributes() method");
        instance = new PersonalREST();
        user = "AttributeTestUser";
        JSONUserAttributes = "{\"gender\":\"male\",\"age\":\"18\"}";

        System.out.println("-- test-1 | - setUserAttributes: "
                + " user: " + user
                + " JSONUserAttributes: " + JSONUserAttributes);

        result = instance.setUserAttributes(clientAuthe, user, JSONUserAttributes);

        expResult = "{\"outputMessage\":\"Set User Attributes Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserAttributes() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;
        user = "AttributeTestUser";

        System.out.println("-- test-2 | - getUserAttributes: "
                + " user: " + user
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUserAttributes(clientAuthe, user, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get User Attributes\",\"output\":{\"gender\":\"male\",\"age\":\"18\"}}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteUsers() method");
        instance = new PersonalREST();
        pattern = "AttributeTestUser";

        System.out.println("-- test-1 | - deleteUsers: "
                + " pattern: " + pattern);

        result = instance.deleteUsers(clientAuthe, pattern);

        expResult = "{\"outputMessage\":\"Delete Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

    }

    /**
     * Test of setUserFeatures method, mofifyUserFeatures, getUserFeatures
     * methods of class PersonalREST.
     */
    @Test
    public void testSetModifyGetUserFeatures() {

        String expResult, result, pattern, page, JSONUsers, user, JSONUserFeatures;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: addUsers() method");
        instance = new PersonalREST();
        JSONUsers = "{\"FeatureTestUser\": {\"features\": { },\"attributes\": { }}}";

        System.out.println("-- test-1 | Single user - addUser:"
                + " JSONUsers: " + JSONUsers);

        result = instance.addUsers(clientAuthe, JSONUsers);

        expResult = "{\"outputMessage\":\"Add Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserProfile() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;
        user = "FeatureTestUser";

        System.out.println("-- test-1 | - getUserProfile: "
                + " user: " + user
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUserProfile(clientAuthe, user, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get User Profile\",\"output\":{}}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setUserFeatures() method");
        instance = new PersonalREST();
        user = "FeatureTestUser";
        JSONUserFeatures = "{\"category.sport\": \"5\",\"category.world\": \"0\"  }";

        System.out.println("-- test-1 | - setUserFeatures: "
                + " user: " + user
                + " JSONUserFeatures: " + JSONUserFeatures);

        result = instance.setUserFeatures(clientAuthe, user, JSONUserFeatures);

        expResult = "{\"outputMessage\":\"Set User Features Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: modifyUserFeatures() method");
        instance = new PersonalREST();
        user = "FeatureTestUser";
        JSONUserFeatures = "{\"category.world\": \"3\" }";

        System.out.println("-- test-1 | - modifyUserFeatures: "
                + " user: " + user
                + " JSONUserFeatures: " + JSONUserFeatures);

        result = instance.modifyUserFeatures(clientAuthe, user, JSONUserFeatures);

        expResult = "{\"outputMessage\":\"Modify User Features Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserProfile() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;
        user = "FeatureTestUser";

        System.out.println("-- test-2 | - getUserProfile: "
                + " user: " + user
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUserProfile(clientAuthe, user, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get User Profile\",\"output\":{\"category.sport\":\"5\",\"category.world\":\"3\"}}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: deleteUsers() method");
        instance = new PersonalREST();
        pattern = "FeatureTestUser";

        System.out.println("-- test-1 | - deleteUsers: "
                + " pattern: " + pattern);

        result = instance.deleteUsers(clientAuthe, pattern);

        expResult = "{\"outputMessage\":\"Delete Users Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

    }

    /**
     * Test of setUserAttributes method, of class PersonalREST.
     */
    public void testSetUserAttributes() {
        String expResult, result, JSONUserAttributes, user;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setUserAttributes() method");
        instance = new PersonalREST();
        user = "user1";
        JSONUserAttributes = "{\"age\": \"25\" }";

        System.out.println("-- test-1 | - setUserAttributes: "
                + " user: " + user
                + " JSONUserAttributes: " + JSONUserAttributes);

        result = instance.setUserAttributes(clientAuthe, user, JSONUserAttributes);

        expResult = "{\"outputMessage\":\"Set User Attributes Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

    }

    /**
     * Test of setUserFeatures method, of class PersonalREST.
     */
    public void testSetUserFeatures() {

        String expResult, result, JSONUserFeatures, user;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: setUserFeatures() method");
        instance = new PersonalREST();
        user = "user1";
        JSONUserFeatures = "{\"category.sport\": \"0\" }";

        System.out.println("-- test-1 | - setUserFeatures: "
                + " user: " + user
                + " JSONUserFeatures: " + JSONUserFeatures);

        result = instance.setUserFeatures(clientAuthe, user, JSONUserFeatures);

        expResult = "{\"outputMessage\":\"Set User Features Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of modifyUserFeatures method, of class PersonalREST.
     */
    public void testModifyUserFeatures() {
        String expResult, result, JSONUserFeatures, user;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: modifyUserFeatures() method");
        instance = new PersonalREST();
        user = "user1";
        JSONUserFeatures = "{\"category.sport\": \"0\" }";

        System.out.println("-- test-1 | - modifyUserFeatures: "
                + " user: " + user
                + " JSONUserFeatures: " + JSONUserFeatures);

        result = instance.modifyUserFeatures(clientAuthe, user, JSONUserFeatures);

        expResult = "{\"outputMessage\":\"Set User Features Complete\"}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUserProfile method, of class PersonalREST.
     */
    public void testGetUserProfile() {
        String expResult, result, pattern, page, user;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserProfile() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;
        user = "user1";

        System.out.println("-- test-1 | - getUserProfile: "
                + " user: " + user
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUserProfile(clientAuthe, user, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get User Profile\",\"output\":{\"category.sport\":\"3\"}}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);

    }

    /**
     * Test of getUserAttributes method, of class PersonalREST.
     */
    public void testGetUserAttributes() {
        String expResult, result, pattern, page, user;
        PersonalREST instance;

        //----------------------------------------------------------------------
        System.out.println("* JUnitTest: getUserAttributes() method");
        instance = new PersonalREST();
        pattern = null;
        page = null;
        user = "user1";

        System.out.println("-- test-1 | - getUserAttributes: "
                + " user: " + user
                + " pattern: " + pattern
                + " page: " + page);

        result = instance.getUserAttributes(clientAuthe, user, pattern, page);

        expResult = "{\"outputMessage\":\"Complete Get User Attributes\",\"output\":{\"gender\":\"male\",\"age\":\"18\"}}";
        System.out.println("|expect| " + expResult);
        System.out.println("|result| " + result);
        assertEquals(expResult, result);
    }
}
