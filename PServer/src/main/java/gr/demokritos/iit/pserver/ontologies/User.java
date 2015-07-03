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

    private String userUID;
    private String username;
    private HashMap<String, String> info = new HashMap<>();
    private HashMap<String, String> attributes = new HashMap<>();
    private HashMap<String, String> features = new HashMap<>();

    public User() {

    }

    /**
     *
     * @param JSONUser A JSON string with the user e.g.
     * {"test1":{"attributes":{"gender": "male","age": "18"},"features":
     * {"ftr1": "34","ftr3": "3","ftr5": "4"}}}
     */
    public User(String JSONUser) {

        //convert JSON with users as a HashMap
        HashMap<String, Object> user = new HashMap<>(
                JSon.unjsonize(JSONUser, HashMap.class));
        
        

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setInfo(HashMap<String, String> info) {
        this.info = info;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setFeatures(HashMap<String, String> features) {
        this.features = features;
    }

    public Map<String, String> getFeatures() {
        return features;
    }

}
