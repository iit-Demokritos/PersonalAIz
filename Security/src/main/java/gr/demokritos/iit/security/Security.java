/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security;

import gr.demokritos.iit.security.authentication.IAuthentication;
import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.authorization.IAuthorization;
import gr.demokritos.iit.security.ontologies.SystemUser;
import java.util.Map;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Security implements IAuthentication, IAuthorization {

    public Security() {
        
        
    }
    
    
    @Override
    public boolean checkCredentials(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Boolean> getAccessRights(SystemUser u, Action a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
}
