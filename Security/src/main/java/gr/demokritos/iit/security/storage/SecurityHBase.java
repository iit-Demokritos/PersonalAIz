/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.storage;

import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.interfaces.ISecurityStorage;
import gr.demokritos.iit.security.ontologies.SystemUser;

/**
 * This class implement the Security Apache HBase storage system.
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class SecurityHBase implements ISecurityStorage {

    /**
     * The constructor of Security HBase storage system.
     */
    public SecurityHBase() {

    }

    
    //========================== Authentication ================================
    
    /**
     * Check credentials from HBase via username and password
     * @param username Username
     * @param password Password
     * @return The status of the checking
     */
    @Override
    public boolean checkCredentials(String username, String password) {
       
        //FIXME: checkCredentials Implement read from HBase and return the status
        return true;
    }
  
    /**
     * Check credentials from HBase via apikey
     * @param apikey The apikey
     * @return The status of the checking
     */
    @Override
    public boolean checkCredentials(String apikey) {
       
        //FIXME: checkCredentials Implement read from HBase and return the status
        return true;
    }
    
    
    
    
    
    
    
    //========================== Authorization =================================
    /**
     * Check the Access permission from HBase
     * @param u The system user
     * @param a The Action for checking
     * @param Access The Access type
     * @return The permission status
     */
    @Override
    public boolean checkAccess(SystemUser u, Action a, String Access) {

        //FIXME: checkAccess Implement read from HBase and return the status
        return true;
    }


    
    
    
    
    
}
