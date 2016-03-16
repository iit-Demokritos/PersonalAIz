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
     *
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
     *
     * @param apiKey
     */
    public Client(String apiKey) {

        //TODO: get username pass based on API key
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

    /**
     * Get client's username
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

}
