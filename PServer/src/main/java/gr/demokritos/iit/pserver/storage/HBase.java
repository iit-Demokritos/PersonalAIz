/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.storage;

import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.utils.Utilities;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.exceptions.DeserializationException;
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * This class implement the PServer Apache HBase storage system.
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class HBase {

    //=================== HBase tables ========================================
    private static final String table_Users = "Users";
    private static final String table_Clients = "Clients";

    //=================== HBase tables ========================================
    //=================== HBase Families ======================================
    private static final byte[] family_Info = Bytes.toBytes("Info");
    private static final byte[] family_Attributes = Bytes.toBytes("Attributes");
    private static final byte[] family_Features = Bytes.toBytes("Features");
    private static final byte[] family_ClientUsers = Bytes.toBytes("ClientUsers");

    //=================== HBase Families ======================================
    //=================== HBase Qualifiers ====================================
    private static final byte[] qualifier_Client = Bytes.toBytes("Client");
    private static final byte[] qualifier_Username = Bytes.toBytes("Username");

    //=================== HBase Qualifiers ====================================
    private static Configuration config;
    private static String clientUID;
    private static final Integer pageSize = 20;
    public static String paging;

    /**
     * The constructor of HBase storage system.
     *
     * @param clientUID
     */
    public HBase(String clientUID) {
        //Set client UID on a global private variable
        HBase.clientUID = clientUID;

        //Create new HBase configuration
        config = HBaseConfiguration.create();

    }

    //=================== Personal Mode =======================================
    /**
     * Add a list of user objects on HBase
     *
     * @param users A list with all users that i want to add on
     * @return the Output code
     * @throws java.io.IOException
     */
    public int addUsers(
            List<User> users) throws IOException {

        //create new Users HTable
        HTable usersTable = new HTable(config, table_Users);
        //create new Clients HTable
        HTable clientsTable = new HTable(config, table_Clients);
        //Create put method with clients row key
        Put putClientUsers = new Put(Bytes.toBytes(clientUID));

        //for each user
        for (User user : users) {

            //Create put method with row key
            Put put = new Put(Bytes.toBytes(user.getUserUID()));

            //-----------------------------------------------
            Increment inc = new Increment(Bytes.toBytes(user.getUserUID()));

            //------------------------------------------------
            //add current user info
            for (String cInfo : user.getInfo().keySet()) {
                put.add(family_Info,
                        Bytes.toBytes(cInfo),
                        Bytes.toBytes(user.getInfo().get(cInfo)));
            }
            //add current user attributes
            for (String cAttribute : user.getAttributes().keySet()) {
                put.add(family_Attributes,
                        Bytes.toBytes(cAttribute),
                        Bytes.toBytes(user.getAttributes().get(cAttribute)));
            }

            //add current user features
            for (String cFeature : user.getFeatures().keySet()) {
                //add feature as element for increase
                try {
                    inc.addColumn(family_Features,
                            Bytes.toBytes(cFeature),
                            Long.parseLong(user.getFeatures().get(cFeature)));

                } catch (Exception e) {

                    //if feature is not integer then add it as not increasement feature 
                    put.add(family_Features,
                            Bytes.toBytes(cFeature),
                            Bytes.toBytes(user.getFeatures().get(cFeature)));

                }

            }

            //add user record on users table
            usersTable.put(put);

            //if increment is not null then add to user table
            if (!inc.isEmpty()) {
                usersTable.increment(inc);
            }

            //put username and UUID on clients user PUT
            putClientUsers.add(family_ClientUsers,
                    Bytes.toBytes(user.getUsername()),
                    Bytes.toBytes(user.getUserUID()));
        }

        //put users on Clients table
        clientsTable.put(putClientUsers);

        //Flush Commits
        usersTable.flushCommits();
        clientsTable.flushCommits();
        //close tables
        usersTable.close();
        clientsTable.close();

        //TODO: return the code
        return 100;

    }

    /**
     * Delete users from HBase storage
     *
     * @param pattern the pattern of the users that i want to delete
     * @return the Output code
     * @throws java.io.IOException
     */
    public int deleteUsers(
            String pattern) throws IOException {
//TODO: fix delete users
        ArrayList<String> usersRowKey = new ArrayList<>();
        //create new HTable
        HTable table = new HTable(config, table_Users);
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
     * @param page
     * @return
     * @throws java.io.IOException
     * @throws DeserializationException
     */
    public List<String> getUsers(
            String pattern,
            Integer page) throws IOException, DeserializationException {

        //Initialize variables
        ArrayList<String> users = new ArrayList<>();

        HTable table = new HTable(config, table_Clients);
        Get get = new Get(Bytes.toBytes(clientUID));
        get.addFamily(family_ClientUsers);

        ArrayList<Filter> filters = new ArrayList<>();

        //if pattern is not null then add the filter pattern
        if (pattern != null) {
            //set feature pattern as filter 
            filters.add(new ColumnPrefixFilter(Bytes.toBytes(pattern)));
        }

        //if pattern and page is null then not add extra filters
        if (!filters.isEmpty()) {
            //Set the filter list on get 
            get.setFilter(
                    new FilterList(filters)
            );
        }
        //if page not null then set the offset of the page 
        if (page != null) {

            int totalItems = table.get(get).size();
            if ((totalItems % pageSize) != 0) {
                paging = page + "/" + (totalItems / pageSize + 1);
            } else {
                paging = page + "/" + (totalItems / pageSize);
            }

            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }
        Result result = table.get(get);
        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(family_ClientUsers);

        if (result.isEmpty()) {
            return users;
        }

        for (byte[] cQ : familyMap.descendingKeySet()) {
            users.add(Bytes.toString(cQ));
        }

        return users;
    }

    /**
     * Set the User's Attributes
     *
     * @param users A list with user object and the the attributes that i want
     * to set
     * @return the Output code
     * @throws IOException
     */
    public int setUsersAttributes(
            List<User> users) throws IOException {

        return addUsers(users);
    }

    /**
     * Get user's key-value pairs of attributes and their values
     *
     * @param username The username that i want the attributes
     * @param pattern The attribute pattern to filter the list If pattern is
     * null then return the whole attribute types.
     * @param page The page number of the data that we want. If page is null
     * then return all the attribute list
     * @return A map with key-value pairs (attribute name-value). If user not
     * exist then return null
     * @throws java.io.IOException
     */
    public Map<String, String> getUserAttributes(
            String username,
            String pattern,
            Integer page) throws IOException {

        //Initialize variables
        HashMap<String, String> attMap = new HashMap<>();
        //get User UID from table Clients
        String userUID = getUserUID(username);

        if (userUID == null) {
            return null;
        }

        HTable table = new HTable(config, table_Users);
        Get get = new Get(Bytes.toBytes(userUID));
        get.addFamily(family_Attributes);

        ArrayList<Filter> filters = new ArrayList<>();

        //if pattern is not null then add the filter pattern
        if (pattern != null) {
            //set attribute pattern as filter 
            filters.add(new ColumnPrefixFilter(Bytes.toBytes(pattern)));
        }

        //if pattern and page is null then not add extra filters
        if (!filters.isEmpty()) {
            //Set the filter list on get 
            get.setFilter(
                    new FilterList(filters)
            );
        }
        //if page not null then set the offset of the page 
        if (page != null) {

            int totalItems = table.get(get).size();
            if ((totalItems % pageSize) != 0) {
                paging = page + "/" + (totalItems / pageSize + 1);
            } else {
                paging = page + "/" + (totalItems / pageSize);
            }

            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }
        Result result = table.get(get);
        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(family_Attributes);

        if (result.isEmpty()) {
            return attMap;

        }
        for (byte[] cQ : familyMap.descendingKeySet()) {
            attMap.put(
                    Bytes.toString(cQ),
                    Bytes.toString(familyMap.descendingMap().get(cQ)));
        }

        return attMap;
    }

    /**
     *
     * Set the User's Features
     *
     * @param users A list with user object and the the attributes that i want
     * to set
     * @return the Output code
     * @throws IOException
     */
    public int setUsersFeatures(
            ArrayList<User> users) throws IOException {

        return addUsers(users);
    }

    /**
     * Get user's key-value pairs of feature and their value
     *
     * @param username The username that i want the features
     * @param pattern The feature pattern to filter the list If pattern is null
     * then return the whole feature types.
     * @param page The range of the data that we want. If range is null then all
     * the feature list
     * @return A map with key-value pairs (feature name-value). If user not
     * exist return null
     * @throws java.io.IOException
     */
    public Map<String, String> getUserFeatures(
            String username,
            String pattern,
            Integer page) throws IOException {

        //Initialize variables
        HashMap<String, String> ftrMap = new HashMap<>();

        //get User UID from table Clients
        String userUID = getUserUID(username);

        if (userUID == null) {
            return null;
        }

        HTable table = new HTable(config, table_Users);
        Get get = new Get(Bytes.toBytes(userUID));
        get.addFamily(family_Features);

        ArrayList<Filter> filters = new ArrayList<>();

        //if pattern is not null then add the filter pattern
        if (pattern != null) {
            //set feature pattern as filter 
            filters.add(new ColumnPrefixFilter(Bytes.toBytes(pattern)));
        }

        //if pattern and page is null then not add extra filters
        if (!filters.isEmpty()) {
            //Set the filter list on get 
            get.setFilter(
                    new FilterList(filters)
            );
        }
        //if page not null then set the offset of the page 
        if (page != null) {

            int totalItems = table.get(get).size();
            if ((totalItems % pageSize) != 0) {
                paging = page + "/" + (totalItems / pageSize + 1);
            } else {
                paging = page + "/" + (totalItems / pageSize);
            }

            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }
        Result result = table.get(get);
        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(family_Features);

        if (result.isEmpty()) {
            return ftrMap;
        }

        for (byte[] cQ : familyMap.descendingKeySet()) {

            try {
                //if feature value is long
                ftrMap.put(
                        Bytes.toString(cQ),
                        Long.toString(Bytes.toLong(familyMap.descendingMap().get(cQ))));
            } catch (Exception e) {
                //if feature is not long
                ftrMap.put(
                        Bytes.toString(cQ),
                        Bytes.toString(familyMap.descendingMap().get(cQ)));
            }

        }
        return ftrMap;
    }

    
    /**
     * Increase or decrease Users features
     * @param usersList 
     * @return
     * @throws InterruptedIOException
     * @throws RetriesExhaustedWithDetailsException
     * @throws IOException 
     */
    public int modifyUsersFeatures(List<User> usersList) throws InterruptedIOException, RetriesExhaustedWithDetailsException, IOException {

        //create new Users HTable
        HTable usersTable = new HTable(config, table_Users);

        //for each user
        for (User user : usersList) {

            Increment inc = new Increment(Bytes.toBytes(user.getUserUID()));
            //add current user features
            for (String cFeature : user.getFeatures().keySet()) {
                inc.addColumn(family_Features,
                        Bytes.toBytes(cFeature),
                        Long.parseLong(user.getFeatures().get(cFeature)));
            }
            usersTable.increment(inc);
        }

        //Flush Commits
        usersTable.flushCommits();
        //close tables
        usersTable.close();

        //TODO: return the code
        return 100;
    }

    //=================== Personal Mode =======================================
    //=================== Stereotype Mode =====================================
    //=================== Stereotype Mode =====================================
    //=================== Community Mode ======================================
    //=================== Community Mode ======================================
    //=================== Administration ======================================
    public int addClient() {

        return 0;
    }

    /**
     * Get the UUID for the given username
     *
     * @param username The username tha i want the UUID
     * @return the UUID if username exists and null if not exists.
     * @throws IOException
     */
    public String getUserUID(String username)
            throws IOException {

        HTable table = new HTable(config, table_Clients);
        Get get = new Get(Bytes.toBytes(clientUID));
        get.setFilter((new ColumnPrefixFilter(Bytes.toBytes(username))));
        Result result = table.get(get);

        return Bytes.toString(
                result.getValue(family_ClientUsers, Bytes.toBytes(username))
        );

    }

    //=================== Administration ======================================
}
