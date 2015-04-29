/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.storage;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.storage.interfaces.IAdminStorage;
import gr.demokritos.iit.pserver.storage.interfaces.ICommunityStorage;
import gr.demokritos.iit.pserver.storage.interfaces.IPersonalStorage;
import gr.demokritos.iit.pserver.storage.interfaces.IStereotypeStorage;
import gr.demokritos.iit.utilities.utils.Util;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * This class implement the PServer Apache HBase storage system.
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class HBase implements IPersonalStorage, IStereotypeStorage, ICommunityStorage, IAdminStorage {

    //=================== HBase tables ========================================
    private static final String table_Users = "Users";
    private static final String table_Clients = "Clients";

    //=================== HBase tables ========================================
    //=================== HBase Families ======================================
    private static final byte[] family_Info = Bytes.toBytes("Info");
    private static final byte[] family_Attributes = Bytes.toBytes("Attributes");
    private static final byte[] family_Features = Bytes.toBytes("Features");
    private static final byte[] family_ClientUsers = Bytes.toBytes("ClientUsers");
    private static final byte[] family_Keys = Bytes.toBytes("Keys");

    //=================== HBase Families ======================================
    //=================== HBase Qualifiers ====================================
    private static final byte[] qualifier_Client = Bytes.toBytes("Client");
    private static final byte[] qualifier_Username = Bytes.toBytes("Username");
    private static final byte[] qualifier_Password = Bytes.toBytes("Password");

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
     */
    @Override
    public int addUsers(
            List<User> users) {

        HTable usersTable = null;
        HTable clientsTable = null;

        try {

            //create new Users HTable
            usersTable = new HTable(config, table_Users);

            //create new Clients HTable
            clientsTable = new HTable(config, table_Clients);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

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

            //add Client info on storage 
            put.add(family_Info,
                    Bytes.toBytes("Client"),
                    Bytes.toBytes(clientUID));

            //add current user attributes
            for (String cAttribute : user.getAttributes().keySet()) {
                put.add(family_Attributes,
                        Bytes.toBytes(cAttribute),
                        Bytes.toBytes(user.getAttributes().get(cAttribute)));
            }

            //add current user features
            for (String cFeature : user.getFeatures().keySet()) {

                try {

                    //add feature as element for increase
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

            try {

                //add user record on users table
                usersTable.put(put);

            } catch (RetriesExhaustedWithDetailsException | InterruptedIOException ex) {
                Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
            }

            //if increment is not null then add to user table
            if (!inc.isEmpty()) {

                try {

                    usersTable.increment(inc);

                } catch (IOException ex) {
                    Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //put username and UUID on clients user PUT
            putClientUsers.add(family_ClientUsers,
                    Bytes.toBytes(user.getUsername()),
                    Bytes.toBytes(user.getUserUID()));
        }

        try {

            //put users on Clients table
            clientsTable.put(putClientUsers);

            //Flush Commits
            usersTable.flushCommits();
            clientsTable.flushCommits();

        } catch (InterruptedIOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RetriesExhaustedWithDetailsException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            //close tables
            usersTable.close();
            clientsTable.close();

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //TODO: return the code
        return 100;

    }

    /**
     * Delete users from HBase storage
     *
     * @param pattern the pattern of the users that i want to delete
     * @return the Output code
     */
    @Override
    public int deleteUsers(String pattern) {

        HTable usersTable = null;
        HTable clientsTable = null;
        HashMap<String, String> usersForDelete;

        try {

            // Create an hbase users table object
            usersTable = new HTable(config, table_Users);
            // Create an hbase clients table object
            clientsTable = new HTable(config, table_Clients);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //get all users basic on pattern
        usersForDelete = new HashMap<>(getUsers(pattern, null));

        // Delete users form the client table
        Delete clientUsersDelete = new Delete(Bytes.toBytes(clientUID));

        //For eash user delete them from the table
        for (String cUserName : usersForDelete.keySet()) {
            Delete deleteUser = new Delete(Bytes.toBytes(usersForDelete.get(cUserName)));

            try {

                //delete user record from the table users
                usersTable.delete(deleteUser);

            } catch (IOException ex) {
                Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
            }

            //add delete column record for the current user
            clientUsersDelete.deleteColumn(family_ClientUsers, Bytes.toBytes(cUserName));
        }

        try {

            // delete user column from the table client
            clientsTable.delete(clientUsersDelete);

            // flush the Commits
            usersTable.flushCommits();
            clientsTable.flushCommits();

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            // close the table
            usersTable.close();
            clientsTable.close();

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //TODO: change 100 with the status code number
        return 100;
    }

    /**
     * Get users from HBase Storage
     *
     * @param pattern
     * @param page
     * @return
     */
    @Override
    public Map<String, String> getUsers(String pattern, Integer page) {

        //Initialize variables
        HashMap<String, String> users = new HashMap<>();

        HTable table = null;
        try {

            //Create clients table
            table = new HTable(config, table_Clients);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

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

            int totalItems = 0;

            try {

                totalItems = table.get(get).size();

            } catch (IOException ex) {
                Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
            }

            if ((totalItems % pageSize) != 0) {
                paging = page + "/" + (totalItems / pageSize + 1);
            } else {
                paging = page + "/" + (totalItems / pageSize);
            }

            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }
        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(family_ClientUsers);

        if (result.isEmpty()) {
            return users;
        }

        for (byte[] cQ : familyMap.descendingKeySet()) {
            users.put(Bytes.toString(cQ), Bytes.toString(familyMap.descendingMap().get(cQ)));
        }

        return users;
    }

    /**
     * Set the User's Attributes
     *
     * @param users A list with user object and the the attributes that i want
     * to set
     * @return the Output code
     */
    @Override
    public int setUsersAttributes(List<User> users) {

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
     */
    @Override
    public Map<String, String> getUserAttributes(
            String username,
            String pattern,
            Integer page) {

        //Initialize variables
        HashMap<String, String> attMap = new HashMap<>();
        //get User UID from table Clients
        String userUID = getUserUID(username);

        if (userUID == null) {
            return null;
        }

        HTable table = null;

        try {

            table = new HTable(config, table_Users);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

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

            int totalItems = 0;

            try {

                totalItems = table.get(get).size();

            } catch (IOException ex) {
                Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
            }

            if ((totalItems % pageSize) != 0) {
                paging = page + "/" + (totalItems / pageSize + 1);
            } else {
                paging = page + "/" + (totalItems / pageSize);
            }

            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }
        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

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
     */
    @Override
    public int setUsersFeatures(ArrayList<User> users) {

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
     */
    @Override
    public Map<String, String> getUserFeatures(
            String username,
            String pattern,
            Integer page) {

        //Initialize variables
        HashMap<String, String> ftrMap = new HashMap<>();

        //get User UID from table Clients
        String userUID = getUserUID(username);

        if (userUID == null) {
            return null;
        }

        HTable table = null;

        try {

            table = new HTable(config, table_Users);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

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

            int totalItems = 0;

            try {

                totalItems = table.get(get).size();

            } catch (IOException ex) {
                Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
            }

            if ((totalItems % pageSize) != 0) {
                paging = page + "/" + (totalItems / pageSize + 1);
            } else {
                paging = page + "/" + (totalItems / pageSize);
            }

            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }

        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

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
     *
     * @param usersList
     * @return
     */
    @Override
    public int modifyUsersFeatures(List<User> usersList) {

        //create new Users HTable
        HTable usersTable = null;

        try {

            usersTable = new HTable(config, table_Users);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //for each user
        for (User user : usersList) {

            Increment inc = new Increment(Bytes.toBytes(user.getUserUID()));
            //add current user features
            for (String cFeature : user.getFeatures().keySet()) {
                inc.addColumn(family_Features,
                        Bytes.toBytes(cFeature),
                        Long.parseLong(user.getFeatures().get(cFeature)));
            }

            try {

                usersTable.increment(inc);

            } catch (IOException ex) {
                Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {

            //Flush Commits
            usersTable.flushCommits();

        } catch (InterruptedIOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RetriesExhaustedWithDetailsException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            //close tables
            usersTable.close();

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //TODO: return the code
        return 100;
    }

    /**
     * Get the UUID for the given username If not User exist on Storage create
     * new UUID
     *
     * @param username The username that i want the UUID
     * @return the UUID for the given username
     */
    @Override
    public String getUserUID(String username) {
        String UUID = null;
        try {

            HTable table = new HTable(config, table_Clients);
            Get get = new Get(Bytes.toBytes(clientUID));
            get.setFilter((new ColumnPrefixFilter(Bytes.toBytes(username))));
            Result result = table.get(get);

            UUID = Bytes.toString(
                    result.getValue(family_ClientUsers, Bytes.toBytes(username))
            );

            if (UUID == null) {
                //Create new UUID
                UUID = Util.getUUID(clientUID + "-" + username).toString();
            }

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return UUID;
    }

    //=================== Personal Mode =======================================
    //=================== Stereotype Mode =====================================
    @Override
    public String addStereotype() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //=================== Stereotype Mode =====================================
    //=================== Community Mode ======================================
    //=================== Community Mode ======================================
    //=================== Administration ======================================

    @Override
    public int addClient(Client client) {
        HTable clientsTable = null;

        try {

            //create new Clients HTable
            clientsTable = new HTable(config, table_Clients);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Create put method with row key
        Put put = new Put(Bytes.toBytes(client.getClientUID()));

        //add current client info
        for (String cInfo : client.getInfo().keySet()) {
            put.add(family_Info,
                    Bytes.toBytes(cInfo),
                    Bytes.toBytes(client.getInfo().get(cInfo)));
        }

        //add client keys
        for (String cKey : client.getKeys().keySet()) {
            put.add(family_Keys,
                    Bytes.toBytes(cKey),
                    Bytes.toBytes(client.getKeys().get(cKey)));
        }

        try {

            //add client record on Clients table
            clientsTable.put(put);
            //Flush Commits
            clientsTable.flushCommits();

        } catch (InterruptedIOException | RetriesExhaustedWithDetailsException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            //close tables
            clientsTable.close();

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //TODO: return the code
        return 100;
    }

    @Override
    public int deleteClient(String clientUIDForDelete) {
        HTable clientsTable = null;

        try {
            // Create an hbase clients table object
            clientsTable = new HTable(config, table_Clients);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Add on delete the rowkey
        Delete deleteClient = new Delete(Bytes.toBytes(clientUIDForDelete));

        try {

            //delete user record from the table Clients
            clientsTable.delete(deleteClient);

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            // flush the Commits
            clientsTable.flushCommits();
            // close the table
            clientsTable.close();

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        //TODO: change 100 with the status code number
        return 100;
    }

    @Override
    public Map<String, String> getClients() {

        //Initialize variables
        HashMap<String, String> clients = new HashMap<>();

        HTable table = null;
        try {

            //Create clients table
            table = new HTable(config, table_Clients);

            Scan scan = new Scan();
//            if (pattern != null) {
//
//                //if pattern is not null then add the filter pattern
//                scan.setFilter(
//                        new SingleColumnValueFilter(
//                        family_Info,
//                        qualifier_Username,
//                        CompareFilter.CompareOp.EQUAL,
//                        Bytes.toBytes(pattern)
//                ));
//            }
            ResultScanner scanner = table.getScanner(scan);

            for (Result cResult : scanner) {
                String cName = Bytes.toString(
                        cResult.getValue(family_Info, qualifier_Username)
                );
                String cUID = Bytes.toString(
                        cResult.getRow()
                );

                clients.put(cName, cUID);

            }

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return clients;
    }

    @Override
    public int setClientRoles(String clientName, String role) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getClientRole(String clientName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getClientUID(String clientName) {
        String UID = null;
        try {

            HTable table = new HTable(config, table_Clients);
            Scan scan = new Scan();

            scan.setFilter((new SingleColumnValueFilter(
                    family_Info,
                    qualifier_Username,
                    CompareFilter.CompareOp.EQUAL,
                    Bytes.toBytes(clientName)
            )));

            ResultScanner scanner = table.getScanner(scan);
            Result result = scanner.next();

            if (result == null) {
                //Create new UUID
                UID = Util.getUUID(clientUID + "-" + clientName).toString();
            } else {
                UID = Bytes.toString(
                        result.getRow()
                );
            }

        } catch (IOException ex) {
            Logger.getLogger(HBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return UID;
    }

    @Override
    public int addRole(String roleName, Map<String, Boolean> actions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int setRoleActions(String roleName, Map<String, Boolean> actions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Boolean> getRoleActions(String roleName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> getSettings() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int setSettings(Map<String, String> settings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //=================== Administration ======================================
}
