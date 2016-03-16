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
