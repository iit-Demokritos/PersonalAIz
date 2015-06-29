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
 * This class represents a client object
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Client extends SystemUser {

//    private String clientUID;
    private HashMap<String, String> info = new HashMap<>();
    private HashMap<String, String> keys = new HashMap<>();
    private String username;
    private String password;
    

    public Client(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Client(String apiKey) {
        
        //TODO: get username pass
        this.username = username;
        this.password = password;
    }
    
    /**
     * Constructor for new client
     *
     * @param clientUID
     */
//    public Client(String clientUID) {
//        this.clientUID = clientUID;
//    }

    /**
     * Get the Clients UID
     *
     * @return A string with clients id
     */
//    public String getClientUID() {
//        return clientUID;
//    }
//
    /**
     * Set clients info like name, password, mail
     *
     * @param info A Map with clients info
     */
    public void setInfo(Map<String, String> info) {
        this.info.putAll(info);
    }

    /**
     * Get clients information
     *
     * @return Return a Map with clients information
     */
    public Map<String, String> getInfo() {
        return info;
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
     * Get Clients Keys
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
