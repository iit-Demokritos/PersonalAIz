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
 * Implements the REST API for the PServer Admin mode.
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
@Path("pserver/{userAuthe}/admin/")
@Produces(MediaType.APPLICATION_JSON)
public class AdminREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminREST.class);
    //TODO: Change HBase with something global to change storage from settings
    private final IAdminStorage db = new PServerHBase();
    private Admin admin;
    private Client cl;
    private final SecurityLayer security = new SecurityLayer();
    private Output output = new Output();
    private final PersonalAIzConfiguration config = new PersonalAIzConfiguration();

    /**
     * Initialize the platform if no one client in the platform
     *
     * @param clientPass The client pass of the root user
     * @return
     */
    @Path("initialization/{password}")
    @GET
    public String initPlatform(@PathParam("password") String clientPass) {

        //Update logging level 
        Logging.updateLoggerLevel(AdminREST.class, config.getLogLevel());

        // Create PServer Admin instance
        admin = new Admin(db, cl);
        //set security layer on PServer Admin
        admin.setSecurity(security);

        if (admin.initPlatform(clientPass)) {
            LOGGER.info("Complete Platform initialization");
            output.setCustomOutputMessage("Complete Platform initialization with username root");
        } else {
            LOGGER.info("Failed Platform initialization");
            output.setCustomOutputMessage("Platform initialization Failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Add a new client in the platform
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
     * Delete a client from the Platform
     *
     * @param userAuthe The user API Key or Username - pass credentials
     * @param clientName The client username that i want to delete
     * @return
     */
    @Path("client/delete/{clientName}")
    @POST
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
     * Get the Platform clients
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

        Set<String> clients = admin.getClients();
        if (clients == null) {
            LOGGER.info("Security Authorization Failed");
            output.setCustomOutputMessage("Security Authorization Failed");
        } else {
            LOGGER.info("Complete Get Clients");
            output.setOutput(clients);
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Set Platform settings
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
     * Set Platform settings
     *
     * @param userAuthe The user API Key or Username - pass credentials
     * @param settingName
     * @param settingValue
     * @return
     */
    @Path("setting")
    @POST
    public String setSetting(
            @PathParam("userAuthe") String userAuthe,
            @FormParam("name") String settingName,
            @FormParam("value") String settingValue
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

        if (settingName == null || settingValue == null) {
            LOGGER.info("Empty variable settings");
            output.setCustomOutputMessage("Set PServer settings failed. Empty settings");
            return JSon.jsonize(output, Output.class);
        }

        if (admin.setSetting(settingName, settingValue)) {
            LOGGER.info("Complete Set PServer settings: " + settingName + " " + settingValue);
            output.setCustomOutputMessage("Set PServer setting complete");
        } else {
            LOGGER.info("Failed Set PServer settings: " + settingName + " " + settingValue);
            output.setCustomOutputMessage("Set PServer setting failed");
        }

        return JSon.jsonize(output, Output.class);
    }

    /**
     * Get Platform Settings map
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
