/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.authorization;

import gr.demokritos.iit.security.ontologies.SystemUser;
import java.util.Map;

/**
 *
 * @author ggianna
 */
public interface IAuthorization {

    public Map<String, Boolean> getAccessRights(SystemUser u, Action a);
}