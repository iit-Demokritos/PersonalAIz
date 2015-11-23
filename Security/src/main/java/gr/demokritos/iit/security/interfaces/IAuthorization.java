/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.interfaces;

import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.ontologies.SystemUser;
import java.util.Map;

/**
 * The interface of the Authorization System
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface IAuthorization {

    public boolean hasReadAccess(SystemUser u, Action a);

    public boolean hasWriteAccess(SystemUser u, Action a);

    public boolean hasExecuteAccess(SystemUser u, Action a);

    public Map<String, Boolean> getAccessRights(SystemUser u, Action a);

}
