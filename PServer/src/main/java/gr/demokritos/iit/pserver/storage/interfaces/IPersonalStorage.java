/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.storage.interfaces;

import gr.demokritos.iit.pserver.ontologies.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface IPersonalStorage {

    boolean addUsers(List<User> users, String clientName);

    boolean deleteUsers(String pattern, String clientName);

    Map<String, String> getUsers(String pattern, Integer page, String clientName);

    boolean setUserAttributes(User user, String clientName);

    Map<String, String> getUserAttributes(String username, String pattern, Integer page, String clientName);

    boolean setUserFeatures(User user, String clientName);

    boolean modifyUserFeatures(User user, String clientName);

    Map<String, String> getUserFeatures(String username, String pattern, Integer page, String clientName);
}
