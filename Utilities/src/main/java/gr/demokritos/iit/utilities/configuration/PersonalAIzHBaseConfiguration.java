/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.configuration;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class PersonalAIzHBaseConfiguration
        extends gr.demokritos.iit.utilities.configuration.Configuration {

    public PersonalAIzHBaseConfiguration(String configurationFileName) {
        CONFIGURATION_FILE_NAME = configurationFileName;
    }

    public PersonalAIzHBaseConfiguration() {

    }

    public Configuration getHBaseConfig() {
        Configuration hbaseConfig = HBaseConfiguration.create();

        hbaseConfig.set("hbase.master",
                properties.getProperty("hbase.host", "localhost") + ":60000");
        hbaseConfig.set("hbase.zookeeper.quorum",
                properties.getProperty("hbase.host", "localhost"));
        hbaseConfig.set("hbase.zookeeper.property.clientPort",
                properties.getProperty("hbase.zookeeper.property.clientPort", "2181"));

        LOGGER.debug(hbaseConfig.toString());

        return hbaseConfig;
    }

}
