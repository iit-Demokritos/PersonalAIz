/* 
 * Copyright 2016 IIT , NCSR Demokritos - http://www.iit.demokritos.gr,
 *                      SciFY NPO http://www.scify.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gr.demokritos.iit.security.ontologies;

import java.util.Date;
import java.util.Map;

/**
 * Implements A ontology of the platform SystemUser
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

    /**
     * Generate a String with SystemUSer information
     *
     * @return
     */
    @Override
    public String toString() {
        return "SystemUser{" + "info=" + info + ", roles=" + roles
                + ", username=" + username
                + ", authenticatedTimestamp=" + authenticatedTimestamp
                + ", password=" + password + '}';
    }

}
