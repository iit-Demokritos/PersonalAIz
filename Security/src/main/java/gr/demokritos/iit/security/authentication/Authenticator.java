/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.authentication;

import gr.demokritos.iit.security.interfaces.IAuthentication;
import gr.demokritos.iit.security.interfaces.ISecurityStorage;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Authenticator implements IAuthentication{

    private final ISecurityStorage securityDB;

    public Authenticator(ISecurityStorage securityDB) {
        this.securityDB = securityDB;
    }

    
    /**
     * Check credentials with username and password
     * @param username 
     * @param password
     * @return The status of the checking
     */
    @Override
    public boolean checkCredentials(String username, String password) {

        //check from ISecurityStorage the credentials
        return securityDB.checkCredentials(username, password);
    }
    /**
     * Check credentials with api key
     * @param apikey 
     * @return The status of the checking
     */
    @Override
    public boolean checkCredentials(String apikey) {

        //check from ISecurityStorage the credentials
        return securityDB.checkCredentials(apikey);
    }

}
