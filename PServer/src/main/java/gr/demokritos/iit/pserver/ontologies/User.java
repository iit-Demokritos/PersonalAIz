/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.ontologies;

import gr.demokritos.iit.utilities.json.JSon;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a User object
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class User {

    private final String username;
    private Map<String, String> info = new HashMap<>();
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, String> features = new HashMap<>();

    /**
     * Empty User constructor
     *
     * @param username The username
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Auto user creator with JSON input
     *
     * @param JSONUser A JSON string with the user e.g. {"attributes":{"gender":
     * "male","age": "18"},"features": {"ftr1": "34","ftr3": "3","ftr5": "4"}}
     */
    public void userCreator(String JSONUser) {

        HashMap<String, HashMap<String, String>> user
                = new HashMap<>(JSon.unjsonize(JSONUser, HashMap.class));

        //add attributes on user
        if (user.containsKey("attributes")) {
            this.attributes.putAll(user.get("attributes"));
        }

        //add features on user
        if (user.containsKey("features")) {
            this.features.putAll(user.get("features"));
        }

    }

    /**
     * Get username
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the user information
     *
     * @param info
     */
    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    /**
     * Get the user information
     *
     * @return
     */
    public Map<String, String> getInfo() {
        return info;
    }

    /**
     * Set the user attributes
     *
     * @param attributes
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes.clear();
        this.attributes.putAll(attributes);
    }

    /**
     * Get the user attributes
     *
     * @return
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Set the user features
     *
     * @param features
     */
    public void setFeatures(Map<String, String> features) {
        this.features.clear();
        this.features.putAll(features);
    }

    /**
     * get the user features
     *
     * @return
     */
    public Map<String, String> getFeatures() {
        return features;
    }

}
