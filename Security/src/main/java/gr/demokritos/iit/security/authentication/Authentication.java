/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.authentication;

import gr.demokritos.iit.security.ontologies.Client;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Authentication {

    private String username;
    private String password;

    public Authentication(String username, String password) {

        this.username = username;
        this.password = password;

    }

    
    public boolean checkCredentials(){
        boolean success = false;
        
        //TODO: check from HBase the credentials
        
        return success;
    }
    
    
    public Client getClient(){

        return new Client(username, password);
    }
    
    
    
    
    
}
