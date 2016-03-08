/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.storage.interfaces.IPersonalStorage;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.authorization.Actions;
import gr.demokritos.iit.utilities.configuration.PServerConfiguration;
import gr.demokritos.iit.utilities.logging.Logging;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final Client psClient;
    public static final Logger LOGGER = LoggerFactory.getLogger(Personal.class);
    private SecurityLayer security = new SecurityLayer();
    private final HashMap<String, Action> actions = new HashMap<>(new Actions().getPersonalActions());

    /**
     * The constructor of personal mode.
     *
     * @param dbPersonal
     * @param psClient
     */
    public Personal(IPersonalStorage dbPersonal, Client psClient) {
        this.psConfig = new PServerConfiguration();
        this.dbPersonal = dbPersonal;
        this.psClient = psClient;

        //Update logging level 
        Logging.updateLoggerLevel(Personal.class, psConfig.getLogLevel());
    }

    /**
     * The constructor of personal mode.
     *
     * @param dbPersonal
     * @param psClient
     * @param configurationFileName
     */
    public Personal(IPersonalStorage dbPersonal, Client psClient, String configurationFileName) {
        this.psConfig = new PServerConfiguration(configurationFileName);
        this.dbPersonal = dbPersonal;
        this.psClient = psClient;

        //Update logging level 
        Logging.updateLoggerLevel(Personal.class, psConfig.getLogLevel());
    }

    /**
     * Set security control for user authorization
     *
     * @param security
     */
    public void setSecurity(SecurityLayer security) {
        this.security = security;
    }

    /**
     * Add a PServer User on Storage.
     *
     * @param user A PServer User
     * @return the status of this action
     */
    public boolean addUser(User user) {

        //Check permission
        if (!getPermissionFor(actions.get("aAddUser"), "W")) {
            LOGGER.error("Premission Denied");
            return false;
        }

        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(user);

        //update Authenticated time
        psClient.updateAuthenticatedTimestamp();

        // call storage AddUsers to add the users on PServer 
        return dbPersonal.addUsers(usersList, psClient.username);
    }

    /**
     * Add a list of PServer Users on Storage. This function help us to add
     * massive, users on PServer.
     *
     * @param usersList A list of PServer Users
     * @return the status of this action
     */
    public boolean addUsers(List<User> usersList) {

        //Check permission
        if (!getPermissionFor(actions.get("aAddUsers"), "W")) {
            LOGGER.error("Premission Denied");
            return false;
        }

        //update Authenticated time
        psClient.updateAuthenticatedTimestamp();

        //Call storage AddUsers to add the users on PServer
        return dbPersonal.addUsers(usersList, psClient.username);
    }

    /**
     * Delete users from PServer storage basic on given pattern. If pattern is
     * null then the function will delete all users for this client.
     *
     * @param pattern The user pattern that we want to delete. If pattern is
     * null then delete all user.
     * @return A boolean status true/false if delete complete or not
     */
    public boolean deleteUsers(String pattern) {

        //Check permission
        if (!getPermissionFor(actions.get("aDeleteUsers"), "X")) {
            LOGGER.error("Premission Denied");
            return false;
        }

        //update Authenticated time
        psClient.updateAuthenticatedTimestamp();

        //call storage delete Users function with the pattern and add the return 
        return dbPersonal.deleteUsers(pattern, psClient.username);
    }

    /**
     * Get a set with users in PServer basic on the given pattern.
     *
     * @param pattern The username pattern that we want in the user list. If the
     * pattern is null then the returned a list which contain the whole users.
     * @param page The page number. Page number will be greater or equal than 1
     * (page>=1). The list returned as page with 20 elements. With page
     * parameter you can ask for the first page, the second page... If page is
     * null or page<1 then return all elements in a single page. @return A set
     * with the usernames. If return is null then permission denied
     * @return 
     */
    public Set<String> getUsers(String pattern, Integer page) {

        //Check permission
        if (!getPermissionFor(actions.get("aGetUsers"), "R")) {
            LOGGER.error("Premission Denied");
            return null;
        }

        //Check if page is null or page <1
        if (page == null || page < 1) {
            //set page null to return single page
            page = null;
        }

        //update Authenticated time
        psClient.updateAuthenticatedTimestamp();

        return dbPersonal.getUsers(pattern, page, psClient.username).keySet();
    }

    /**
     * Set the attributes for the given PServer User. If username not exists
     * then will be added on PServer.
     *
     * @param username The username of the user that we want to set the
     * attributes
     * @param attributes A map with pairs of attribute name - attribute value
     * @return A boolean status true/false if setUserAttributes complete or not
     */
    public boolean setUserAttributes(String username,
            Map<String, String> attributes) {

        //Check permission
        if (!getPermissionFor(actions.get("aSetUserAttributes"), "W")) {
            LOGGER.error("Premission Denied");
            return false;
        }

        // Create new User object
        User user = new User(username);

        //set attributes on user
        user.setAttributes(attributes);

        //update Authenticated time
        psClient.updateAuthenticatedTimestamp();

        // call storage setUserAttributes to set the user Attributes on PServer
        return dbPersonal.setUserAttributes(user, psClient.username);
    }

    /**
     * Get a map with the user attributes.
     *
     * @param user The username that we want the attribute list.
     * @param pattern The attribute pattern that we want in the attribute list.
     * If the pattern is null then the returned a list which contain the whole
     * user's attributes.
     * @param page The page number. Page number will be greater or equal than 1
     * (page>=1). The list returned as page with 20 elements. With page
     * parameter you can ask for the first page, the second page... If page is
     * null then return all elements in a single page.
     * @return A map with user attribute name - value pairs. If return is null
     * then permission denied
     */
    public Map<String, String> getUserAttributes(String user,
            String pattern, Integer page) {

        //Check permission
        if (!getPermissionFor(actions.get("aGetUserAttributes"), "R")) {
            LOGGER.error("Premission Denied");
            return null;
        }

        //Check if page is null or page <1
        if (page == null || page < 1) {
            //set page null to return single page
            page = null;
        }

        //update Authenticated time
        psClient.updateAuthenticatedTimestamp();

        //Call storage to get User attributes
        return dbPersonal.getUserAttributes(user, pattern, page, psClient.username);
    }

    /**
     * Set the features for the given PServer User. If username not exists then
     * will be added on PServer.
     *
     * @param username The username
     * @param features A map with user feature name - value
     * @return A boolean status true/false if setUserFeatures complete or not
     */
    public boolean setUserFeatures(String username,
            Map<String, String> features) {

        //Check permission
        if (!getPermissionFor(actions.get("aSetUserFeatures"), "W")) {
            LOGGER.error("Premission Denied");
            return false;
        }

        //Create user object
        User user = new User(username);

        //set features on user
        user.setFeatures(features);

        //update Authenticated time
        psClient.updateAuthenticatedTimestamp();

        // call storage setUsersFeatures to set the users Features on PServer
        return dbPersonal.setUserFeatures(user, psClient.username);
    }

    /**
     * Modify the features for the given PServer User. If username not exists
     * then will be added on PServer.
     *
     * @param username The username
     * @param features A map with user feature name - value (to modify)
     * @return A boolean status true/false if setUserFeatures complete or not
     */
    public boolean modifyUserFeatures(String username,
            Map<String, String> features) {

        //Check permission
        if (!getPermissionFor(actions.get("aModifyUserFeatures"), "W")) {
            LOGGER.error("Premission Denied");
            return false;
        }

        User user = new User(username);

        //set features on user
        user.setFeatures(features);

        //update Authenticated time
        psClient.updateAuthenticatedTimestamp();

        //Call storage modifyUserFeatures to set the users Features on PServer
        return dbPersonal.modifyUserFeatures(user, psClient.username);
    }

    /**
     * Get the user profile. User profile is a Map with user's features and
     * their values.
     *
     * @param user The username that we want to get the profile
     * @param pattern The feature pattern that we want in the profile. If the
     * pattern is null then the returned a list which contain the whole profile.
     * @param page The page number. Page number will be greater or equal than 1
     * (page>=1). The list returned as page with 20 elements. With page
     * parameter you can ask for the first page, the second page... If page is
     * null then return all elements in a single page.
     * @return A map of key-value pairs for user's features. If return is null
     * then permission denied
     */
    public Map<String, String> getUserFeatures(String user,
            String pattern, Integer page) {

        //Check permission
        if (!getPermissionFor(actions.get("aGetUserFeatures"), "R")) {
            LOGGER.error("Premission Denied");
            return null;
        }

        //Check if page is null or page <1
        if (page == null || page < 1) {
            //set page null to return single page
            page = null;
        }
        //update Authenticated time
        psClient.updateAuthenticatedTimestamp();

        //Call storage to get User features
        return dbPersonal.getUserFeatures(user, pattern, page, psClient.username);
    }

    /**
     * Get the permission for the given action and client
     *
     * @param a The action that we want to check the permission
     * @param sAccessType The access type R (read) - W (write) - X (execute)
     * @return A true or false if the permission granted
     */
    public boolean getPermissionFor(Action a, String sAccessType) {
        return ((security != null)
                && (security.autho.getAccessRights(psClient, a).get(sAccessType))
                && (psClient.authenticatedTimestamp != 0));
    }
}
