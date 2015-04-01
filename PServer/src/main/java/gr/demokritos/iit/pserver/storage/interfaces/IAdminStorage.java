/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.storage.interfaces;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface IAdminStorage {

    //========================== Client =======================================

    int addClient(String clientUID, String clientInfo);

    int deleteClient(String clientName);

    Map<String, String> getClients();

    int setClientRoles(String clientName, String role);

    String getClientRole(String clientName);

    //========================== Client =======================================
    //=========================== Roles =======================================
    
    int addRole(String roleName, Map<String, Boolean> actions);

    int setRoleActions(String roleName, Map<String, Boolean> actions);

    Map<String, Boolean> getRoleActions(String roleName);

    //=========================== Roles =======================================
    //======================== Settings =======================================

    Map<String, String> getSettings();

    int setSettings(Map<String, String> settings);

    //======================== Settings =======================================

}
