/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.storage.HBase;
import gr.demokritos.iit.pserver.utils.JSon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implement the Personal mode of PServer. It supports the personal
 * user model. In this mode PS store the user preferences (features) as long as
 * the demographic user information (attributes)
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class Personal {

    private static String clientUID;
    private static String output;
    private static HBase db;

    /**
     * The constructor of personal mode.
     *
     * @param clientKey The clients API-Key
     */
    public Personal(String clientKey) {
        //TODO: check clientkey 
        //TODO: get client UID
        clientUID = clientKey;
        //Create HBase stroge object
        db = new HBase(clientUID);

    }

    /**
     * Add a list of users on PServer Storage. This function help us to add
     * massive, users on PServer without features or attributes.
     *
     * @param JSONUsers A JSON string with the users e.g. ["user1","user2",...]
     * @return A JSON response with method status
     */
    public String addUsers(String JSONUsers) throws IOException {
        //convert JSON with users as a ArrayList
        //["user1","user2",...]
        ArrayList<String> users = new ArrayList<String>(
                JSon.unjsonize(JSONUsers, ArrayList.class));

        // call HBase AddUsers to add the users in HBase Storage
        db.addUsers(users);

        return null;
    }

    /**
     * Delete users from PServer storage basic on given pattern. If pattern is
     * null then the function will delete all users for this client.
     *
     * @param pattern The user pattern that we want to delete
     * @return A JSON response with method status
     */
    public String deleteUsers(String pattern) {

        //TODO: set DefaultValue("*") @QueryParam("pattern")  if pattern is null
        return null;
    }

    /**
     * Get a list with users in PServer basic on the given pattern.
     *
     * @param pattern The username pattern that we want in the user list. If the
     * pattern is null then the returned a list which contain the whole users.
     * @param page The page number. Page number will be greater or equal than 1
     * (page>=1). The list returned as page with 20 elements. With page
     * parameter you can ask for the first page, the second page... If page is
     * null or page<1 then return all elements in a single page.
     * @return A JSON response with the users list
     */
    public String getUsers(String pattern, Integer page) throws IOException {
        
        //Initialize variables
        ArrayList<String> users = new ArrayList<String>();
        
        //Check if pattern is null
        if (pattern == null) {
            //set pattern to return all users
            pattern = "*";
        }
        //Check if page is null
        if (page == null || page<1) {
            //set page to return single page
            page = 0;
        }

        //Call HBase to get Users
        users.addAll(db.getUsers(pattern, "ss"));
        
         
        return output = JSon.jsonize(users, ArrayList.class);
    }

    /**
     * Massive Set for each user in the list, the given attributes. If username
     * not exists then will be added on PServer.
     *
     * @param JSONUsersAttributes A JSON string with usernames and for each
     * username a key-value pairs. Key is the attribute name and value is the
     * attributes value. e.g. {"user1":{"gender":"male", "age":"18",
     * ...},"user2":{"gender":"fmale", "age":"28", ...}}
     * @return A JSON response with method succeed
     */
    public String setUsersAttributes(String JSONUsersAttributes) {

        return null;
    }

    /**
     * Set for specifically user the attributes. If username not exists then
     * will be added on PServer
     *
     * @param user The user name that we want to set the attributes
     * @param JSONUserAttributes A JSON string with key-value pairs. Key is the
     * attribute name and value is the attributes value. e.g. {"gender":"male",
     * "age":"18", ...}
     * @return A JSON response with method succeed
     */
    public String setUserAttributes(String user, String JSONUserAttributes) {

        return null;
    }

    /**
     * Get the attribute list for the given user.
     *
     * @param user The username that we want the attribute list.
     * @param pattern The attribute pattern that we want in the attribute list.
     * If the pattern is null then the returned a list which contain the whole
     * user's attributes.
     * @param page The page number. Page number will be greater or equal than 1
     * (page>=1). The list returned as page with 20 elements. With page
     * parameter you can ask for the first page, the second page... If page is
     * null then return all elements in a single page.
     * @return A JSON response with the attribute list
     */
    public String getUserAttributes(String user, String pattern, String page) {

          //TODO: set @DefaultValue("*") @QueryParam("pattern") if pattern is null 
        //TODO: set @DefaultValue("*") @QueryParam("page") if pattern is null
        return null;
    }

    /**
     * Massive Set for each user in the list, the given features. If username
     * not exists then will be added on PServer.
     *
     * @param JSONUsersFeatures A JSON string with usernames and for each
     * username a with key-value pairs. Key is the feature name and value. e.g.
     * {"user1":{"category.sport":"1", "category.economics":"8", ...},
     * "user2":{"category.sport":"12", "category.economics":"82", ...},...}
     * @return A JSON response with method succeed
     */
    public String setUsersFeatures(String JSONUsersFeatures) {

        return null;
    }

    /**
     * Set for specifically user the features. If username not exists then will
     * be added on PServer.
     *
     * @param user The username that we want to set the features
     * @param JSONUserFeatures A JSON string with key-value pairs. Key is the
     * feature name and value. e.g. {"category.sport":"1",
     * "category.economics":"8", ...}
     * @return A JSON response with method succeed
     */
    public String setUserFeatures(String user, String JSONUserFeatures) {

        return null;
    }

    /**
     * Massive Modify (increase/decrease) for each user in the list, the given
     * features. If username not exists then will be added on PServer.
     *
     * @param JSONUsersFeatures A JSON string with usernames and for each
     * username a key-value pairs. Key is the feature name and value is the
     * modification number. e.g. {"user1":{"category.sport":"-1",
     * "category.economics":"8", ...}, "user2":{"category.sport":"12",
     * "category.economics":"-2", ...},...}
     * @return A JSON response with method succeed
     */
    public String modifyUsersFeatures(String JSONUsersFeatures) {

        return null;
    }

    /**
     * Modify (increase/decrease) for specifically user the features. If
     * username not exists then will be added on PServer.
     *
     * @param user The username that we want to modify the features
     * @param JSONUserFeatures A JSON string with key-value pairs. Key is the
     * feature name and value is the modification number. e.g.
     * {"category.sport":"-1", "category.economics":"8", ...}
     * @return A JSON response with method succeed
     */
    public String modifyUserFeatures(String user, String JSONUserFeatures) {

        return null;
    }

    /**
     * Get the user profile. User profile is a list with user's features and
     * their values.
     *
     * @param user The username that we want to get the profile
     * @param pattern The feature pattern that we want in the profile. If the
     * pattern is null then the returned a list which contain the whole profile.
     * @param page The page number. Page number will be greater or equal than 1
     * (page>=1). The list returned as page with 20 elements. With page
     * parameter you can ask for the first page, the second page... If page is
     * null then return all elements in a single page.
     * @return A JSON response with the user's profile. A list of key-value
     * pairs for user's features.
     */
    public String getUserProfile(String user, String pattern, String page) {

          //TODO: set @DefaultValue("*") @QueryParam("pattern") if pattern is null 
        //TODO: set @DefaultValue("*") @QueryParam("page") if pattern is null
        return null;
    }

}
