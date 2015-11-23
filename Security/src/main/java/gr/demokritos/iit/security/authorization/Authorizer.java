/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.authorization;

import static gr.demokritos.iit.security.SecurityLayer.LOGGER;
import gr.demokritos.iit.security.interfaces.IAuthorization;
import gr.demokritos.iit.security.interfaces.ISecurityStorage;
import gr.demokritos.iit.security.ontologies.SystemUser;
import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of the Authorization System
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Authorizer implements IAuthorization {

    public static final String READ = "R";
    public static final String WRITE = "W";
    public static final String EXECUTE = "X";
    private final ISecurityStorage securityDB;

    /**
     * The class constructor
     *
     * @param securityDB The security storage
     */
    public Authorizer(ISecurityStorage securityDB) {
        this.securityDB = securityDB;
    }

    /**
     * Check if a user has read permission for the given action
     *
     * @param u The System User
     * @param a The action
     * @return The result of the permission
     */
    @Override
    public boolean hasReadAccess(SystemUser u, Action a) {
        LOGGER.debug("#Authorizer | hasReadAccess: $SystemUser " + u.toString()
                + " $Action " + a.toString());
        //check permission form storage
        return securityDB.checkAccess(u, a, READ);
    }

    /**
     * Check if a user has write permission for the given action
     *
     * @param u The System User
     * @param a The action
     * @return The result of the permission
     */
    @Override
    public boolean hasWriteAccess(SystemUser u, Action a) {
        LOGGER.debug("#Authorizer | hasWriteAccess: $SystemUser " + u.toString()
                + " $Action " + a.toString());
        //check permission form storage
        return securityDB.checkAccess(u, a, WRITE);
    }

    /**
     * Check if a user has execute permission for the given action
     *
     * @param u The System User
     * @param a The action
     * @return The result of the permission
     */
    @Override
    public boolean hasExecuteAccess(SystemUser u, Action a) {
        LOGGER.debug("#Authorizer | hasExecuteAccess: $SystemUser " + u.toString()
                + " $Action " + a.toString());
        //check permission form storage
        return securityDB.checkAccess(u, a, EXECUTE);
    }

    /**
     * Get all access rights for the specific user and action
     *
     * @param u The System User
     * @param a The action
     * @return A map with read - write - execute and permission status
     */
    @Override
    public Map<String, Boolean> getAccessRights(SystemUser u, Action a) {
        LOGGER.debug("#Authorizer | getAccessRights: $SystemUser " + u.toString()
                + " $Action " + a.toString());
        HashMap<String, Boolean> hmRes = new HashMap<>();

        if (u.username.equals("root")) {
            hmRes.put(READ, true);
            hmRes.put(WRITE, true);
            hmRes.put(EXECUTE, true);

        } else {

            hmRes.put(READ, hasReadAccess(u, a));
            hmRes.put(WRITE, hasWriteAccess(u, a));
            hmRes.put(EXECUTE, hasExecuteAccess(u, a));
        }

        return hmRes;
    }
}
