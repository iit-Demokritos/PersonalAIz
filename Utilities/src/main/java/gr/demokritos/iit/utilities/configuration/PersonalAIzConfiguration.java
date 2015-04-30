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

    public static String PERSONALAIZ_PROPERTIES = "PersonalAIz.properties";

    public PersonalAIzConfiguration(String configurationFileName) {
        super(PERSONALAIZ_PROPERTIES);
    }

    
    
    /**
     * Get logging level
     * @return Return the default level (Info) or (Debug) if debug mode is ON
     */
    public String getLogLevel() {
        //Default logging level is Info
        String logLevel="info";
        
        //If debug mode is true
        if(Boolean.parseBoolean(super.properties.getProperty("LogLevel"))){
            //set logging level as debug
            logLevel="debug";
        }
        
        //Return the logging level
        return logLevel;
    }
}