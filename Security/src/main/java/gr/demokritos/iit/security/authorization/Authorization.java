/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.authorization;

import gr.demokritos.iit.security.ontologies.SystemUser;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Authorization {
    public static final String READ = "R";
    public static final String WRITE = "W";
    public static final String EXECUTE = "X";
    
    public boolean hasReadAccess(SystemUser u, Action a) {
        return true;
    }
    public boolean hasWriteAccess(SystemUser u, Action a) {
        return true;
    }
    public boolean hasExecuteAccess(SystemUser u, Action a) {
        return true;
    }
    
    public Map<String, Boolean> getAccessRights(SystemUser u, Action a) {
        HashMap<String, Boolean> hmRes = new HashMap<>();
        hmRes.put(READ, hasReadAccess(u, a));
        hmRes.put(WRITE, hasWriteAccess(u, a));
        hmRes.put(EXECUTE, hasExecuteAccess(u, a));
        
        return hmRes;
    }
}
