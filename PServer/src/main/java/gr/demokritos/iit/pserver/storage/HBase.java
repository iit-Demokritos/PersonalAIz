/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.storage;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * This class implement the PServer Apache HBase storage system.
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class HBase {

    //=================== HBase tables ========================================
    private static String usersTable = "Users";
    private static String clientsTable = "Clients";

    //=================== HBase tables ========================================
    private static Configuration config;
    private static String clientUID;

    /**
     * The constructor of HBase storage system.
     */
    public HBase(String clientUID) {
        //Set client UID on a global private variable
        this.clientUID = clientUID;

        //Create new HBase configuration
        config = HBaseConfiguration.create();

    }

    /**
     * Add a list of users on HBase
     *
     * @param users A list with all usernames that i want to add on
     */
    public void addUsers(ArrayList<String> users) throws IOException {

        //create new HTable
        HTable table = new HTable(config, "Users");

        //for each user
        for (String user : users) {

            //Create put method with row key
            Put put = new Put(Bytes.toBytes(clientUID + "-" + user));
            //add info 
            put.add(Bytes.toBytes("Info"),
                    Bytes.toBytes("Client"),
                    Bytes.toBytes(clientUID));

            table.put(put);
        }

        table.flushCommits();
        table.close();

    }
    
    
    
    /**
     * Get users from HBase Storage
     * @param pattern
     * @param range
     * @return
     */
    public ArrayList<String> getUsers(String pattern, String range) throws IOException {
        //Initialize variables
        ArrayList<String> users = new ArrayList<String>();
        Scan scan = new Scan();
        HTable table = new HTable(config, "Users");

        //TODO: Check if range is null

        
//        scan.addColumn(Bytes.toBytes("personal"), Bytes.toBytes("givenName"));
//        scan.addColumn(Bytes.toBytes("contactinfo"), Bytes.toBytes("email"));
//        scan.setFilter(new PageFilter(25));
        scan.setFilter(
                new PrefixFilter(Bytes.toBytes(clientUID+"-"))
        );
        ResultScanner scanner = table.getScanner(scan);

        for (Result result : scanner) {
//      //debug line
//            System.out.println("in-- "+Bytes.toString(result.getRow()));
            users.add(Bytes.toString(result.getRow()));
        }

        return users;
    }

    public void getRow() {

        try {
            HTable table = new HTable(config, "Users");
            Scan s = new Scan();
            ResultScanner ss = table.getScanner(s);
            for (Result r : ss) {
                for (KeyValue kv : r.raw()) {
                    System.out.print(new String(kv.getRow()) + " ");
                    System.out.print(new String(kv.getFamily()) + ":");
                    System.out.print(new String(kv.getQualifier()) + " ");
                    System.out.print(kv.getTimestamp() + " ");
                    System.out.println(new String(kv.getValue()));

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
