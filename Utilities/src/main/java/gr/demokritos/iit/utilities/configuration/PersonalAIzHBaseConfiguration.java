/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.configuration;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 * This class extends Configuration class and create the
 * PersonalAIzHBaseConfiguration
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class PersonalAIzHBaseConfiguration
        extends gr.demokritos.iit.utilities.configuration.Configuration {

    /**
     * PersonalAIzHBaseConfiguration Constructor with given configuration file
     * name
     *
     * @param configurationFileName The new filename
     */
    public PersonalAIzHBaseConfiguration(String configurationFileName) {
        CONFIGURATION_FILE_NAME = configurationFileName;
    }

    /**
     * The PersonalAIzHBaseConfiguration constructor with default configuration
     * filename
     */
    public PersonalAIzHBaseConfiguration() {

    }

    /**
     * Get HBase configuration based on stored settings.
     *
     * @return The HBase Configuration. The default values are
     * hbase.zookeeper.quorum= localhost hbase.zookeeper.property.clientPort=
     * 2181
     */
    public Configuration getHBaseConfig() {
        Configuration hbaseConfig = HBaseConfiguration.create();

        hbaseConfig.set("hbase.zookeeper.quorum",
                properties.getProperty("zookeeper.host", "localhost"));
        hbaseConfig.set("hbase.zookeeper.property.clientPort",
                properties.getProperty("hbase.zookeeper.property.clientPort", "2181"));

        LOGGER.debug(hbaseConfig.toString());

        return hbaseConfig;
    }

}
