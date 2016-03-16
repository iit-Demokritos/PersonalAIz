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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for Stereotype Storage implementation
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface IStereotypeStorage {

    Set<String> getSystemAttributes(String clientName);

    boolean addStereotype(String stereotypeName, String rule, String clientName);

    boolean deleteStereotypes(String pattern, String clientName);

    Map<String, String> getStereotypes(String pattern, Integer page, String clientName);

    boolean remakeStereotype(String stereotypeName, String clientName);

    boolean updateStereotypeFeatures(String stereotypeName, String clientName);

    boolean updateStereotypeUsers(String stereotypeName, String clientName);

    boolean findStereotypeUsers(String stereotypeName, String clientName);

    boolean checkStereotypeUsers(String stereotypeName, String clientName);

    boolean setStereotypeFeatures(String stereotypeName,
            Map<String, String> features, String clientName);

    boolean modifyStereotypeFeatures(String stereotypeName,
            Map<String, String> features, String clientName);

    Map<String, String> getStereotypeFeatures(String stereotypeName,
            String pattern, Integer page, String clientName);

    boolean deleteStereotypeFeatures(String stereotypeName,
            String pattern, String clientName);

    List<String> getStereotypeUsers(String stereotypeName, String pattern,
            Integer page, String clientName);

    List<String> getUserStereotypes(String username, String pattern,
            Integer page, String clientName);

    boolean addUserOnStereotype(String username, String stereotypeName,
            String clientName);

    boolean deleteUserFromStereotype(String username, String stereotypeName,
            String clientName);

}
