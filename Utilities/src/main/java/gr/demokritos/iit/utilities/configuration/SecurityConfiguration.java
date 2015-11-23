/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.configuration;

/**
 * This class extends Configuration class and create the SecurityConfiguration
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class SecurityConfiguration extends Configuration {

    /**
     * SecurityConfiguration Constructor with given configuration file name
     *
     * @param configurationFileName The new filename
     */
    public SecurityConfiguration(String configurationFileName) {
        CONFIGURATION_FILE_NAME = configurationFileName;
    }

    /**
     * The SecurityConfiguration constructor with default configuration filename
     */
    public SecurityConfiguration() {
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
