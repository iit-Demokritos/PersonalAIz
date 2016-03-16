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
package gr.demokritos.iit.security.authentication;

import static gr.demokritos.iit.security.SecurityLayer.LOGGER;
import gr.demokritos.iit.security.interfaces.IAuthentication;
import gr.demokritos.iit.security.interfaces.ISecurityStorage;

/**
 * Implements the platform Authentication System
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Authenticator implements IAuthentication {

    private final ISecurityStorage securityDB;

    /**
     * Authenticator Constructor
     *
     * @param securityDB
     */
    public Authenticator(ISecurityStorage securityDB) {
        this.securityDB = securityDB;
    }

    /**
     * Check credentials with username and password
     *
     * @param username The username
     * @param password The password
     * @return The status of the checking
     */
    @Override
    public boolean checkCredentials(String username, String password) {
        LOGGER.debug("#Authenticator | checkCredentials: " + username + "/" + password);
        //check from ISecurityStorage the credentials
        return securityDB.checkCredentials(username, password);
    }

    /**
     * Check credentials with api key
     *
     * @param apikey The api key
     * @return The status of the checking
     */
    @Override
    public boolean checkCredentials(String apikey) {
        LOGGER.debug("#Authenticator | checkCredentials APIKey: " + apikey);
        //check from ISecurityStorage the credentials
        return securityDB.checkCredentials(apikey);
    }

}
