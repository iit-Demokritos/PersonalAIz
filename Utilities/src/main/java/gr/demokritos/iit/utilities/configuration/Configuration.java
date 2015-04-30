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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Configuration {

    protected final Properties properties;
    private File pFile;

    private static final String EXTERNAL_PROPERTIES_PATH = "./data/configuration/";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    /**
     * Constructor of the configuration file
     *
     * @param configurationFileName The properties file to load
     */
    public Configuration(String configurationFileName) {
       LOGGER.debug("dssdsd");
        // Create new file from the external path
        this.pFile = new File(EXTERNAL_PROPERTIES_PATH + configurationFileName);
        //If not exist the external file then load the default file inside the resources
        if (!this.pFile.exists()) {
            this.pFile = new File(getClass().getResource("/configuration/" + 
                    configurationFileName).getPath());
        }
        this.properties = new Properties();
        //load the properties
        try {
            this.properties.load(new FileInputStream(this.pFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
     * Save file to Disk
     */
    public void commit() {
        try {
            properties.store(new FileWriter(this.pFile, Boolean.FALSE), null);
//            properties.store(new OutputStreamWriter(new FileOutputStream(p), Charset.forName("UTF-8")));
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
