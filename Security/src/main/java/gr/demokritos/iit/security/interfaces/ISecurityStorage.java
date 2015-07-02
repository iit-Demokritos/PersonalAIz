/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.interfaces;

import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.ontologies.SystemUser;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface ISecurityStorage {
    
    //========================== Authentication ================================
    
    boolean checkCredentials(String username, String password);
    boolean checkCredentials(String apikey);
    
    //========================== Authentication ================================
    //========================== Authorization =================================
    boolean checkAccess(SystemUser u, Action a, String Access);

    //========================== Authorization =================================
    
}
