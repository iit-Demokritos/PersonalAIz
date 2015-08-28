/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.api.pserver;

import gr.demokritos.iit.pserver.api.Admin;
import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import gr.demokritos.iit.pserver.storage.interfaces.IAdminStorage;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.utilities.configuration.PersonalAIzConfiguration;
import gr.demokritos.iit.utilities.json.JSon;
import gr.demokritos.iit.utilities.json.Output;
import gr.demokritos.iit.utilities.logging.Logging;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import javax.ws.rs.DELETE;
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
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
@Path("pserver/{userAuthe}/admin/")
@Produces(MediaType.APPLICATION_JSON)
public class AdminREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminREST.class);
    private final IAdminStorage db = new PServerHBase();
    private Admin admin;
    private Client cl;
    private final SecurityLayer security = new SecurityLayer();
    private Output output=new Output();
    private final PersonalAIzConfiguration config = new PersonalAIzConfiguration();

    /**
     *
     * @param userAuthe System user API Key or Username - pass credentials
     * @param clientName Client Username
     * @param clientPass Client Password
     * @param clientInfo {"mail":"info@mail.org","infoname":"infovalue"}
     * @return
     */
    @Path("client/{clientName}")
    @POST
    public String addClient(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("clientName") String clientName,
            @FormParam("password") String clientPass,
            @FormParam("info") String clientInfo
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(AdminREST.class, config.getLogLevel());

        //set security layer on PServer Admin
        admin.setSecurity(security);

        HashMap<String, String> info = new HashMap<String, String>();
        if (clientInfo != null) {
            info.putAll(JSon.unjsonize(clientInfo, HashMap.class));
        }

        if (admin.addClient(clientName, clientPass, info)) {
            LOGGER.info("Complete Add Client: " + clientName);
            output.setCustomOutputMessage("Add Client Complete");
        } else {
            LOGGER.info("Failed Add Client: " + clientName);
            output.setCustomOutputMessage("Add Client Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Delete a client from PServer
     *
     * @param userAuthe The user API Key or Username - pass credentials
     * @param clientName The client username that i want to delete
     * @return
     */
    @Path("client/{clientName}")
    @DELETE
    public String deleteClient(
            @PathParam("userAuthe") String userAuthe,
            @PathParam("clientName") String clientName
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(AdminREST.class, config.getLogLevel());

        //set security layer on PServer Admin
        admin.setSecurity(security);

        if (admin.deleteClient(clientName)) {
            LOGGER.info("Complete Delete client: " + clientName);
            output.setCustomOutputMessage("Delete Client Complete");
        } else {
            LOGGER.info("Failed Delete client: " + clientName);
            output.setCustomOutputMessage("Delete Client Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get the PServer clients
     *
     * @param userAuthe The user API Key or Username - pass credentials
     * @return
     */
    @Path("clients")
    @GET
    public String getClients(
            @PathParam("userAuthe") String userAuthe
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(AdminREST.class, config.getLogLevel());

        //set security layer on PServer Admin
        admin.setSecurity(security);

        HashSet<String> clients = new HashSet<>(admin.getClients());
        if (clients.isEmpty()) {
            LOGGER.info("Security Authorization Failed");
            output.setCustomOutputMessage("Security Authorization Failed");
        } else {
            LOGGER.info("Complete Get Clients");
            output.setOutput(clients);
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Set PServer settings
     *
     * @param userAuthe The user API Key or Username - pass credentials
     * @param JSONSettings A JSON with key - value pairs with the settings. e.g.
     * {"SettingName":"SettingValue"}
     * @return
     */
    @Path("settings")
    @POST
    public String setSettings(
            @PathParam("userAuthe") String userAuthe,
            @FormParam("settings") String JSONSettings
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(AdminREST.class, config.getLogLevel());

        //set security layer on PServer Admin
        admin.setSecurity(security);

        if (JSONSettings == null) {
            LOGGER.info("Empty variable settings");
            output.setCustomOutputMessage("Set PServer settings failed. Empty settings");
            return JSon.jsonize(output, Output.class);
        }

        HashMap<String, String> settings = new HashMap<String, String>(
                JSon.unjsonize(JSONSettings, HashMap.class));

        if (admin.setSettings(settings)) {
            LOGGER.info("Complete Set PServer settings: " + settings.toString());
            output.setCustomOutputMessage("Set PServer settings complete");
        } else {
            LOGGER.info("Failed Set PServer settings: " + settings.toString());
            output.setCustomOutputMessage("Set PServer settings failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get PServer Settings map
     *
     * @param userAuthe The user API Key or Username - pass credentials
     * @return
     */
    @Path("settings")
    @GET
    public String getSettings(
            @PathParam("userAuthe") String userAuthe
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
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
            // Create PServer Admin instance
            admin = new Admin(db, cl);
        }

        //Update logging level 
        Logging.updateLoggerLevel(AdminREST.class, config.getLogLevel());

        //set security layer on PServer Admin
        admin.setSecurity(security);
        HashMap<String, String> settings;
        try {
            settings = new HashMap<String, String>(admin.getSettings());
            LOGGER.info("Complete Get PServer Settings");
            output.setCustomOutputMessage("Get PServer settings complete");
        } catch (Exception e) {
            LOGGER.info("Failed Get PServer Settings");
            output.setCustomOutputMessage("Get PServer settings failed");
            return JSon.jsonize(output, Output.class);
        }

        output.setOutput(settings);

        return JSon.jsonize(output, Output.class);
    }

}
