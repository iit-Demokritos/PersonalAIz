/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.storage.interfaces.IAdminStorage;
import gr.demokritos.iit.security.Security;
import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.authorization.Actions;
import gr.demokritos.iit.utilities.configuration.PServerConfiguration;
import gr.demokritos.iit.utilities.logging.Logging;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implement the administration functionality of PServer.
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class Admin {

    private final IAdminStorage dbAdmin;
    public static final Logger LOGGER = LoggerFactory.getLogger(Admin.class);
    private final PServerConfiguration psConfig;
    private final Client adminClient;
    public Security security;
    private final HashMap<String,Action> actions = new HashMap<>
        (new Actions().getAdminActions());

    public Admin(IAdminStorage dbAdmin, Client adminClient) {
        this.psConfig = new PServerConfiguration();
        this.dbAdmin = dbAdmin;
        this.adminClient = adminClient;
        security = null;

        //Update logging level 
        Logging.updateLoggerLevel(Admin.class, psConfig.getLogLevel());
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    /**
     * Add a PServer client in storage
     *
     * @param clientName The client Username
     * @param password The client password
     * @param info A map with client information. Key - value pairs (e.g.mail:info@mail.com)
     * @return A boolean status true/false if add complete or not
     */
    public boolean addClient(String clientName, String password, HashMap<String, String> info) {
        
        //Check permission
        if (!getPermissionFor(actions.get("aAddClient"), "W")) {
            //TODO:  throw exeption   
            return false;
        }
        
        //encrypt password
        String clientPassword = DigestUtils.sha1Hex(password);

        Client client = new Client(clientName, password);
        info.put("Username", clientName);
        info.put("Password", clientPassword);

        //Add info on client object 
        client.setInfo(info);

        
        // call add client from db and return the result status
        return dbAdmin.addClient(client);
    }

    /**
     * Delete client with given username
     *
     * @param clientName The client name that we want to delete 
     * @return A boolean status true/false if delete complete or not
     */
    public boolean deleteClient(String clientName) {
        
        //Check permission
        if (!getPermissionFor(actions.get("aDeleteClient"), "X")) {
            //TODO:  throw exeption   
            return false;
        }

        // call delete client from db and return the result status
        return dbAdmin.deleteClient(clientName);
    }

    /**
     * Get all client names
     *
     * @return Get A Set with all client names. If return is null then permission denied  
     */
    public Set<String> getClients() {
        
        //Check permission
        if (!getPermissionFor(actions.get("aGetClients"), "R")) {
            //TODO:  throw exeption   
            return null;
        }

        //Call HBase to get Clients and return the client set
        return dbAdmin.getClients().keySet();
    }

    /**
     *
     * @return A boolean status true/false if delete complete or not
     */
    public boolean setClientRoles() {
        
        //Check permission
        if (!getPermissionFor(actions.get("aSetClientRoles"), "W")) {
            //TODO:  throw exeption   
            return false;
        }
        //TODO: implement

        //call set client roles and return the status
        return dbAdmin.setClientRoles(null, null);
    }

    /**
     * Get PServer settings
     * 
     * @return Return a map with PServer settings. If return is null then permission denied
     */
    public Map<String, String> getSettings() {
        
        //Check permission
        if (!getPermissionFor(actions.get("aGetSettings"), "R")) {
            //TODO:  throw exeption   
            return null;
        }

        // Call get settings function and return the settings map
        return dbAdmin.getSettings();
    }

    /**
     * Set PServer settings
     * @param settings A map with settings to update
     * @return the status of this action
     */
    public boolean setSettings(Map<String, String> settings) {
        
        //Check permission
        if (!getPermissionFor(actions.get("aSetSettings"), "W")) {
            //TODO:  throw exeption   
            return false;
        }

        // Call set settings function and return the status 
        return dbAdmin.setSettings(settings);
    }

    
    
    /**
     * Get the permission for the given action and client
     *
     * @param a The action that we want to check the permission
     * @param sAccessType The access type R (read) - W (write) - X (execute)
     * @return A true or false if the permission granted
     */
    public boolean getPermissionFor(Action a, String sAccessType) {
        //If security is not null and access granted then return true
        return ((security != null) && (security.getAccessRights(adminClient, a)
                .get(sAccessType)));
    }

}
