/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.logging;

import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Logging {

    private static String LOG4J_PROPERTIES = "./res/log4j.properties";

    public static void setLOG4J_PROPERTIES(String LOG4J_PROPERTIES) {
        Logging.LOG4J_PROPERTIES = LOG4J_PROPERTIES;
    }
    
    public static void updateLoggerLevel(Logger LOGGER, String logLevel) {
        File f = new File(LOG4J_PROPERTIES);
        if (!f.exists()) {
            BasicConfigurator.configure();
        } else {
            PropertyConfigurator.configure(LOG4J_PROPERTIES);
        }
        switch (logLevel.trim().toLowerCase()) {
            case "all":
                LOGGER.setLevel(Level.ALL);
                break;
            case "debug":
                LOGGER.setLevel(Level.DEBUG);
                break;
            case "error":
                LOGGER.setLevel(Level.ERROR);
                break;
            case "fatal":
                LOGGER.setLevel(Level.FATAL);
                break;
            case "info":
                LOGGER.setLevel(Level.INFO);
                break;
            case "off":
                LOGGER.setLevel(Level.OFF);
                break;
            case "trace":
                LOGGER.setLevel(Level.TRACE);
                break;
            case "warn":
                LOGGER.setLevel(Level.WARN);
                break;
        }
    }

}
