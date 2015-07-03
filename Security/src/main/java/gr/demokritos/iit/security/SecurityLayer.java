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
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Security is a layer which connects Authentication and Authorization layers
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class SecurityLayer {

    public final Authorizer autho;
    public final Authenticator authe;

    public SecurityLayer() {

        //TODO: Change HBase with something global to change storage from settings
        //create ISecurityStorage 
        ISecurityStorage securityDB = new SecurityHBase();

        //Create Authentication instance
        authe = new Authenticator(securityDB);
        //Create Authorization instance
        autho = new Authorizer(securityDB);
    }

    
    public SecurityLayer(ISecurityStorage securityDB) {
        //Create Authentication instance
        authe = new Authenticator(securityDB);
        //Create Authorization instance
        autho = new Authorizer(securityDB);
    }
    
    /**
     * Password encryption
     * @param password the password that we want to encrypt
     * @return 
     */
    public String encryptPassword(String password){
        return DigestUtils.sha1Hex(password);
    }

}
