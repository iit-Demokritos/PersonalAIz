/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.api.pserver;

import gr.demokritos.iit.pserver.api.Stereotype;
import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import gr.demokritos.iit.pserver.storage.interfaces.IStereotypeStorage;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.utilities.configuration.PersonalAIzConfiguration;
import gr.demokritos.iit.utilities.json.JSon;
import gr.demokritos.iit.utilities.json.Output;
import gr.demokritos.iit.utilities.logging.Logging;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the Stereotype mode REST API
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
@Path("pserver/{userAuthe}/stereotype/")
@Produces(MediaType.APPLICATION_JSON)
public class StereotypeREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(StereotypeREST.class);
    //TODO: Change HBase with something global to change storage from settings
    private final IStereotypeStorage db = new PServerHBase();
    private Stereotype stereotype;
    private Client cl;
    private final SecurityLayer security = new SecurityLayer();
    private Output output = new Output();
    private final PersonalAIzConfiguration config = new PersonalAIzConfiguration();

    /**
     * Get system attributes based on user attributes
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @return
     */
    @Path("attributes")
    @GET
    public String getSystemAttributes(
            @PathParam("userAuthe") String userAuthe) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        Set<String> attributes = stereotype.getSystemAttributes();

        if (attributes != null) {
            LOGGER.info("Complete Get Sytem Attributes");
            output.setOutput(attributes);
            output.setCustomOutputMessage("Get System Attributes Complete");
        } else {
            LOGGER.info("Failed Get System Attributes");
            output.setCustomOutputMessage("Get System Attributes Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Add a stereotype in PServer
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @param rule The stereotype rule
     * @return
     */
    @Path("{stereotypeName}")
    @POST
    public String addStereotype(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName,
            @FormParam("rule") String rule) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (rule == null) {
            LOGGER.info("Failed to add stereotype: No rule");
            output.setCustomOutputMessage("AddStereotype Failed: No Rule");

            return JSon.jsonize(output, Output.class);
        } else {
            if (stereotype.addStereotype(stereotypeName, rule)) {
                LOGGER.info("Complete Add stereotype: " + stereotypeName);
                output.setCustomOutputMessage("Add Stereotype Complete");
            } else {
                LOGGER.info("Failed Add Stereotype: " + stereotypeName);
                output.setCustomOutputMessage("Add Stereotype Failed");
            }
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Delete Stereotypes from PServer
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param pattern The deleting Stereotype Name pattern
     * @return
     */
    @Path("delete")
    @POST
    public String deleteStereotypes(
            @PathParam("userAuthe") String userAuthe,
            @FormParam("pattern") String pattern) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.deleteStereotypes(pattern)) {
            LOGGER.info("Complete delete stereotypes with pattern: " + pattern);
            output.setCustomOutputMessage("Delete stereotypes Complete");
        } else {
            LOGGER.info("Failed delete stereotypes with pattern: " + pattern);
            output.setCustomOutputMessage("Delete stereotypes Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get list with PServer stereotypes
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param pattern The name pattern. If pattern is null return all the
     * stereotype names.
     * @param page The page number. Return results to single page.
     * @return
     */
    @GET
    public String getStereotypes(
            @PathParam("userAuthe") String userAuthe,
            @FormParam("pattern") String pattern,
            @FormParam("page") String page) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        int pageParam = -1;
        if (page != null) {
            pageParam = Integer.parseInt(page);
        }

        Set<String> users = stereotype.getStereotypes(pattern, pageParam);

        if (users != null) {
            LOGGER.info("Complete Get Stereotypes");
            output.setOutput(users);
            output.setCustomOutputMessage("Get Stereotypes Complete");
        } else {
            LOGGER.info("Failed Get Stereotypes");
            output.setCustomOutputMessage("Get Stereotypes Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Remake Stereotype
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @return
     */
    @Path("{stereotypeName}/remake")
    @POST
    public String remakeStereotype(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.remakeStereotype(stereotypeName)) {
            LOGGER.info("Complete remake stereotype: " + stereotypeName);
            output.setCustomOutputMessage("Remake stereotype Complete");
        } else {
            LOGGER.info("Failed remake stereotype: " + stereotypeName);
            output.setCustomOutputMessage("Remake stereotype Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Update stereotype features based on user profiles who belongs to the
     * stereotype.
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @return
     */
    @Path("{stereotypeName}/features/update")
    @POST
    public String updateStereotypeFeatures(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.updateStereotypeFeatures(stereotypeName)) {
            LOGGER.info("Complete update stereotype features: " + stereotypeName);
            output.setCustomOutputMessage("Update stereotype features Complete");
        } else {
            LOGGER.info("Failed update stereotype features: " + stereotypeName);
            output.setCustomOutputMessage("Update stereotype features Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Update Stereotype users. Add the users who satisfy the stereotype rule
     * and remove the users who not satisfy the rule yet.
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @return
     */
    @Path("{stereotypeName}/users/update")
    @POST
    public String updateStereotypeUsers(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.updateStereotypeUsers(stereotypeName)) {
            LOGGER.info("Complete update stereotype users: " + stereotypeName);
            output.setCustomOutputMessage("Update stereotype users Complete");
        } else {
            LOGGER.info("Failed update stereotype users: " + stereotypeName);
            output.setCustomOutputMessage("Update stereotype users Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Find and add the users who satisfy the Stereotype rule.
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @return
     */
    @Path("{stereotypeName}/users/find")
    @POST
    public String findStereotypeUsers(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.findStereotypeUsers(stereotypeName)) {
            LOGGER.info("Complete find stereotype users: " + stereotypeName);
            output.setCustomOutputMessage("Find stereotype users Complete");
        } else {
            LOGGER.info("Failed find stereotype users: " + stereotypeName);
            output.setCustomOutputMessage("Find stereotype users Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Find and remove the users who not satisfy the Stereotype rule.
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @return
     */
    @Path("{stereotypeName}/users/check")
    @POST
    public String checkStereotypeUsers(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.checkStereotypeUsers(stereotypeName)) {
            LOGGER.info("Complete check stereotype users: " + stereotypeName);
            output.setCustomOutputMessage("Check stereotype users Complete");
        } else {
            LOGGER.info("Failed check stereotype users: " + stereotypeName);
            output.setCustomOutputMessage("Check stereotype users Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Manually Set of stereotype features.
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @param JSONStereotypeFeatures A JSON with feature name - value
     * @return
     */
    @Path("{stereotypeName}/features/set")
    @POST
    public String setStereotypeFeatures(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName,
            @FormParam("JSONStereotypeFeatures") String JSONStereotypeFeatures) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        HashMap<String, String> features = new HashMap<>();
        if (JSONStereotypeFeatures != null) {
            features.putAll(JSon.unjsonize(JSONStereotypeFeatures, HashMap.class));
        } else {
            LOGGER.info("No given features: " + JSONStereotypeFeatures);
            output.setCustomOutputMessage("No features given");
            return JSon.jsonize(output, Output.class);
        }

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.setStereotypeFeatures(stereotypeName, features)) {
            LOGGER.info("Complete set Stereotype Features " + features.toString());
            output.setCustomOutputMessage("Set Stereotype Features Complete");
        } else {
            LOGGER.info("Failed set Stereotype Features " + features.toString());
            output.setCustomOutputMessage("Set Stereotype Features Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Manually modification of stereotype features
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @param JSONStereotypeFeatures A JSON with stereotype feature name - value
     * @return
     */
    @Path("{stereotypeName}/features/modify")
    @POST
    public String modifyStereotypeFeatures(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName,
            @FormParam("JSONStereotypeFeatures") String JSONStereotypeFeatures) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        HashMap<String, String> features = new HashMap<>();
        if (JSONStereotypeFeatures != null) {
            features.putAll(JSon.unjsonize(JSONStereotypeFeatures, HashMap.class));
        } else {
            LOGGER.info("No given features: " + JSONStereotypeFeatures);
            output.setCustomOutputMessage("No features given");
            return JSon.jsonize(output, Output.class);
        }

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.modifyStereotypeFeatures(stereotypeName, features)) {
            LOGGER.info("Complete modify Stereotype Features " + features.toString());
            output.setCustomOutputMessage("modify Stereotype Features Complete");
        } else {
            LOGGER.info("Failed modify Stereotype Features " + features.toString());
            output.setCustomOutputMessage("modify Stereotype Features Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get stereotype profile
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name.
     * @param pattern The feature name pattern. If null return all the features
     * @param page The page number. If is null return results to single page
     * @return
     */
    @Path("{stereotypeName}/features")
    @GET
    public String getStereotypeFeatures(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName,
            @FormParam("pattern") String pattern,
            @FormParam("page") String page) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        int pageParam = -1;
        if (page != null) {
            pageParam = Integer.parseInt(page);
        }

        Map<String, String> features = stereotype.getStereotypeFeatures(
                stereotypeName, pattern, pageParam);

        if (features != null) {
            LOGGER.info("Complete Get Stereotype Features");
            output.setOutput(features);
            output.setCustomOutputMessage("Get Stereotype Features Complete");
        } else {
            LOGGER.info("Failed Get Stereotype Features");
            output.setCustomOutputMessage("Get Stereotype Features Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Delete stereotype features
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @param pattern The feature name pattern to delete. If is null delete all
     * features.
     * @return
     */
    @Path("{stereotypeName}/features/delete")
    @POST
    public String deleteStereotypeFeatures(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName,
            @FormParam("pattern") String pattern) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.deleteStereotypeFeatures(stereotypeName, pattern)) {
            LOGGER.info("Complete Delete stereotype " + stereotypeName
                    + " features with pattern " + pattern);
            output.setCustomOutputMessage("Delete stereotype features Complete");
        } else {
            LOGGER.info("Failed Delete stereotype " + stereotypeName
                    + " features with pattern " + pattern);
            output.setCustomOutputMessage("Delete stereotype features Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get The users who belongs to stereotype
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @param pattern The username pattern. If is null return all the users.
     * @param page The page number. If is null return results to single page.
     * @return
     */
    @Path("{stereotypeName}/users")
    @GET
    public String getStereotypeUsers(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName,
            @FormParam("pattern") String pattern,
            @FormParam("page") String page) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        int pageParam = -1;
        if (page != null) {
            pageParam = Integer.parseInt(page);
        }

        List<String> users = stereotype.getStereotypeUsers(stereotypeName,
                pattern, pageParam);

        if (users != null) {
            LOGGER.info("Complete Get Stereotype Users");
            output.setOutput(users);
            output.setCustomOutputMessage("Get Stereotype Users Complete");
        } else {
            LOGGER.info("Failed Get Stereotype Users");
            output.setCustomOutputMessage("Get Stereotype Users Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get the stereotypes which a user belongs
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param usernameName The username
     * @param pattern The stereotype name pattern. If in null return all
     * stereotypes
     * @param page The page number. If is null return results to single page
     * @return
     */
    @Path("user/{usernameName}/stereotypes")
    @GET
    public String getUserStereotypes(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("usernameName") String usernameName,
            @FormParam("pattern") String pattern,
            @FormParam("page") String page) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        int pageParam = -1;
        if (page != null) {
            pageParam = Integer.parseInt(page);
        }

        List<String> stereotypes = stereotype.getUserStereotypes(usernameName,
                pattern, pageParam);

        if (stereotypes != null) {
            LOGGER.info("Complete Get User Stereotypes");
            output.setOutput(stereotypes);
            output.setCustomOutputMessage("Get User Stereotypes Complete");
        } else {
            LOGGER.info("Failed Get User Stereotypes");
            output.setCustomOutputMessage("Get User Stereotypes Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Add user on Stereotype manually
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @param username The username
     * @return
     */
    @Path("{stereotypeName}/user/add/{username}")
    @POST
    public String addUserOnStereotype(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName,
            @PathParam("username") String username) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.addUserOnStereotype(username, stereotypeName)) {
            LOGGER.info("Complete Add user " + username + " on stereotype " + stereotypeName);
            output.setCustomOutputMessage("Add user on stereotype Complete");
        } else {
            LOGGER.info("Failed Add user " + username + " on stereotype " + stereotypeName);
            output.setCustomOutputMessage("Add user on stereotype Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Delete user from stereotype manually
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param stereotypeName The stereotype name
     * @param username The username
     * @return
     */
    @Path("{stereotypeName}/user/delete/{username}")
    @POST
    public String deleteUserFromStereotype(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("stereotypeName") String stereotypeName,
            @PathParam("username") String username) {

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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
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
            //Create PServer Stereotype instance
            stereotype = new Stereotype(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(StereotypeREST.class, config.getLogLevel());

        //set security layer on PServer Stereotype
        stereotype.setSecurity(security);

        if (stereotype.deleteUserFromStereotype(username, stereotypeName)) {
            LOGGER.info("Complete Delete user " + username + " from stereotype " + stereotypeName);
            output.setCustomOutputMessage("Delete user from stereotype Complete");
        } else {
            LOGGER.info("Failed Delete user " + username + " from stereotype " + stereotypeName);
            output.setCustomOutputMessage("Delete user from stereotype Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

}
