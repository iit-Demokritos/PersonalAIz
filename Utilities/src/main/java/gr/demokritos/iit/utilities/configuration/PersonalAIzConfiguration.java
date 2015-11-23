/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.configuration;

/**
 * This class extends Configuration class and create the
 * PersonalAIzConfiguration
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class PersonalAIzConfiguration extends Configuration {

    /**
     * PersonalAIzConfiguration Constructor with given configuration file name
     *
     * @param configurationFileName The new filename
     */
    public PersonalAIzConfiguration(String configurationFileName) {
        CONFIGURATION_FILE_NAME = configurationFileName;
    }

    /**
     * The PersonalAIzConfiguration constructor with default configuration
     * filename
     */
    public PersonalAIzConfiguration() {
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
