/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.api.recommendationengine;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.recommendationengine.api.RecommendationEngine;
import gr.demokritos.iit.recommendationengine.onologies.FeedObject;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.utilities.configuration.RecommendationConfiguration;
import gr.demokritos.iit.utilities.json.JSon;
import gr.demokritos.iit.utilities.json.Output;
import gr.demokritos.iit.utilities.logging.Logging;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the REST API to expose the recommendation engine module
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
@Path("recengine/{userAuthe}/")
@Produces(MediaType.APPLICATION_JSON)
public class RecommendationEngineRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationEngineRest.class);
    private Client cl;
    private final SecurityLayer security = new SecurityLayer();
    private final Output output = new Output();
    private RecommendationEngine re;
    private final RecommendationConfiguration config = new RecommendationConfiguration();

    /**
     * Add user on Recommendation Engine
     *
     * @param userAuthe The Client Credentials
     * @param username The username
     * @param JSONUser The User attributes or information
     * @return A JSON with process result
     */
    @Path("user/{username}")
    @POST
    public String addUser(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("username") String username,
            @FormParam("JSONUser") String JSONUser
    ) {

        LOGGER.debug("#addUser | userAuthe: " + userAuthe
                + " username: " + username
                + " JSONUser: " + JSONUser);

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
            //Create Recommendation Engine instance
            re = new RecommendationEngine(cl);
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
            //Create Recommendation Engine instance
            re = new RecommendationEngine(cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(RecommendationEngineRest.class, config.getLogLevel());

        //set security layer on Recommendation Engine
        re.setSecurity(security);

        HashMap<String, String> attributes = null;
        HashMap<String, String> info = null;

        if (JSONUser != null) {

            HashMap<String, HashMap<String, String>> userMap;
            userMap = new HashMap<>(
                    JSon.unjsonize(JSONUser, HashMap.class));

            //If contains attributes
            if (userMap.containsKey("attributes")) {
                //add attributes on user
                attributes = new HashMap<>();
                attributes.putAll(userMap.get("attributes"));
            }
            //If contains info
            if (userMap.containsKey("info")) {
                //add info on user
                info = new HashMap<>();
                info.putAll(userMap.get("info"));
            }

        }

        if (re.addUser(username, attributes, info)) {
            LOGGER.info("Complete Add User");
            output.setCustomOutputMessage("Add User Complete");
        } else {
            LOGGER.info("Failed Add User");
            output.setCustomOutputMessage("Add User Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Delete a user from Recommendation Engine
     *
     * @param userAuthe The client credentials
     * @param username The username
     * @return A JSON with process result
     */
    @Path("user/delete/{username}")
    @POST
    public String deleteUser(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("username") String username
    ) {

        LOGGER.debug("#deleteUser | userAuthe: " + userAuthe
                + " username: " + username);

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
            //Create Recommendation Engine instance
            re = new RecommendationEngine(cl);
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
            //Create Recommendation Engine instance
            re = new RecommendationEngine(cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(RecommendationEngineRest.class, config.getLogLevel());

        //set security layer on Recommendation Engine
        re.setSecurity(security);

        if (re.deleteUser(username)) {
            LOGGER.info("Complete Delete User");
            output.setCustomOutputMessage("Delete User Complete");
        } else {
            LOGGER.info("Failed Delete User");
            output.setCustomOutputMessage("Delete User Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Feed recommendation engine with user preferences
     *
     * @param userAuthe The client credentials
     * @param username The username
     * @param JSONObject The feed object that user click on it
     * @return A JSON with process result
     */
    @Path("feed/{username}")
    @POST
    public String feed(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("username") String username,
            @FormParam("JSONObject") String JSONObject
    ) {

        LOGGER.debug("#feed | userAuthe: " + userAuthe
                + " username: " + username
                + " JSONObject: " + JSONObject);

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
            //Create Recommendation Engine instance
            re = new RecommendationEngine(cl);
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
            //Create Recommendation Engine instance
            re = new RecommendationEngine(cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(RecommendationEngineRest.class, config.getLogLevel());

        //set security layer on Recommendation Engine
        re.setSecurity(security);

        //Create feed object
        FeedObject fo;

        if (JSONObject == null) {
            LOGGER.info("Failed Feed User: No object");
            output.setCustomOutputMessage("Feed User Failed: No object");

            return JSon.jsonize(output, Output.class);
        } else {
            fo = FeedObject.fromJSON(JSONObject);
        }

        if (re.feed(username, fo)) {
            LOGGER.info("Complete Feed User");
            output.setCustomOutputMessage("Feed User Complete");
        } else {
            LOGGER.info("Failed Feed User");
            output.setCustomOutputMessage("Feed User Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get recommendation, Rank the given object list based on user profile.
     *
     * @param userAuthe The client credentials
     * @param username Th username
     * @param JSONObjectList A object list that we want to recommend on user
     * @return A JSON with process result and ranked object list
     */
    @Path("getRecommendation/{username}")
    @POST
    public String getRecommendation(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("username") String username,
            @FormParam("JSONObjectList") String JSONObjectList
    ) {

        LOGGER.debug("#getRecommendation | userAuthe: " + userAuthe
                + " username: " + username
                + " JSONObjectList: " + JSONObjectList);

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
            //Create Recommendation Engine instance
            re = new RecommendationEngine(cl);
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
            //Create Recommendation Engine instance
            re = new RecommendationEngine(cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(RecommendationEngineRest.class, config.getLogLevel());

        //set security layer on Recommendation Engine
        re.setSecurity(security);

        //Create feed object
        ArrayList<FeedObject> recommendationObjectList = new ArrayList<>();

        //[
        //    {
        //        "id": "1",
        //        "language": "en",
        //        "text": [
        //            "mpla mpla mpla mpla mpla mpla mpla mpla",
        //            "mpla2 mpla2 mpla2 mpla2 mpla2 mpla2 mpla2"
        //        ],
        //        "category": [
        //            "world"
        //        ],
        //        "tag": [
        //            "tag1",
        //            "tag2",
        //            "tag3"
        //        ],
        //        "boolean": [
        //            "boolean1",
        //            "boolean2",
        //            "boolean3"
        //        ],
        //        "numeric": [
        //            "numeric1",
        //            "numeric2",
        //            "numeric3"
        //        ]
        //    },
        //    {
        //        "id": "2",
        //        "language": "en",
        //        "text": [
        //            "mpla mpla mpla mpla mpla mpla mpla mpla",
        //            "mpla2 mpla2 mpla2 mpla2 mpla2 mpla2 mpla2"
        //        ],
        //        "category": [
        //            "world"
        //        ],
        //        "tag": [
        //            "tag1",
        //            "tag2",
        //            "tag3"
        //        ],
        //        "boolean": [
        //            "boolean1",
        //            "boolean2",
        //            "boolean3"
        //        ],
        //        "numeric": [
        //            "numeric1",
        //            "numeric2",
        //            "numeric3"
        //        ]
        //    }
        //]
        if (JSONObjectList == null) {
            LOGGER.info("Failed to get recommendation: No object list");
            output.setCustomOutputMessage("Get recommendation Failed: No object list");

            return JSon.jsonize(output, Output.class);
        } else {
            recommendationObjectList.addAll(FeedObject.fromJSONArray(JSONObjectList));
        }

        LinkedHashMap<String, Double> recommendations = re.getRecommendation(username, recommendationObjectList);
        if (recommendations != null) {
            LOGGER.info("Complete get recommendation");
            output.setOutput(recommendations);
            output.setCustomOutputMessage("Get recommendation Complete");
        } else {
            LOGGER.info("Failed get recommendation");
            output.setCustomOutputMessage("Get recommendation Failed");
        }
        
        LOGGER.debug("#getRecommendation | recommendations: " + recommendations);

        return JSon.jsonize(output, Output.class);
    }

}
