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
package gr.demokritos.iit.utilities.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * This class extends Configuration class and create the
 * RecommendationConfiguration
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class RecommendationConfiguration extends Configuration {

    /**
     * RecommendationConfiguration Constructor with given configuration file
     * name
     *
     * @param configurationFileName The new filename
     */
    public RecommendationConfiguration(String configurationFileName) {
        CONFIGURATION_FILE_NAME = configurationFileName;
    }

    /**
     * The RecommendationConfiguration constructor with default configuration
     * filename
     */
    public RecommendationConfiguration() {
    }

    /**
     * Get logging level
     *
     * @return Return current logging level. If not exist on properties file
     * return the default value (info)
     */
    public String getLogLevel() {
        //Default logging level is Info
        //Return the logging level
        return properties.getProperty("LogLevel", "info");
    }

    /**
     * Get the stored object weights for the recommendation engine
     *
     * @return A map with object name - weight. If not exist a object name in
     * properties file then return default value (1).
     */
    public Map<String, Double> getObjectWeights() {

        HashMap<String, Double> weights = new HashMap<>();

        weights.put("TextWeight", Double.parseDouble(
                properties.getProperty("TextWeight", "1")));
        weights.put("CategoryWeight", Double.parseDouble(
                properties.getProperty("CategoryWeight", "1")));
        weights.put("TagWeight", Double.parseDouble(
                properties.getProperty("TagWeight", "1")));
        weights.put("BooleanWeight", Double.parseDouble(
                properties.getProperty("BooleanWeight", "1")));

        return weights;
    }

    /**
     * Get PServer mode weights
     *
     * @return A map with PServer mode name and weight. If not exist a mode name
     * in properties file then return default value (1).
     */
    public Map<String, Double> getPServerModesWeights() {

        HashMap<String, Double> weights = new HashMap<>();

        weights.put("PersonalWeight", Double.parseDouble(
                properties.getProperty("PersonalWeight", "1")));
        weights.put("StereotypeWeight", Double.parseDouble(
                properties.getProperty("StereotypeWeight", "1")));
        weights.put("CommunityWeight", Double.parseDouble(
                properties.getProperty("CommunityWeight", "1")));

        return weights;
    }

}
