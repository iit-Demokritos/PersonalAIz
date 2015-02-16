/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.storage;

import gr.demokritos.iit.pserver.ontologies.User;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
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

    //=================== Personal Mode ========================================
    /**
     * Add a list of user objects on HBase
     *
     * @param users A list with all users that i want to add on
     * @throws java.io.IOException
     */
    public void addUsers(ArrayList<User> users) throws IOException {

        //create new HTable
        HTable table = new HTable(config, "Users");

        //for each user
        for (User user : users) {

            //Create put method with row key
            Put put = new Put(Bytes.toBytes(user.getRowKey()));
            //add current user info
            for (String cInfo : user.getInfo().keySet()) {
                put.add(Bytes.toBytes("Info"),
                        Bytes.toBytes(cInfo),
                        Bytes.toBytes(user.getInfo().get(cInfo)));
            }
            //add current user attributes
            for (String cAttribute : user.getAttributes().keySet()) {
                put.add(Bytes.toBytes("Attributes"),
                        Bytes.toBytes(cAttribute),
                        Bytes.toBytes(user.getAttributes().get(cAttribute)));
            }

            //add current user features
            for (String cFeature : user.getFeatures().keySet()) {
                put.add(Bytes.toBytes("Features"),
                        Bytes.toBytes(cFeature),
                        Bytes.toBytes(user.getFeatures().get(cFeature)));
            }
            table.put(put);
        }

        //Flush Commits
        table.flushCommits();
        table.close();

    }

    /**
     *
     * @param pattern
     * @throws java.io.IOException
     */
    public int deleteUsers(String pattern) throws IOException {
        ArrayList<String> usersRowKey = new ArrayList<String>();
        //create new HTable
        HTable table = new HTable(config, "Users");
        //Create scan object to read users from the storage
        Scan scan = new Scan();

        //TODO: Get users via pattern
        //if pattern is null then get all users
        if (pattern == null) {
            //set a filter to get only the current client's users
            scan.setFilter(
                    new PrefixFilter(Bytes.toBytes(clientUID + "-"))
            );
            ResultScanner scanner = table.getScanner(scan);
            //for each result set get the row key
            for (Result result : scanner) {
//      //debug line
//            System.out.println("in-- "+Bytes.toString(result.getRow()));
                usersRowKey.add(Bytes.toString(result.getRow()));
            }
        } else {
            //TODO: add code for the get with filter the pattern

        }

        //For eash user delete them from the table
        for (String cUserRowKey : usersRowKey) {
            Delete delete = new Delete(Bytes.toBytes(cUserRowKey));
            table.delete(delete);
        }

        //close the table
        table.close();

        //TODO: change 100 with the status code number
        return 100;
    }

    /**
     * Get users from HBase Storage
     *
     * @param pattern
     * @param range
     * @return
     * @throws java.io.IOException
     */
    public ArrayList<String> getUsers(String pattern, String range) throws IOException {
        //Initialize variables
        ArrayList<String> users = new ArrayList<String>();
        Scan scan = new Scan();
        HTable table = new HTable(config, "Users");

//        scan.addColumn(Bytes.toBytes("personal"), Bytes.toBytes("givenName"));
//        scan.addColumn(Bytes.toBytes("contactinfo"), Bytes.toBytes("email"));
//        scan.setFilter(new PageFilter(25));
        //if pattern is null and range is null then return all the users 
        if (pattern == null && range == null) {
            //set the filter
            scan.setFilter(
                    new PrefixFilter(Bytes.toBytes(clientUID + "-"))
            );

        } else if (pattern == null) {
            //set the filter
            scan.setFilter(
                    new PrefixFilter(Bytes.toBytes(clientUID + "-"))
            );
            //TODO: Add filter for the pagination
        } else if (range == null) {
            //set the filter
            scan.setFilter(
                    new PrefixFilter(Bytes.toBytes(clientUID + "-"))
            );
            //TODO: Add filter for the pattern
        }
        //Create the result scanner
        ResultScanner scanner = table.getScanner(scan);
        //Get the RowKey for each result and add the username on the list 
        for (Result result : scanner) {
//      //debug line
//            System.out.println("in-- "+Bytes.toString(result.getRow()));
            //split the row key on - and add the clean username on the list 
            String[] username = Bytes.toString(result.getRow()).split("-");
            users.add(username[1]);
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
    //=================== Personal Mode ========================================
    //=================== Stereotype Mode ========================================

    //=================== Stereotype Mode ========================================
    //=================== Community Mode ========================================
    //=================== Community Mode ========================================
    //=================== Administration ========================================
    //=================== Administration ========================================
}
