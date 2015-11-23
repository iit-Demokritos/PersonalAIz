/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
