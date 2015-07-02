/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.storage.interfaces;

import gr.demokritos.iit.pserver.ontologies.Client;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface IAdminStorage {

    //========================== Client =======================================
    boolean addClient(Client client);

    boolean deleteClient(String clientName);

    Map<String, String> getClients();

    boolean setClientRoles(String clientName, String role);

    List<String> getClientRoles(String clientName);

    String getClientUID(String clientName);

    //========================== Client =======================================
//    //=========================== Roles =======================================
//    boolean addRole(String roleName, Map<String, Boolean> actions);
//
//    boolean setRoleActions(String roleName, Map<String, Boolean> actions);
//
//    Map<String, Boolean> getRoleActions(String roleName);
//
//    //=========================== Roles =======================================

}
