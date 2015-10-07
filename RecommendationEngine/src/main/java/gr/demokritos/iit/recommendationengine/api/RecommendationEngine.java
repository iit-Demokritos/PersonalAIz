/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.api;

import gr.demokritos.iit.pserver.api.Personal;
import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import gr.demokritos.iit.pserver.storage.interfaces.IPersonalStorage;
import gr.demokritos.iit.recommendationengine.converters.category.CategoryConverter;
import gr.demokritos.iit.recommendationengine.converters.tag.TagConverter;
import gr.demokritos.iit.recommendationengine.converters.text.TextConverter;
import gr.demokritos.iit.recommendationengine.onologies.FeedObject;
import gr.demokritos.iit.recommendationengine.onologies.RecommendationObject;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.authorization.Actions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class RecommendationEngine {

    private final Client psClient;
    public static final Logger LOGGER = LoggerFactory.getLogger(RecommendationEngine.class);
    private SecurityLayer security = new SecurityLayer();
    private Personal personal;
    private final IPersonalStorage db;
    private final HashMap<String, Action> actions = new HashMap<>(new Actions().getRecommendationActions());

    public RecommendationEngine(Client psClient) {

        this.psClient = psClient;
        //TODO: change hardcoded HBsase storage with generic storage
        this.db = new PServerHBase();
        //Create PServer2 Personal mode Instance
        this.personal = new Personal(db, psClient);

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
     * Add new application user on the platform
     *
     * @param username The username
     * @param attributes A map of user's attributes. Add null if there is no
     * attributes
     * @param info A map of user's information. Add null if there is no info
     * @return The status of this action
     */
    public boolean addUser(String username,
            Map<String, String> attributes,
            Map<String, String> info) {
        //Check permission
        if (!getPermissionFor(actions.get("aAddUser"), "W")) {
            LOGGER.error("Premission Denied");
            return false;
        }

        //Create the new user
        User user = new User(username);

        //If user information is not null
        if (info != null) {
            user.setInfo(info);
        }
        //If user attributes is not null
        if (attributes != null) {
            user.setAttributes(attributes);
        }

        //Call add user function on PServer
        return personal.addUser(user);
    }

    /**
     * Delete application user
     *
     * @param username The username
     * @return The status of this action
     */
    public boolean deleteUser(String username) {
        //Check permission
        if (!getPermissionFor(actions.get("aDeleteUser"), "X")) {
            LOGGER.error("Premission Denied");
            return false;
        }

        //Call deleteUser to remove user from PServer
        return personal.deleteUsers(username);
    }

    /**
     * Feed User's profile with given object 
     * @param username The username
     * @param object The feed object
     * @return The action Status
     */
    public boolean feed(String username, FeedObject object) {

        //Check permission
        if (!getPermissionFor(actions.get("aFeed"), "W")) {
            LOGGER.error("Premission Denied");
            return false;
        }

        //TODO: Call evaluation method to store user's actions
        //Convert object to features  and call setUserFeatures 
        //to update user profile
        return personal.setUserFeatures(username,
                mapValueIntegerToString(objectHandler(object)));
    }

    /**
     * Rank a FeedObject List based on user's profile
     * @param username The username
     * @param recommendationList The FeedObject List
     * @return A shorted map with object id and score
     */
    public Map<String, Integer> getRecommendation(String username,
            List<FeedObject> recommendationList) {
        //Check permission
        if (!getPermissionFor(actions.get("aGetRecommendations"), "R")) {
            LOGGER.error("Premission Denied");
            return null;
        }

        //Get user profile
        HashMap<String, Integer> userProfile = new HashMap<>(
                mapValueStringToInteger(
                        personal.getUserFeatures(username, null, null)));
        
        //Generate RecommendationObjects Map
        HashMap<String,RecommendationObject> recommendationObjects = new HashMap<>();
        //For each FeedObject
        for(FeedObject cObject : recommendationList){
            RecommendationObject reObj = new RecommendationObject(
                    cObject.getId(), 
                    cObject.getLanguage(), 
                    cObject.isRecommended(), 
                    cObject.getTimestamp());
            
            reObj.setFeatures(objectHandler(cObject));
            
            recommendationObjects.put(cObject.getId(), reObj);
        }
        
        //TODO: take score for each object
        
        
        return null;
    }

    /**
     *
     * @param obj
     * @return
     */
    private Map<String, Integer> objectHandler(FeedObject obj) {

        HashMap<String, Integer> features = new HashMap<>();

        if (obj.getTexts() != null) {
            TextConverter tc = new TextConverter(obj.getLanguage(), false);
            features.putAll(tc.getFeatures(obj.getTexts()));
        } else if (obj.getCategories() != null) {
            CategoryConverter cc = new CategoryConverter(obj.getLanguage());
            features.putAll(cc.getFeatures(obj.getCategories()));
        } else if (obj.getTags() != null) {
            TagConverter tgc = new TagConverter(obj.getLanguage());
            features.putAll(tgc.getFeatures(obj.getTags()));
        }

        return features;
    }

    /**
     * Convert Map<String,String> to Map<String,Integer>
     *
     * @param map
     * @return
     */
    private Map<String, Integer> mapValueStringToInteger(Map<String, String> map) {
        HashMap<String, Integer> returnMap = new HashMap<>();

        for (String cKey : map.keySet()) {
            returnMap.put(cKey, Integer.parseInt(map.get(cKey)));
        }

        return returnMap;
    }

    /**
     * Convert Map<String,Integer> to Map<String,String>
     *
     * @param map
     * @return
     */
    private Map<String, String> mapValueIntegerToString(Map<String, Integer> map) {
        HashMap<String, String> returnMap = new HashMap<>();

        for (String cKey : map.keySet()) {
            returnMap.put(cKey, map.get(cKey).toString());
        }

        return returnMap;
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
