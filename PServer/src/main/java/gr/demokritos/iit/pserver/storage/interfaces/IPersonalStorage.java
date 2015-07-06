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

    boolean addUsers(List<User> users);

    boolean deleteUsers(String pattern);

    Map<String, String> getUsers(String pattern, Integer page);

    boolean setUserAttributes(User user);

    Map<String, String> getUserAttributes(String username, String pattern, Integer page);

    boolean setUserFeatures(User user);

    boolean modifyUserFeatures(User user);

    Map<String, String> getUserFeatures(String username, String pattern, Integer page);
}
