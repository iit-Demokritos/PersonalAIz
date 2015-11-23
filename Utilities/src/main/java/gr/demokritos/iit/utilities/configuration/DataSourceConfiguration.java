/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.configuration;

/**
 * This class extends Configuration class and create the DataSourceConfiguration
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class DataSourceConfiguration extends Configuration {

    /**
     * DataSourceConfiguration Constructor with given configuration file name
     *
     * @param configurationFileName The new filename
     */
    public DataSourceConfiguration(String configurationFileName) {
        CONFIGURATION_FILE_NAME = configurationFileName;
    }

    /**
     * The DataSourceConfiguration constructor with default configuration
     * filename
     */
    public DataSourceConfiguration() {
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
