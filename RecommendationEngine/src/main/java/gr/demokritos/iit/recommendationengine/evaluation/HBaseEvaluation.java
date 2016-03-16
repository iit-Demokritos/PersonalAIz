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
package gr.demokritos.iit.recommendationengine.evaluation;

import gr.demokritos.iit.utilities.configuration.PersonalAIzHBaseConfiguration;
import gr.demokritos.iit.utilities.utils.Utilities;
import java.io.IOException;
import java.io.InterruptedIOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.LoggerFactory;

/**
 * Implements the evaluation with HBase storage system
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class HBaseEvaluation implements IEvaluation {

    //=================== HBase tables ========================================
    private final String table_Evaluation = "Evaluation";

    //=================== HBase tables ========================================
    //=================== HBase Families ======================================
    private final byte[] family_Info = Bytes.toBytes("Info");

    //=================== HBase Families ======================================
    //=================== HBase Qualifiers ====================================
    private final byte[] qualifier_Client = Bytes.toBytes("Client");
    private final byte[] qualifier_Username = Bytes.toBytes("Username");
    private final byte[] qualifier_ObjId = Bytes.toBytes("ObjId");
    private final byte[] qualifier_Recommended = Bytes.toBytes("Recommended");
    private final byte[] qualifier_Timestamp = Bytes.toBytes("Timestamp");

    //=================== HBase Qualifiers ====================================
    private final Configuration config;

    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HBaseEvaluation.class);

    /**
     * HBaseEvaluation Constructor
     */
    public HBaseEvaluation() {
        //Create new HBase configuration
        config = new PersonalAIzHBaseConfiguration().getHBaseConfig();
    }

    /**
     * Store a evaluation entry on HBsase
     *
     * @param username The username
     * @param objectId The feed object id
     * @param recommended The flag if the object is recommended
     * @param timestamp The Feed timestamp
     * @param clientName The client name
     */
    @Override
    public void storeEntry(String username, String objectId,
            boolean recommended, long timestamp, String clientName) {

        String tmp = clientName + "_" + username + "_" + objectId + "_" + recommended + "_" + timestamp;
        String evaluationUID = Utilities.getUUID(tmp).toString();

        LOGGER.debug("#storeEntry | UUID: " + evaluationUID
                + " username: " + username
                + " objectId: " + objectId
                + " recommended: " + recommended
                + " timestamp: " + timestamp
                + " clientName: " + clientName);
        boolean status = true;
        HTable clientsTable = null;

        try {

            //create new Evaluation HTable
            clientsTable = new HTable(config, table_Evaluation);

        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Evaluation, ex);
        }

        //Create put method with row key
        Put put = new Put(Bytes.toBytes(evaluationUID));

        put.add(family_Info, qualifier_Username,
                Bytes.toBytes(username));
        put.add(family_Info, qualifier_Client,
                Bytes.toBytes(clientName));
        put.add(family_Info, qualifier_ObjId,
                Bytes.toBytes(objectId));
        put.add(family_Info, qualifier_Recommended,
                Bytes.toBytes(Boolean.toString(recommended)));
        put.add(family_Info, qualifier_Timestamp,
                Bytes.toBytes(Long.toString(timestamp)));

        try {

            //add client record on Clients table
            clientsTable.put(put);
            //Flush Commits
            clientsTable.flushCommits();

        } catch (InterruptedIOException | RetriesExhaustedWithDetailsException ex) {
            LOGGER.error(null, ex);
        }

        try {

            //close tables
            clientsTable.close();

        } catch (IOException ex) {
            LOGGER.error("Can't close table", ex);
        }

    }

}
