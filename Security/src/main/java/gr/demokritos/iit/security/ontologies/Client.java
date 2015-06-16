/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.ontologies;

import java.util.Map;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Client {
    private String UUID;
    private Map<String,String> info;
    private Map<String,String> roles;

    public Client(String UUID, Map<String, String> info, Map<String,String> roles) {
        this.UUID = UUID;
        this.info = info;
        this.roles = roles;
    }
    public Client(String username,String password) {

        //TODO: get from HBase client info, UUID, roles
    
    
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUUID() {
        return UUID;
    }

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
