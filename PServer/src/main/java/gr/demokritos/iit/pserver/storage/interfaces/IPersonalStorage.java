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
package gr.demokritos.iit.pserver.storage.interfaces;

import gr.demokritos.iit.pserver.ontologies.User;
import java.util.List;
import java.util.Map;

/**
 * Interface for Personal Storage implementation
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
