/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.ontologies;

import java.util.Map;

/**
 *
 * @author ggianna
 */
public class SystemUser {
    private Map<String,String> info;
    private Map<String,String> roles;

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setRoles(Map<String,String> roles) {
        this.roles = roles;
    }

    public Map<String,String> getRoles() {
        return roles;
    }
    
    
}
