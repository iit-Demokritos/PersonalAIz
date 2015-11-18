/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.api;

import gr.demokritos.iit.pserver.api.Personal;
import gr.demokritos.iit.pserver.computationaltools.similaritymetrics.CosineSimilarity;
import gr.demokritos.iit.pserver.computationaltools.similaritymetrics.IVectorSimilarity;
import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import gr.demokritos.iit.pserver.storage.interfaces.IPersonalStorage;
import gr.demokritos.iit.recommendationengine.converters.alphanumeric.AlphanumericConverter;
import gr.demokritos.iit.recommendationengine.converters.booleans.BooleanConverter;
import gr.demokritos.iit.recommendationengine.converters.category.CategoryConverter;
import gr.demokritos.iit.recommendationengine.converters.numeric.NumericConverter;
import gr.demokritos.iit.recommendationengine.converters.tag.TagConverter;
import gr.demokritos.iit.recommendationengine.converters.text.TextConverter;
import gr.demokritos.iit.recommendationengine.evaluation.CSVEvaluation;
import gr.demokritos.iit.recommendationengine.evaluation.HBaseEvaluation;
import gr.demokritos.iit.recommendationengine.evaluation.IEvaluation;
import gr.demokritos.iit.recommendationengine.onologies.FeedObject;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.authorization.Actions;
import gr.demokritos.iit.utilities.configuration.RecommendationConfiguration;
import gr.demokritos.iit.utilities.logging.Logging;
import gr.demokritos.iit.utilities.utils.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class RecommendationEngine {

    private final Client psClient;
    public static final Logger LOGGER
            = LoggerFactory.getLogger(RecommendationEngine.class);
    private SecurityLayer security = new SecurityLayer();
    private final Personal personal;
    private final IPersonalStorage db;
    private final IEvaluation evaluation;
    private final HashMap<String, Action> actions
            = new HashMap<>(new Actions().getRecommendationActions());
    private final Utilities utilities = new Utilities();
    private final RecommendationConfiguration config;

    /**
     *
     * @param psClient
     */
    public RecommendationEngine(Client psClient) {
        this.config = new RecommendationConfiguration();
        //Update logging level 
        Logging.updateLoggerLevel(RecommendationEngine.class, config.getLogLevel());
        this.psClient = psClient;
        this.db = new PServerHBase();
        //Create PServer2 Personal mode Instance
        this.personal = new Personal(db, psClient);
        //TODO: load evaluation from settings
        evaluation = new HBaseEvaluation();
    }

    /**
     *
     * @param psClient
     * @param configurationFileName
     */
    public RecommendationEngine(Client psClient, String configurationFileName) {
        this.config = new RecommendationConfiguration(configurationFileName);
        //Update logging level 
        Logging.updateLoggerLevel(RecommendationEngine.class, config.getLogLevel());
        this.psClient = psClient;
        //TODO: change hardcoded HBsase storage with generic storage
        this.db = new PServerHBase();
        //Create PServer2 Personal mode Instance
        this.personal = new Personal(db, psClient);
        evaluation = new HBaseEvaluation();
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

        LOGGER.debug("#addUser | username: " + username
                + " attributes: " + attributes
                + " info: " + info);

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

        LOGGER.debug("#deleteUser | username: " + username);

        //Call deleteUser to remove user from PServer
        return personal.deleteUsers(username);
    }

    /**
     * Feed User's profile with given object
     *
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

        if (!object.isValidObject()) {
            LOGGER.error("No valid object: " + object.toString());
            return false;
        }

        LOGGER.debug("#feed | username: " + username
                + " FeedObject: " + object.toString());
        //Call evaluation method to store user's actions
        evaluation.storeEntry(username, object.getId(), object.isRecommended(),
                object.getTimestamp(), psClient.getUsername());

        //Convert object to features  and call setUserFeatures 
        HashMap<String, String> features = new HashMap<>(
                utilities.mapValueIntegerToString(objectHandler(object)));
        //update user profile
        if (features.isEmpty()) {
            LOGGER.error("No features to update");
            return false;
        } else {
            return personal.modifyUserFeatures(username, features);
        }
    }

    /**
     * Rank a FeedObject List based on user's profile
     *
     * @param username The username
     * @param recommendationList The FeedObject List
     * @return A shorted map with object id and score
     */
    public LinkedHashMap<String, Double> getRecommendation(String username,
            List<FeedObject> recommendationList) {
        //Check permission
        if (!getPermissionFor(actions.get("aGetRecommendations"), "R")) {
            LOGGER.error("Premission Denied");
            return null;
        }

        //Get user profile
        final HashMap<String, Integer> userProfile = new HashMap<>(
                utilities.mapValueStringToInteger(
                        personal.getUserFeatures(username, null, null)));
        
        if(userProfile.isEmpty()){
            LOGGER.error("User Profile is empty");
            return null;
        }

        //Generate RecommendationObjects Map
        final Map<String, Double> recommendationObjects
                = Collections.synchronizedMap(
                        new HashMap<String, Double>());

        //Create executor service with a thread pool with all available processors
        ExecutorService ex = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());

        //Get weights
        final HashMap<String, Double> objectWeights
                = new HashMap<>(config.getObjectWeights());

        LOGGER.debug("#getRecommendation | objectWeights: " + objectWeights);
        //For each FeedObject
        for (final FeedObject cObject : recommendationList) {

            //if object is not valid continue
            if (!cObject.isValidObject()) {
                continue;
            }
            LOGGER.debug("#getRecommendation | username: " + username
                    + " FeedObject: " + cObject.toString());

            ex.submit(new Runnable() {

                HashMap<String, Double> weights;

                @Override
                public void run() {
                    this.weights = objectWeights;
                    //Get object features
                    Map<String, Integer> objFeatures
                            = new HashMap<>(objectHandler(cObject));

                    //TODO: Make it global to load Similarity from configuration
                    //create new similarity object
                    IVectorSimilarity cs = new CosineSimilarity();
                    //Get object score
                    double score = getScore(objFeatures, userProfile, cs);

                    //add objectId and score on recommendationObjects map
                    synchronized (recommendationObjects) {
                        recommendationObjects.put(cObject.getId(), score);
                    }
                }

                /**
                 * Get the recommendation score between user's profile and
                 * object
                 *
                 * @param objFeatures the object features
                 * @param userFeatures the user's profile
                 * @return the score of the similarity
                 */
                private double getScore(Map<String, Integer> objFeatures,
                        HashMap<String, Integer> userFeatures,
                        IVectorSimilarity cs) {

                    //create new similarity object
//                    IVectorSimilarity cs = new CosineSimilarity();
                    HashSet<String> featureUnion = new HashSet<>();
                    featureUnion.addAll(objFeatures.keySet());
                    featureUnion.addAll(userFeatures.keySet());

                    double[] userVector = new double[featureUnion.size()];
                    double[] objectVector = new double[featureUnion.size()];

                    int pointer = 0;
                    for (String cFeature : featureUnion) {

                        if (userFeatures.containsKey(cFeature)) {
                            //manipulate weights
                            if (cFeature.contains(".text.")) {
                                //get user feature value and add it on vector
                                userVector[pointer]
                                        = userFeatures.get(cFeature)
                                        * weights.get("TextWeight");
                            } else if (cFeature.contains(".category.")) {
                                //get user feature value and add it on vector
                                userVector[pointer]
                                        = userFeatures.get(cFeature)
                                        * weights.get("CategoryWeight");
                            } else if (cFeature.contains(".tag.")) {
                                //get user feature value and add it on vector
                                userVector[pointer]
                                        = userFeatures.get(cFeature)
                                        * weights.get("TagWeight");
                            } else if (cFeature.contains(".boolean.")) {
                                //get user feature value and add it on vector
                                userVector[pointer]
                                        = userFeatures.get(cFeature)
                                        * weights.get("BooleanWeight");
                            }
                        } else {
                            //set value 0
                            userVector[pointer] = 0;
                        }

                        if (objFeatures.containsKey(cFeature)) {
                            //manipulate weights
                            if (cFeature.contains(".text.")) {
                                //get object feature value and add it on vector
                                objectVector[pointer]
                                        = objFeatures.get(cFeature)
                                        * weights.get("TextWeight");
                            } else if (cFeature.contains(".category.")) {
                                //get object feature value and add it on vector
                                objectVector[pointer]
                                        = objFeatures.get(cFeature)
                                        * weights.get("CategoryWeight");
                            } else if (cFeature.contains(".tag.")) {
                                //get object feature value and add it on vector
                                objectVector[pointer]
                                        = objFeatures.get(cFeature)
                                        * weights.get("TagWeight");
                            } else if (cFeature.contains(".boolean.")) {
                                //get object feature value and add it on vector
                                objectVector[pointer]
                                        = objFeatures.get(cFeature)
                                        * weights.get("BooleanWeight");
                            }
                        } else {
                            //set value 0
                            objectVector[pointer] = 0;
                        }

                        //update pointer
                        pointer++;
                    }

                    //Get similarity score and return score
                    return cs.getSimilarity(objectVector, userVector);
                }
            });
        }

        //Shutdown threads
        ex.shutdown();
        try {
            //Await an hour until terminates all threads if not complete executable
            ex.awaitTermination((long) 1.0, TimeUnit.HOURS);
        } catch (InterruptedException ex1) {
            LOGGER.error("Error on awaiting thread", ex1);
            return null;
        }

        LinkedHashMap<String, Double> sortedHashMap = new LinkedHashMap<>();
        sortedHashMap = utilities.sortHashMapByDoubleValues(recommendationObjects, true);

        LOGGER.debug("#getRecommendation | RecommendationList: " + sortedHashMap);

        return sortedHashMap;
    }

    /**
     * Get a FeedObject and return a Map with export feature names and values
     *
     * @param obj The given object
     * @return The feature map
     */
    private Map<String, Integer> objectHandler(FeedObject obj) {

        HashMap<String, Integer> features = new HashMap<>();

        if (obj.getTexts() != null) {
            //if contains text call text converter
            TextConverter tc = new TextConverter(obj.getLanguage(), true);
            //Add features to feature map
            features.putAll(tc.getFeatures(obj.getTexts()));
        }

        if (obj.getCategories() != null) {
            //if contains categories call category converter
            CategoryConverter cc = new CategoryConverter(obj.getLanguage());
            //Add features to feature map
            features.putAll(cc.getFeatures(obj.getCategories()));
        }

        if (obj.getTags() != null) {
            //if contains tags call tag converter
            TagConverter tgc = new TagConverter(obj.getLanguage());
            //Add features to feature map
            features.putAll(tgc.getFeatures(obj.getTags()));
        }

        if (obj.getBooleans() != null) {
            //if contains booleans call boolean converter
            BooleanConverter bc = new BooleanConverter(obj.getLanguage());
            //Add features to feature map
            features.putAll(bc.getFeatures(obj.getBooleans()));
        }

        if (obj.getNumerics() != null) {
            //if contains Numerics call numeric converter
            NumericConverter nc = new NumericConverter(obj.getLanguage());
            //Add features to feature map
            features.putAll(nc.getFeatures(obj.getNumerics()));
        }

        if (obj.getAlphanumerics() != null) {
            //if contains Alphanumerics call Alphanumeric converter
            AlphanumericConverter ac = new AlphanumericConverter(obj.getLanguage());
            //Add features to feature map
            features.putAll(ac.getFeatures(obj.getNumerics()));
        }

        return features;
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
