/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.ontologies;

import gr.demokritos.iit.security.ontologies.SystemUser;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a PServer Client
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Client extends SystemUser {

    private HashMap<String, String> keys;

    /**
     * Constructor with clients username and password
     * @param username
     * @param password 
     */
    public Client(String username, String password) {
        super.username = username;
        super.password = password;
        super.info = new HashMap<>();
        this.keys = new HashMap<>();
    }

    /**
     * Constructor with clients API key
     * @param apiKey 
     */
    public Client(String apiKey) {

        //TODO: get username pass
        super.username = "";
        super.password = "";
        super.info = new HashMap<>();
        this.keys = new HashMap<>();
    }

    /**
     * Set Clients keys
     *
     * @param keys A Map with key and key expiration date
     */
    public void setKeys(Map<String, String> keys) {
        this.keys.putAll(keys);
    }

    /**
     * Get Client Keys
     *
     * @return A Map with pairs of key and expiration date
     */
    public Map<String, String> getKeys() {
        return keys;
    }

    public String getUsername() {
        return username;
    }

}
