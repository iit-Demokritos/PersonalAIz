/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

//    public String getPropertyName() {
//        return properties.getProperty("PropertyName");
//    }
}
