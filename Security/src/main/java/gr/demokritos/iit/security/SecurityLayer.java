/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security;

import gr.demokritos.iit.security.authentication.Authenticator;
import gr.demokritos.iit.security.authorization.Authorizer;
import gr.demokritos.iit.security.interfaces.ISecurityStorage;
import gr.demokritos.iit.security.storage.SecurityHBase;
import gr.demokritos.iit.utilities.configuration.SecurityConfiguration;
import gr.demokritos.iit.utilities.logging.Logging;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SecurityLayer is a layer which connects Authentication and Authorization
 * layers
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class SecurityLayer {

    public static final Logger LOGGER = LoggerFactory.getLogger(SecurityLayer.class);
    public final Authorizer autho;
    public final Authenticator authe;
    private final SecurityConfiguration securityConfig;

    /**
     * Constructor with default ISecurity Storage
     */
    public SecurityLayer() {
        this.securityConfig = new SecurityConfiguration();
        //Update logging level 
        Logging.updateLoggerLevel(SecurityLayer.class, securityConfig.getLogLevel());

        //TODO: Change HBase with something global to change storage from settings
        //create ISecurityStorage 
        ISecurityStorage securityDB = new SecurityHBase();

        //Create Authenticator instance
        authe = new Authenticator(securityDB);
        //Create Authorizer instance
        autho = new Authorizer(securityDB);
    }

    /**
     * Constructor with custom ISecurityStorage
     *
     * @param securityDB A ISecurityStorage object
     */
    public SecurityLayer(ISecurityStorage securityDB) {
        this.securityConfig = new SecurityConfiguration();
        //Update logging level 
        Logging.updateLoggerLevel(SecurityLayer.class, securityConfig.getLogLevel());
        //Create Authenticator instance
        authe = new Authenticator(securityDB);
        //Create Authorizer instance
        autho = new Authorizer(securityDB);
    }

    /**
     * Password encryption. Calculates the SHA-1 digest and returns the value as
     * a hex string.
     *
     * @param password the password that we want to encrypt
     * @return
     */
    public String encryptPassword(String password) {
        return DigestUtils.sha1Hex(password);
    }

}
