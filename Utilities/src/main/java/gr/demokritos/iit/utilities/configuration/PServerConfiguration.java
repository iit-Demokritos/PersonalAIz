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

/**
 * This class extends Configuration class and create the PServerConfiguration
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class PServerConfiguration extends Configuration {

    /**
     * PServerConnfiguration Constructor with given configuration file name
     *
     * @param configurationFileName The new filename
     */
    public PServerConfiguration(String configurationFileName) {
        CONFIGURATION_FILE_NAME = configurationFileName;
    }

    /**
     * The PServerConfiguration constructor with default configuration filename
     */
    public PServerConfiguration() {
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
}
