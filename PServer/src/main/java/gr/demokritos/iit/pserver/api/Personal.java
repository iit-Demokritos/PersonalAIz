/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.storage.HBase;
import gr.demokritos.iit.pserver.storage.interfaces.IPersonalStorage;
import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.interfaces.ISecurity;
import gr.demokritos.iit.utilities.configuration.PServerConfiguration;
import gr.demokritos.iit.utilities.json.JSon;
import gr.demokritos.iit.utilities.json.Output;
import gr.demokritos.iit.utilities.logging.Logging;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implement the Personal mode of PServer. It supports the personal
 * user model. In this mode PS store the user preferences (features) as long as
 * the demographic user information (attributes)
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class Personal {

    private final IPersonalStorage dbPersonal;
    private final PServerConfiguration psConfig;
    private Output output;
    private Client client;
    public static final Logger LOGGER = LoggerFactory.getLogger(Personal.class);
    public ISecurity security;

    /**
     * The constructor of personal mode.
     *
     * @param dbPersonal
     */
    public Personal(IPersonalStorage dbPersonal, Client c) {
        this.psConfig = new PServerConfiguration();
        this.dbPersonal = dbPersonal;
        this.client = c;
        security = null;
        Action aAddUsers = new Action("Personal.AddUsers");
        Action aRemoveUsers = new Action("Personal.RemoveUsers");
        //...
        // TODO: Make strings final
        //TODO: Complete
        
        //Update logging level 
        Logging.updateLoggerLevel(Personal.class, psConfig.getLogLevel());
    }

    public void setSecurity(ISecurity security) {
        this.security = security;
    }
    
    

    
    /**
     * Add a list of users on PServer Storage. This function help us to add
     * massive, users on PServer. Features or Attributes is optional.
     *
     * @param JSONUsers A JSON string with the users e.g.
     * {"test1":{"attributes":{"gender": "male","age": "18"},"features":
     * {"ftr1": "34","ftr3": "3","ftr5": "4"}}}
     * @return A JSON response with method status
     */
    public String addUsers(String JSONUsers) {
        //Initialize variables
        output = new Output();
        //convert JSON with users as a HashMap
        HashMap<String, Object> users = new HashMap<>(
                JSon.unjsonize(JSONUsers, HashMap.class));

        ArrayList<User> usersList = new ArrayList<>();

        //for each username create User object and add it on the list
        for (String cUser : users.keySet()) {

            // Get user UUID
            String UUID = dbPersonal.getUserUID(cUser);

            //Create new user object
            User user = new User(UUID);

            //add info on user
            HashMap<String, String> info = new HashMap<>();

            //set the user info
            user.setInfo(info);

            HashMap<String, HashMap<String, String>> userMap
                    = new HashMap<>();
            userMap.putAll(
                    (Map<? extends String, ? extends HashMap<String, String>>) users.get(cUser)
            );

            //add attributes on user
            if (userMap.containsKey("attributes")) {

                HashMap<String, String> attributes = new HashMap<>();
                attributes.putAll(userMap.get("attributes"));

                //set attributes on user
                user.setAttributes(attributes);
            }

            //add features on user
            if (userMap.containsKey("features")) {

                HashMap<String, String> features = new HashMap<>();
                features.putAll(userMap.get("features"));

                //set features on user
                user.setFeatures(features);
            }

            //add user on the users lsit
            usersList.add(user);
        }

        // call HBase AddUsers to add the users in HBase Storage 
        // and set the output code
        output.setOutputCode(dbPersonal.addUsers(usersList));
//        output.setCustomOutputMessage("custom message");

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Delete users from PServer storage basic on given pattern. If pattern is
     * null then the function will delete all users for this client.
     *
     * @param pattern The user pattern that we want to delete
     * @return A JSON response with method status
     */
    public String deleteUsers(String pattern) {
        //Initialize variables
        output = new Output();
        //call storage delete Users function with the pattern and add the return 
        //code on setOutputCode
        output.setOutputCode(dbPersonal.deleteUsers(pattern));
//        output.setCustomOutputMessage("test");
        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get a list with users in PServer basic on the given pattern.
     *
     * @param pattern The username pattern that we want in the user list. If the
     * pattern is null then the returned a list which contain the whole users.
     * @param page The page number. Page number will be greater or equal than 1
     * (page>=1). The list returned as page with 20 elements. With page
     * parameter you can ask for the first page, the second page... If page is
     * null or page<1 then return all elements in a single page. @return A JSON
     * response with the user
     * s list
     */
    public String getUsers(String pattern, Integer page) {
        //Initialize variables
        output = new Output();
        ArrayList<String> users = new ArrayList<>();

        //Check if page is null or page <1
        if (page == null || page < 1) {
            //set page null to return single page
            page = null;
        }
        //Call HBase to get Users
//        users.addAll(db.getUsers(pattern, page).keySet());
        users.addAll(dbPersonal.getUsers(pattern, page).keySet());
        output.setOutputCode(100);
//        output.setCustomOutputMessage("test");
        if (page != null) {
            output.setCustomOutputMessage("page " + HBase.paging);
        }
        output.setOutput(users);

        return JSon.jsonize(output, Output.class);
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
        //Initialize variables
        output = new Output();
        //convert JSON with users as a HashMap
        HashMap<String, HashMap<String, String>> users
                = new HashMap<>(
                        JSon.unjsonize(JSONUsersAttributes, HashMap.class));

        ArrayList<User> usersList = new ArrayList<>();

        //for each username create User object and add it on the list
        for (String cUser : users.keySet()) {

            //get current user UID
            String cUUID = dbPersonal.getUserUID(cUser);

            // Create new User object
            User user = new User(cUUID);
            user.setUsername(cUser);

            HashMap<String, String> attributes = new HashMap<>();
            attributes.putAll(users.get(cUser));

            //set attributes on user
            user.setAttributes(attributes);

            //add user on the users lsit
            usersList.add(user);
        }

        // call HBase setUsersAttributes to set the users Attributes in HBase Storage
        output.setOutputCode(dbPersonal.setUsersAttributes(usersList));
//        output.setCustomOutputMessage("custom message");

        return JSon.jsonize(output, Output.class);
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
    public String getUserAttributes(String user, String pattern, Integer page) {
        //Initialize variables
        output = new Output();
        HashMap<String, String> attributes = new HashMap<>();

        //Check if page is null or page <1
        if (page == null || page < 1) {
            //set page null to return single page
            page = null;
        }

        //Call HBase to get User attributes
        attributes.putAll(dbPersonal.getUserAttributes(user, pattern, page));
        output.setOutputCode(100);
//        output.setCustomOutputMessage("test");
        if (page != null) {
            output.setCustomOutputMessage("page " + HBase.paging);
        }
        output.setOutput(attributes);

        return JSon.jsonize(output, Output.class);
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
        //Initialize variables
        output = new Output();
        //convert JSON with users as a HashMap
        HashMap<String, HashMap<String, String>> users
                = new HashMap<>(JSon.unjsonize(JSONUsersFeatures, HashMap.class));

        ArrayList<User> usersList = new ArrayList<>();

        //for each username create User object and add it on the list
        for (String cUser : users.keySet()) {
            //get current user UID
            String cUUID = dbPersonal.getUserUID(cUser);

            //Create user object
            User user = new User(cUUID);
            user.setUsername(cUser);

            HashMap<String, String> features = new HashMap<>();
            features.putAll(users.get(cUser));

            //set features on user
            user.setFeatures(features);

            //add user on the users lsit
            usersList.add(user);
        }

        // call HBase setUsersFeatures to set the users Features in HBase Storage
        output.setOutputCode(dbPersonal.setUsersFeatures(usersList));

        return JSon.jsonize(output, Output.class);
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
        //Initialize variables
        output = new Output();
        //convert JSON with users as a HashMap
        HashMap<String, HashMap<String, String>> users
                = new HashMap<>(JSon.unjsonize(JSONUsersFeatures, HashMap.class));

        ArrayList<User> usersList = new ArrayList<>();

        //for each username create User object and add it on the list
        for (String cUser : users.keySet()) {
            //get current user UID
            String cUUID = dbPersonal.getUserUID(cUser);

            User user = new User(cUUID);
            user.setUsername(cUser);

            HashMap<String, String> features = new HashMap<>();
            features.putAll(users.get(cUser));

            //set features on user
            user.setFeatures(features);

            //add user on the users lsit
            usersList.add(user);
        }

        // call HBase setUsersFeatures to set the users Features in HBase Storage
        output.setOutputCode(dbPersonal.modifyUsersFeatures(usersList));

        return JSon.jsonize(output, Output.class);
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
    public String getUserFeatures(String user, String pattern, Integer page) {
        //Initialize variables
        output = new Output();
        HashMap<String, String> features = new HashMap<>();

        //Check if page is null or page <1
        if (page == null || page < 1) {
            //set page null to return single page
            page = null;
        }
        //Call HBase to get User features
        features.putAll(dbPersonal.getUserFeatures(user, pattern, page));
        output.setOutputCode(100);

        if (page != null) {
            output.setCustomOutputMessage("page " + HBase.paging);
        }
        output.setOutput(features);

        return JSon.jsonize(output, Output.class);
    }

    public boolean getPermissionFor(Action a, String sAccessType) {
        return ((security != null) && (security.getAccessRights(client, a).get(
                sAccessType)));
    }
}
