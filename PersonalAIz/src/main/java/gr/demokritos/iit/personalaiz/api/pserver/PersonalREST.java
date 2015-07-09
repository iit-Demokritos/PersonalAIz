/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the Personal mode API
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
/**
 * Root resource (exposed at "pserver/:credentials/personal" path)
 */
@Path("{userKey}/personal")
@Produces(MediaType.APPLICATION_JSON)
public class PersonalREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalREST.class);
    private final IPersonalStorage db = new PServerHBase();
    private Personal personal;
    private final SecurityLayer security = new SecurityLayer();
    private Output output;
    private final PersonalAIzConfiguration config = new PersonalAIzConfiguration();

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @param userKey
     * @param JSONUsers
     * @return String that will be returned as a text/plain response.
     */
    //POST users/:JSONUsers | Add new users
    @Path("users/{JSONUsers}")
    @POST
    public String addUsers(
            @PathParam("userKey") String userKey,
            @PathParam("JSONUsers") String JSONUsers
    ) {

        // Check the api key Credentials
        if (!security.authe.checkCredentials(userKey)) {
            LOGGER.info("No valid API Key: " + userKey);
            output.setCustomOutputMessage("Security Authentication Failed");
            return JSon.jsonize(output, Output.class);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //Create PServer Personal instance
        personal = new Personal(db, new Client(userKey));

        //set security layer on PServer Personal
        personal.setSecurity(security);

        //convert JSONUsers to users objects
        ArrayList<User> usersList = new ArrayList<User>();

        //convert JSON with users as a HashMap
        HashMap<String, Object> users = new HashMap<String, Object>(
                JSon.unjsonize(JSONUsers, HashMap.class));

        //for each username create User object and add it on the list
        for (String cUser : users.keySet()) {

            //Create new user object
            User user = new User(cUser);

            HashMap<String, HashMap<String, String>> userMap
                    = new HashMap<String, HashMap<String, String>>();
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

        return JSon.jsonize(output, Output.class);
    }

    /**
     *
     * @param userKey
     * @param pattern
     * @return
     */
    //DELETE users | Delete users
    @Path("users")
    @DELETE
    public String deleteUsers(
            @PathParam("userKey") String userKey,
            @QueryParam("pattern") String pattern
    ) {

        // Check the api key Credentials
        if (!security.authe.checkCredentials(userKey)) {
            LOGGER.info("No valid API Key: " + userKey);
            output.setCustomOutputMessage("Security Authentication Failed");
            return JSon.jsonize(output, Output.class);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //Create PServer Personal instance
        personal = new Personal(db, new Client(userKey));

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
     *
     * @param userKey
     * @param pattern
     * @param page
     * @return
     */
    //GET users | Get list with users
    @Path("users")
    @GET
    public String getUsers(
            @PathParam("userKey") String userKey,
            @QueryParam("pattern") String pattern,
            @QueryParam("page") String page
    ) {

        // Check the api key Credentials
        if (!security.authe.checkCredentials(userKey)) {
            LOGGER.info("No valid API Key: " + userKey);
            output.setCustomOutputMessage("Security Authentication Failed");
            return JSon.jsonize(output, Output.class);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //Create PServer Personal instance
        personal = new Personal(db, new Client(userKey));

        //set security layer on PServer Personal
        personal.setSecurity(security);

        HashSet<String> users = new HashSet<String>(
                personal.getUsers(pattern, Integer.parseInt(page)));

        if (users.isEmpty()) {
            LOGGER.info("Security Authorization Failed");
            output.setCustomOutputMessage("Security Authorization Failed");
        } else {
            LOGGER.info("Complete Get Users");
            output.setOutput(users);
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     *
     * @param userKey
     * @param user
     * @param JSONUserAttributes {"gender":"male","age": "18"}
     * @return
     */
    //PUT users/:User/attributes/:JSONUserAttributes | Set user’s attributes
    @Path("users/{User}/attributes/{JSONUserAttributes}")
    @PUT
    public String setUserAttributes(
            @PathParam("userKey") String userKey,
            @PathParam("user") String user,
            @PathParam("JSONUserAttributes") String JSONUserAttributes
    ) {

        // Check the api key Credentials
        if (!security.authe.checkCredentials(userKey)) {
            LOGGER.info("No valid API Key: " + userKey);
            output.setCustomOutputMessage("Security Authentication Failed");
            return JSon.jsonize(output, Output.class);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //Create PServer Personal instance
        personal = new Personal(db, new Client(userKey));

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
     *
     * @param userKey
     * @param user
     * @param JSONUserFeatures
     * @return
     */
    //PUT users/:User/features/:JSONUserFeatures | Set user’s features
    @Path("users/{User}/features/{JSONUserFeatures}")
    @PUT
    public String setUserFeatures(
            @PathParam("userKey") String userKey,
            @PathParam("user") String user,
            @PathParam("JSONUserFeatures") String JSONUserFeatures
    ) {

        // Check the api key Credentials
        if (!security.authe.checkCredentials(userKey)) {
            LOGGER.info("No valid API Key: " + userKey);
            output.setCustomOutputMessage("Security Authentication Failed");
            return JSon.jsonize(output, Output.class);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //Create PServer Personal instance
        personal = new Personal(db, new Client(userKey));

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
     *
     * @param userKey
     * @param user
     * @param JSONUserFeatures
     * @return
     */
    //PUT users/:User/features/modify/:JSONUserFeatures | Modify (increase/decrease) user’s feature
    @Path("users/{User}/features/modify/{JSONUserFeatures}")
    @PUT
    public String modifyUserFeatures(
            @PathParam("userKey") String userKey,
            @PathParam("user") String user,
            @PathParam("JSONUserFeatures") String JSONUserFeatures
    ) {

        // Check the api key Credentials
        if (!security.authe.checkCredentials(userKey)) {
            LOGGER.info("No valid API Key: " + userKey);
            output.setCustomOutputMessage("Security Authentication Failed");
            return JSon.jsonize(output, Output.class);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //Create PServer Personal instance
        personal = new Personal(db, new Client(userKey));

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

        return JSon.jsonize(output, Output.class);
    }

    /**
     *
     * @param userKey
     * @param user
     * @param pattern
     * @param page
     * @return
     */
    //GET users/:User/features | Get user’s profile
    @Path("users/{User}/features")
    @GET
    public String getUserProfile(
            @PathParam("userKey") String userKey,
            @PathParam("user") String user,
            @QueryParam("pattern") String pattern,
            @QueryParam("page") String page
    ) {

        // Check the api key Credentials
        if (!security.authe.checkCredentials(userKey)) {
            LOGGER.info("No valid API Key: " + userKey);
            output.setCustomOutputMessage("Security Authentication Failed");
            return JSon.jsonize(output, Output.class);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //Create PServer Personal instance
        personal = new Personal(db, new Client(userKey));

        //set security layer on PServer Personal
        personal.setSecurity(security);

        HashMap<String, String> userProfile = new HashMap<String, String>(
                personal.getUserFeatures(user, pattern, Integer.parseInt(page)));

        if (userProfile.isEmpty()) {
            LOGGER.info("Security Authorization Failed");
            output.setCustomOutputMessage("Security Authorization Failed");
        } else {
            LOGGER.info("Complete Get User Profile");
            output.setCustomOutputMessage("Complete Get User Profile");
            output.setOutput(userProfile);
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     *
     * @param userKey
     * @param user
     * @param pattern
     * @param page
     * @return
     */
    //GET users/:User/attributes | Get user’s attributes
    @Path("users/{User}/attributes")
    @GET
    public String getUserAttributes(
            @PathParam("userKey") String userKey,
            @PathParam("user") String user,
            @QueryParam("pattern") String pattern,
            @QueryParam("page") String page
    ) {

        // Check the api key Credentials
        if (!security.authe.checkCredentials(userKey)) {
            LOGGER.info("No valid API Key: " + userKey);
            output.setCustomOutputMessage("Security Authentication Failed");
            return JSon.jsonize(output, Output.class);
        }

        //Update logging level 
        Logging.updateLoggerLevel(PersonalREST.class, config.getLogLevel());

        //Create PServer Personal instance
        personal = new Personal(db, new Client(userKey));

        //set security layer on PServer Personal
        personal.setSecurity(security);

        HashMap<String, String> userAttributes = new HashMap<String, String>(
                personal.getUserAttributes(user, pattern, Integer.parseInt(page)));

        if (userAttributes.isEmpty()) {
            LOGGER.info("Security Authorization Failed");
            output.setCustomOutputMessage("Security Authorization Failed");
        } else {
            LOGGER.info("Complete Get User Attributes");
            output.setCustomOutputMessage("Complete Get User Attributes");
            output.setOutput(userAttributes);
        }

        return JSon.jsonize(output, Output.class);
    }

}
