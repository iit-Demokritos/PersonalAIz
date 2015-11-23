/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.configuration;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class PersonalAIzConfiguration extends Configuration {

    public PersonalAIzConfiguration(String configurationFileName) {
         CONFIGURATION_FILE_NAME=configurationFileName;
    }
    public PersonalAIzConfiguration() {
    }

    
    
    /**
     * Get logging level
     * @return Return the default level (Info) or (Debug) if debug mode is ON
     */
    public String getLogLevel() {
        //Default logging level is Info
        //Return the logging level
        return properties.getProperty("LogLevel", "info");
    }
}
