/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.LoggerFactory;

/**
 * This class implement the genera platform configuration
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Configuration {

    protected final Properties properties;
    private File pFile;
    private static final String EXTERNAL_PROPERTIES_PATH = "./data/configuration/";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public String CONFIGURATION_FILE_NAME = "platform.properties";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    /**
     * Constructor of the Configuration class
     */
    public Configuration() {
        // Create new file from the external path
        this.pFile = new File(EXTERNAL_PROPERTIES_PATH + CONFIGURATION_FILE_NAME);
        //If not exist the external file then load the default file inside the resources
        if (!this.pFile.exists()) {
            this.pFile = new File(getClass().getResource("/configuration/"
                    + CONFIGURATION_FILE_NAME).getPath());
        }
        this.properties = new Properties();
        //load the properties
        try {
            this.properties.load(new FileInputStream(this.pFile));
        } catch (IOException e) {
            LOGGER.error("Can't open properties file", e);
        }
    }

    /**
     * Get the property with a given name
     *
     * @param sKey The property name
     * @param def A default value if property not exists
     * @return The property value
     */
    public Object getProperty(String sKey, String def) {
        if (def != null) {
            return this.properties.getProperty(sKey, def);
        } else {
            return this.properties.getProperty(sKey);
        }
    }

    /**
     * Get the all properties
     *
     * @return The property value
     */
    public Map<String, String> getProperties() {
        HashMap<String, String> propMap = new HashMap<>();

        for (String cProp : this.properties.stringPropertyNames()) {
            propMap.put(cProp, this.properties.getProperty(cProp));
        }

        return propMap;
    }

    /**
     * Check if property exist
     *
     * @param key The property name
     * @return True or False if exist the property or not
     */
    public boolean hasProperty(String key) {
        return this.properties.contains(key);
    }

    /**
     * Set value of the given property name
     *
     * @param sKey The property name
     * @param sValue The property value
     */
    public void setProperty(String sKey, String sValue) {
        this.properties.setProperty(sKey, sValue);
    }

    /**
     * Set value of the given properties name
     *
     * @param properties A map with properties name and value
     */
    public void setProperties(Map<String, String> properties) {
        this.properties.clear();
        for (String sKey : properties.keySet()) {
            this.properties.setProperty(sKey, properties.get(sKey));
        }

    }

    /**
     * Save file to Disk
     *
     * @return The save status
     */
    public boolean commit() {
        boolean status = true;
        try {
            properties.store(new FileWriter(this.pFile, Boolean.FALSE), null);
        } catch (IOException ex) {
            status = false;
            LOGGER.error("Can't commit properties file", ex);
        }

        return status;
    }

}
