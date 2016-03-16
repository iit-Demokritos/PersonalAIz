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
package gr.demokritos.iit.utilities.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This class help us to update the Logger level
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Logging {

    /**
     * Update Logger function
     *
     * @param className The class name
     * @param logLevel The new logger level
     */
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
