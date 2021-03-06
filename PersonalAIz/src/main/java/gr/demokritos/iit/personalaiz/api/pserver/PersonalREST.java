/* 
 * Copyright 2016 IIT , NCSR Demokritos - http://www.iit.demokritos.gr,
 *                      SciFY NPO http://www.scify.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gr.demokritos.iit.personalaiz.api.pserver;

import gr.demokritos.iit.pserver.api.Personal;
import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import gr.demokritos.iit.pserver.storage.interfaces.IPersonalStorage;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.utilities.configuration.PersonalAIzConfiguration;
import gr.demokritos.iit.utilities.json.JSon;
import gr.demokritos.iit.utilities.json.Output;
import gr.demokritos.iit.utilities.logging.Logging;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the Personal mode API
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
@Path("pserver/{userAuthe}/personal/")
@Produces(MediaType.APPLICATION_JSON)
public class PersonalREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalREST.class);
    //TODO: Change HBase with something global to change storage from settings
    private final IPersonalStorage db = new PServerHBase();
    private Personal personal;
    private Client cl;
    private final SecurityLayer security = new SecurityLayer();
    private Output output = new Output();
    private final PersonalAIzConfiguration config = new PersonalAIzConfiguration();

    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Add users in PServer
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param JSONUsers
     * @return
     */
    @Path("users")
    @POST
    public String addUsers(
            @PathParam("userAuthe") String userAuthe,
            @FormParam("JSONUsers") String JSONUsers
    ) {
        String startTime = dateFormat.format(date.getTime());

        //Check if user Authentication is with username pass or api key
        if (userAuthe.contains("|")) {
            // Check the username - pass Credentials
            String[] credentials = userAuthe.split("\\|");

            if (!security.authe.checkCredentials(credentials[0], credentials[1])) {
                LOGGER.info("No valid Username: " + credentials[0]
                        + " and pass: " + credentials[1]);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(credentials[0], credentials[1]);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        } else {
            // Check the api key Credentials
            if (!security.authe.checkCredentials(userAuthe)) {
                LOGGER.info("No valid API Key: " + userAuthe);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(userAuthe);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //set security layer on PServer Personal
        personal.setSecurity(security);

        //convert JSONUsers to users objects
        ArrayList<User> usersList = new ArrayList<>();

        //convert JSON with users as a HashMap
        HashMap<String, Object> users = new HashMap<>(
                JSon.unjsonize(JSONUsers, HashMap.class));

        //for each username create User object and add it on the list
        for (String cUser : users.keySet()) {

            //Create new user object
            User user = new User(cUser);

            HashMap<String, HashMap<String, String>> userMap = new HashMap<>();
            userMap.putAll(
                    (Map<? extends String, ? extends HashMap<String, String>>) users.get(cUser)
            );

            if (userMap.containsKey("attributes")) {
                //set attributes on user
                user.setAttributes(userMap.get("attributes"));
            }

            //add features on user
            if (userMap.containsKey("features")) {
                //set features on user
                user.setFeatures(userMap.get("features"));
            }

            //add user on the users lsit
            usersList.add(user);
        }

        if (personal.addUsers(usersList)) {
            LOGGER.info("Complete Add Users");
            output.setCustomOutputMessage("Add Users Complete");
        } else {
            LOGGER.info("Failed Add Users");
            output.setCustomOutputMessage("Add Users Failed");
        }

        String stopTime = dateFormat.format(date.getTime());
        LOGGER.debug("add#" + startTime + "#" + stopTime);
        return JSon.jsonize(output, Output.class);
    }

    /**
     * Delete users from PServer
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param pattern The username pattern. If is null then delete all users
     * @return
     */
    @Path("users/delete")
    @POST
    public String deleteUsers(
            @PathParam("userAuthe") String userAuthe,
            @FormParam("pattern") String pattern
    ) {

        //Check if user Authentication is with username pass or api key
        if (userAuthe.contains("|")) {
            // Check the username - pass Credentials
            String[] credentials = userAuthe.split("\\|");

            if (!security.authe.checkCredentials(credentials[0], credentials[1])) {
                LOGGER.info("No valid Username: " + credentials[0]
                        + " and pass: " + credentials[1]);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(credentials[0], credentials[1]);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        } else {
            // Check the api key Credentials
            if (!security.authe.checkCredentials(userAuthe)) {
                LOGGER.info("No valid API Key: " + userAuthe);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(userAuthe);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //set security layer on PServer Personal
        personal.setSecurity(security);

        if (personal.deleteUsers(pattern)) {
            LOGGER.info("Complete Delete Users with pattern " + pattern);
            output.setCustomOutputMessage("Delete Users Complete");
        } else {
            LOGGER.info("Failed Delete Users with pattern " + pattern);
            output.setCustomOutputMessage("Delete Users Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get PServer Users
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param pattern The username pattern. If is null then return all the
     * usernames.
     * @param page The page number. If is null return results to single page.
     * @return
     */
    @Path("users")
    @GET
    public String getUsers(
            @PathParam("userAuthe") String userAuthe,
            @FormParam("pattern") String pattern,
            @FormParam("page") String page
    ) {

        //Check if user Authentication is with username pass or api key
        if (userAuthe.contains("|")) {
            // Check the username - pass Credentials
            String[] credentials = userAuthe.split("\\|");

            if (!security.authe.checkCredentials(credentials[0], credentials[1])) {
                LOGGER.info("No valid Username: " + credentials[0]
                        + " and pass: " + credentials[1]);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(credentials[0], credentials[1]);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        } else {
            // Check the api key Credentials
            if (!security.authe.checkCredentials(userAuthe)) {
                LOGGER.info("No valid API Key: " + userAuthe);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(userAuthe);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //set security layer on PServer Personal
        personal.setSecurity(security);

        int pageParam = -1;
        if (page != null) {
            pageParam = Integer.parseInt(page);
        }

        Set<String> users = personal.getUsers(pattern, pageParam);

        if (users != null) {
            LOGGER.info("Complete Get Users");
            output.setOutput(users);
            output.setCustomOutputMessage("Get Users Complete");
        } else {
            LOGGER.info("Failed Get Users");
            output.setCustomOutputMessage("Get Users Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Set user attributes
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param user The username
     * @param JSONUserAttributes {"gender":"male","age": "18"}
     * @return
     */
    @Path("users/{user}/attributes")
    @PUT
    public String setUserAttributes(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("user") String user,
            @FormParam("JSONUserAttributes") String JSONUserAttributes
    ) {

        //Check if user Authentication is with username pass or api key
        if (userAuthe.contains("|")) {
            // Check the username - pass Credentials
            String[] credentials = userAuthe.split("\\|");

            if (!security.authe.checkCredentials(credentials[0], credentials[1])) {
                LOGGER.info("No valid Username: " + credentials[0]
                        + " and pass: " + credentials[1]);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(credentials[0], credentials[1]);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        } else {
            // Check the api key Credentials
            if (!security.authe.checkCredentials(userAuthe)) {
                LOGGER.info("No valid API Key: " + userAuthe);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(userAuthe);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //set security layer on PServer Personal
        personal.setSecurity(security);

        HashMap<String, String> attributes = new HashMap<String, String>(
                JSon.unjsonize(JSONUserAttributes, HashMap.class));

        if (personal.setUserAttributes(user, attributes)) {
            LOGGER.info("Complete setUserAttributes " + attributes.toString());
            output.setCustomOutputMessage("Set User Attributes Complete");
        } else {
            LOGGER.info("Failed setUserAttributes " + attributes.toString());
            output.setCustomOutputMessage("Set User Attributes Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Set user features
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param user The username
     * @param JSONUserFeatures A json with user feature name - value
     * @return
     */
    @Path("users/{user}/features")
    @PUT
    public String setUserFeatures(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("user") String user,
            @FormParam("JSONUserFeatures") String JSONUserFeatures
    ) {

        //Check if user Authentication is with username pass or api key
        if (userAuthe.contains("|")) {
            // Check the username - pass Credentials
            String[] credentials = userAuthe.split("\\|");

            if (!security.authe.checkCredentials(credentials[0], credentials[1])) {
                LOGGER.info("No valid Username: " + credentials[0]
                        + " and pass: " + credentials[1]);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(credentials[0], credentials[1]);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        } else {
            // Check the api key Credentials
            if (!security.authe.checkCredentials(userAuthe)) {
                LOGGER.info("No valid API Key: " + userAuthe);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(userAuthe);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //set security layer on PServer Personal
        personal.setSecurity(security);

        HashMap<String, String> features = new HashMap<String, String>(
                JSon.unjsonize(JSONUserFeatures, HashMap.class));

        if (personal.setUserFeatures(user, features)) {
            LOGGER.info("Complete setUserFeatures " + features.toString());
            output.setCustomOutputMessage("Set User Features Complete");
        } else {
            LOGGER.info("Failed setUserFeatures " + features.toString());
            output.setCustomOutputMessage("Set User Features Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Modify user features
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param user The username
     * @param JSONUserFeatures A json with user feature name - value
     * @return
     */
    @Path("users/{user}/features/modify")
    @PUT
    public String modifyUserFeatures(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("user") String user,
            @FormParam("JSONUserFeatures") String JSONUserFeatures
    ) {
        String startTime = dateFormat.format(date.getTime());

        //Check if user Authentication is with username pass or api key
        if (userAuthe.contains("|")) {
            // Check the username - pass Credentials
            String[] credentials = userAuthe.split("\\|");

            if (!security.authe.checkCredentials(credentials[0], credentials[1])) {
                LOGGER.info("No valid Username: " + credentials[0]
                        + " and pass: " + credentials[1]);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(credentials[0], credentials[1]);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        } else {
            // Check the api key Credentials
            if (!security.authe.checkCredentials(userAuthe)) {
                LOGGER.info("No valid API Key: " + userAuthe);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(userAuthe);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //set security layer on PServer Personal
        personal.setSecurity(security);

        HashMap<String, String> features = new HashMap<String, String>(
                JSon.unjsonize(JSONUserFeatures, HashMap.class));

        if (personal.modifyUserFeatures(user, features)) {
            LOGGER.info("Complete modifyUserFeatures " + features.toString());
            output.setCustomOutputMessage("Modify User Features Complete");
        } else {
            LOGGER.info("Failed modifyUserFeatures " + features.toString());
            output.setCustomOutputMessage("Modify User Features Failed");
        }

        String stopTime = dateFormat.format(date.getTime());
        LOGGER.debug("modify#" + startTime + "#" + stopTime);
        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get user profile. A map with user feature names - value
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param user The username
     * @param pattern The feature name pattern. If is null return all the
     * features
     * @param page The page number. If is null return results to single page
     * @return
     */
    @Path("users/{user}/features")
    @GET
    public String getUserProfile(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("user") String user,
            @FormParam("pattern") String pattern,
            @FormParam("page") String page
    ) {
        String startTime = dateFormat.format(date.getTime());

        //Check if user Authentication is with username pass or api key
        if (userAuthe.contains("|")) {
            // Check the username - pass Credentials
            String[] credentials = userAuthe.split("\\|");

            if (!security.authe.checkCredentials(credentials[0], credentials[1])) {
                LOGGER.info("No valid Username: " + credentials[0]
                        + " and pass: " + credentials[1]);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(credentials[0], credentials[1]);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        } else {
            // Check the api key Credentials
            if (!security.authe.checkCredentials(userAuthe)) {
                LOGGER.info("No valid API Key: " + userAuthe);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(userAuthe);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //set security layer on PServer Personal
        personal.setSecurity(security);

        int pageParam = -1;
        if (page != null) {
            pageParam = Integer.parseInt(page);
        }

        Map<String, String> userProfile = personal.getUserFeatures(user, pattern, pageParam);

        if (userProfile != null) {
            LOGGER.info("Complete Get User Profile");
            output.setOutput(userProfile);
            output.setCustomOutputMessage("Complete Get User Profile");
        } else {
            LOGGER.info("User not exist");
            output.setCustomOutputMessage("User not exist");
        }

        String stopTime = dateFormat.format(date.getTime());
        LOGGER.debug("get#" + startTime + "#" + stopTime);
        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get User Attributes
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param user The username
     * @param pattern The attribute name pattern. If is null return all the
     * attributes
     * @param page The page number. If is null return the results in single page
     * @return
     */
    @Path("users/{user}/attributes")
    @GET
    public String getUserAttributes(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("user") String user,
            @FormParam("pattern") String pattern,
            @FormParam("page") String page
    ) {

        //Check if user Authentication is with username pass or api key
        if (userAuthe.contains("|")) {
            // Check the username - pass Credentials
            String[] credentials = userAuthe.split("\\|");

            if (!security.authe.checkCredentials(credentials[0], credentials[1])) {
                LOGGER.info("No valid Username: " + credentials[0]
                        + " and pass: " + credentials[1]);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(credentials[0], credentials[1]);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        } else {
            // Check the api key Credentials
            if (!security.authe.checkCredentials(userAuthe)) {
                LOGGER.info("No valid API Key: " + userAuthe);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
            //Create new client and set auth time
            cl = new Client(userAuthe);
            cl.setAuthenticatedTimestamp(new Date().getTime());
            //Create PServer Personal instance
            personal = new Personal(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //set security layer on PServer Personal
        personal.setSecurity(security);

        int pageParam = -1;
        if (page != null) {
            pageParam = Integer.parseInt(page);
        }

        Map<String, String> userAttributes = personal.getUserAttributes(user, pattern, pageParam);

        if (userAttributes != null) {
            LOGGER.info("Complete Get User Attributes");
            output.setOutput(userAttributes);
            output.setCustomOutputMessage("Complete Get User Attributes");
        } else {
            LOGGER.info("Failed Get User Attributes");
            output.setCustomOutputMessage("Get User Attributes Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

}
