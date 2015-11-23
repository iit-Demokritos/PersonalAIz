/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.recommendationengine.onologies.FeedObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
public class RecommendationEngineTest {

    public RecommendationEngineTest() {
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
     * Test all Recommendation module
     */
    @Test
    public void testRecommendationEngine() {

        boolean expResult = true;
        boolean result;

        System.out.println("* JUnitTest: RecommenationEngine class");

        String username = "testReUser1";
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("age", "25");
        attributes.put("gender", "male");
        HashMap<String, String> info = new HashMap<>();

        //crete object
        FeedObject fo = new FeedObject("0", "el", true, new Date().getTime());

        //crete object 1
        FeedObject fo1 = new FeedObject("1", "el", true, new Date().getTime());

        ArrayList<String> text = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();
        text.add("Οι αθλητές θα έχουν τη δυνατότητα να χρησιμοποιούν παπούτσια πραγματικά κομμένα και ραμμένα στα μέτρα τους με στόχο τη βελτίωση των επιδόσεων τους");
        categories.add("Τεχνολογία");
        tags.add("παπούτσια");
        tags.add("αθλητές");

        fo1.setTexts(text);
        fo1.setCategories(categories);
        fo1.setTags(tags);

        //create object 2
        FeedObject fo2 = new FeedObject("2", "el", true, new Date().getTime());

        ArrayList<String> text2 = new ArrayList<>();
        ArrayList<String> categories2 = new ArrayList<>();
        ArrayList<String> tags2 = new ArrayList<>();
        text2.add("Ένα αμφιλεγόμενο ερευνητικό πρόγραμμα στην Ελβετία, με απώτερο στόχο την προσομοίωση ενός ανθρώπινου εγκεφάλου στον υπολογιστή, παρουσιάζει τα πρώτα σημαντικά αποτελέσματα: το ψηφιακό μοντέλο μιας περιοχής του εγκεφάλου του αρουραίου σε μέγεθος κόκκου άμμου.\nΌμως αρκετοί νευροεπιστήμονες δεν δείχνουν να πείθονται για τη χρησιμότητα της προσπάθειας.");
        categories2.add("Επιστήμη");
        tags2.add("έρευνα");

        fo2.setTexts(text2);
        fo2.setCategories(categories2);
        fo2.setTags(tags2);

        //create object 2
        FeedObject fo3 = new FeedObject("3", "en", true, new Date().getTime());

        ArrayList<String> text3 = new ArrayList<>();
        ArrayList<String> categories3 = new ArrayList<>();
        ArrayList<String> tags3 = new ArrayList<>();
        text3.add("North Korean leader Kim Jong Un declared Saturday that his country was ready to stand up to any threat posed by the United States as he spoke at a lavish military parade to mark the 70th anniversary of the North's ruling party and trumpet his third-generation leadership.");
        categories3.add("world");
        tags3.add("military");

        fo3.setTexts(text3);
        fo3.setCategories(categories3);
        fo3.setTags(tags3);

        //----------------------------------------------------------------------
        //Crete new client
        Client cl = new Client("root", "root");
        //Set client auth time
        cl.setAuthenticatedTimestamp(new Date().getTime());

        RecommendationEngine instance = new RecommendationEngine(cl);

        //add user
        System.out.println("* JUnitTest: addUser() method");
        result = instance.addUser(username, attributes, info);
        assertEquals(expResult, result);

        System.out.println("* JUnitTest: feed() method");
        //feed object 1
        for (int i = 0; i < 5; i++) {
            result = instance.feed(username, fo1);
            assertEquals(expResult, result);
        }

        //feed object 2
        for (int i = 0; i < 5; i++) {
            result = instance.feed(username, fo2);
            assertEquals(expResult, result);
        }

        //feed object 3
        for (int i = 0; i < 5; i++) {
            result = instance.feed(username, fo3);
            assertEquals(expResult, result);
        }

        //Get recommendations
        System.out.println("* JUnitTest: getRecommendation() method");
        ArrayList<FeedObject> recommendationList = new ArrayList<>();
        recommendationList.add(fo1);
        recommendationList.add(fo2);
        recommendationList.add(fo3);

        LinkedHashMap<String, Double> expReResult = new LinkedHashMap<>();
        LinkedHashMap<String, Double> recResult = instance.getRecommendation(username,
                recommendationList);
        expReResult.put("2", 0.676879603269757);
        expReResult.put("3", 0.6111415350830072);
        expReResult.put("1", 0.4400359866366431);

        assertEquals(expReResult, recResult);

        //delete user
        System.out.println("* JUnitTest: deleteUser() method");
        result = instance.deleteUser(username);
        assertEquals(expResult, result);

    }

}
