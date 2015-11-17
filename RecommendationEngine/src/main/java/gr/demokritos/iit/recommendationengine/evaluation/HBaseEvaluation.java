/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.evaluation;

import gr.demokritos.iit.pserver.ontologies.Client;
import java.io.IOException;
import java.io.InterruptedIOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.LoggerFactory;

/**
 * NOT WORKING CLASS
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class HBaseEvaluation implements IEvaluation{
    
        //=================== HBase tables ========================================
    private final String table_Evaluation = "Evaluation";

    //=================== HBase tables ========================================
    //=================== HBase Families ======================================
    private final byte[] family_Info = Bytes.toBytes("Info");
    private final byte[] family_EvalObj = Bytes.toBytes("EvalObj");

    //=================== HBase Families ======================================
    //=================== HBase Qualifiers ====================================
    private final byte[] qualifier_Client = Bytes.toBytes("Client");
    private final byte[] qualifier_Username = Bytes.toBytes("Username");

    //=================== HBase Qualifiers ====================================
    private final Configuration config;
    private final Integer pageSize = 20;

    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HBaseEvaluation.class);
    private static Client psClient;

    public HBaseEvaluation(Client psClient) {
        this.psClient= psClient;
      //Create new HBase configuration
        config = HBaseConfiguration.create();
        //FIXME: enable remote HBase before deployed build
//        config.set("hbase.master", "master:60000");
//        config.set("hbase.zookeeper.quorum", "master"); 
//        config.set("hbase.zookeeper.property.clientPort", "2181");
    
    }
    

    @Override
    public void storeEntry(String username, String objectId, 
            boolean recommended, long timestamp, String clientName) {

    
       boolean status = true;
        HTable clientsTable = null;

        try {

            //create new Evaluation HTable
            clientsTable = new HTable(config, table_Evaluation);

        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Evaluation, ex);
        }

        String evaluationUserUID = psClient.getUsername()+"_"+username;

        //Create put method with row key
        Put put = new Put(Bytes.toBytes(evaluationUserUID));

            put.add(family_Info,qualifier_Username,
                    Bytes.toBytes(username));
            put.add(family_Info,qualifier_Client,
                    Bytes.toBytes(psClient.getUsername()));
            put.add(family_EvalObj,
                    Bytes.toBytes(objectId),
                    Bytes.toBytes(recommended));

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
