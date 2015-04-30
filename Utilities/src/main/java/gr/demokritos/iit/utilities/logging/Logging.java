/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Logging {
    
    public static void updateLoggerLevel(Class className, String logLevel) {
        Logger LOGGER = Logger.getLogger(className.getName());
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
