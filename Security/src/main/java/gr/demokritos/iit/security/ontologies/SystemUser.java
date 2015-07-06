/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.ontologies;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author ggianna
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class SystemUser {

    protected Map<String, String> info;
    protected Map<String, String> roles;
    public String username;
    public long authenticatedTimestamp = 0;
    protected String password;

    /**
     * Set system user info like name, password, mail
     *
     * @param info A Map with user informations
     */
    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    /**
     * Get system user information
     *
     * @return Return a Map with user informations
     */
    public Map<String, String> getInfo() {
        return info;
    }

    /**
     * Set user roles for permission access
     *
     * @param roles A map with user pairs of Action - status
     */
    public void setRoles(Map<String, String> roles) {
        this.roles = roles;
    }

    /**
     * Get user roles for permission access
     *
     * @return A map with user pairs of Action - status
     */
    public Map<String, String> getRoles() {
        return roles;
    }

    /**
     * Set the time in milliseconds for the period that user is authenticated
     *
     * @param authenticatedTime The time in milliseconds
     */
    public void setAuthenticatedTimestamp(long authenticatedTime) {
        this.authenticatedTimestamp = authenticatedTime;
    }

    /**
     * Update the Authenticated timestamp
     */
    public void updateAuthenticatedTimestamp() {
        Date dt = new Date();

        this.authenticatedTimestamp = dt.getTime();
    }

    /**
     * Check if user password is equals with the given password
     *
     * @param givenPassword
     * @return
     */
    public boolean comparePassword(String givenPassword) {
        return password.equals(givenPassword);
    }

}
