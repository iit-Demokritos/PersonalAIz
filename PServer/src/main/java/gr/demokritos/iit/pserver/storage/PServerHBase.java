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
import gr.demokritos.iit.utilities.configuration.PersonalAIzHBaseConfiguration;
import gr.demokritos.iit.utilities.utils.Utilities;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
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
import org.slf4j.LoggerFactory;

/**
 * This class implement the PServer Apache HBase storage system.
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class PServerHBase implements IPersonalStorage, IStereotypeStorage, ICommunityStorage, IAdminStorage {

    //=================== HBase tables ========================================
    private final String table_Users = "Users";
    private final String table_Clients = "Clients";
    private final String table_Stereotypes = "Stereotypes";

    //=================== HBase tables ========================================
    //=================== HBase Families ======================================
    private final byte[] family_Info = Bytes.toBytes("Info");
    private final byte[] family_Attributes = Bytes.toBytes("Attributes");
    private final byte[] family_Features = Bytes.toBytes("Features");
    private final byte[] family_ClientUsers = Bytes.toBytes("ClientUsers");
    private final byte[] family_Stereotypes = Bytes.toBytes("Stereotypes");
    private final byte[] family_Users = Bytes.toBytes("Users");

    //=================== HBase Families ======================================
    //=================== HBase Qualifiers ====================================
    private final byte[] qualifier_Client = Bytes.toBytes("Client");
    private final byte[] qualifier_Username = Bytes.toBytes("Username");
    private final byte[] qualifier_Password = Bytes.toBytes("Password");
    private final byte[] qualifier_Rule = Bytes.toBytes("Rule");

    //=================== HBase Qualifiers ====================================
    private final Configuration config;
    private final Integer pageSize = 20;

    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PServerHBase.class);

    /**
     * The constructor of PServer HBase storage system.
     */
    public PServerHBase() {
        //Create new HBase configuration
        config = new PersonalAIzHBaseConfiguration().getHBaseConfig();
    }

    //=================== Personal Mode =======================================
    /**
     * Add a list of PServer user objects on HBase
     *
     * @param users A list with all users that i want to add on
     * @param clientName The client username
     * @return The status of this action
     */
    @Override
    public boolean addUsers(
            List<User> users,
            String clientName) {

        boolean status = true;
        HTable usersTable = null;
        HTable clientsTable = null;

        try {

            //create new Users HTable
            usersTable = new HTable(config, table_Users);

            //create new Clients HTable
            clientsTable = new HTable(config, table_Clients);

        } catch (IOException ex) {
            LOGGER.error("Problem on load tables", ex);
            return false;
        }

        //Create put method with client row key
        Put putClientUsers = new Put(Bytes.toBytes(getClientUID(clientName)));

        //for each user
        for (User user : users) {
            String userID = getUserUID(user.getUsername(), clientName);
            if (userID != null) {
                LOGGER.error("User exists");
                return false;
            }
            userID = generateUserUID(user.getUsername(), clientName);
            //Create put method with row key
            Put put = new Put(Bytes.toBytes(userID));

            //-----------------------------------------------
//            Increment inc = new Increment(Bytes.toBytes(userID));
            //------------------------------------------------
            //add current user info
            for (String cInfo : user.getInfo().keySet()) {
                put.add(family_Info,
                        Bytes.toBytes(cInfo),
                        Bytes.toBytes(user.getInfo().get(cInfo)));
            }

            //add Client info on storage 
            put.add(family_Info,
                    qualifier_Client,
                    Bytes.toBytes(getClientUID(clientName)));

            //add current user attributes
            for (String cAttribute : user.getAttributes().keySet()) {
                put.add(family_Attributes,
                        Bytes.toBytes(cAttribute),
                        Bytes.toBytes(user.getAttributes().get(cAttribute)));
            }

            //add current user features
            for (String cFeature : user.getFeatures().keySet()) {
                //if feature is not integer then add it as not increasement feature 
                put.add(family_Features,
                        Bytes.toBytes(cFeature),
                        Bytes.toBytes(user.getFeatures().get(cFeature)));

            }

            try {

                //add user record on users table
                usersTable.put(put);

            } catch (RetriesExhaustedWithDetailsException | InterruptedIOException ex) {
                LOGGER.error("Add Users no increment features failed", ex);
                return false;
            }

            //put username and UUID on clients user PUT
            putClientUsers.add(family_ClientUsers,
                    Bytes.toBytes(user.getUsername()),
                    Bytes.toBytes(userID));
        }

        try {

            //put users on Clients table
            clientsTable.put(putClientUsers);

            //Flush Commits
            usersTable.flushCommits();
            clientsTable.flushCommits();

        } catch (InterruptedIOException | RetriesExhaustedWithDetailsException ex) {
            LOGGER.error("Add Users failed", ex);
            return false;
        }

        try {

            //close tables
            usersTable.close();
            clientsTable.close();

        } catch (IOException ex) {
            LOGGER.error("Add Users failed", ex);
            return false;
        }

        //return the action status
        return status;

    }

    public boolean updateUsers(
            List<User> users,
            String clientName) {

        boolean status = true;
        HTable usersTable = null;

        try {

            //create new Users HTable
            usersTable = new HTable(config, table_Users);

        } catch (IOException ex) {
            LOGGER.error("Problem on load tables", ex);
            return false;
        }

        //for each user
        for (User user : users) {
            String userID = getUserUID(user.getUsername(), clientName);
            //Create put method with row key
            Put put = new Put(Bytes.toBytes(userID));

            //add current user attributes
            for (String cAttribute : user.getAttributes().keySet()) {
                put.add(family_Attributes,
                        Bytes.toBytes(cAttribute),
                        Bytes.toBytes(user.getAttributes().get(cAttribute)));
            }

            //add current user features
            for (String cFeature : user.getFeatures().keySet()) {
                //if feature is not integer then add it as not increasement feature 
                put.add(family_Features,
                        Bytes.toBytes(cFeature),
                        Bytes.toBytes(user.getFeatures().get(cFeature)));
            }

            try {

                //add user record on users table
                usersTable.put(put);

            } catch (RetriesExhaustedWithDetailsException | InterruptedIOException ex) {
                LOGGER.error("Add Users no increment features failed", ex);
                return false;
            }

        }

        try {

            //Flush Commits
            usersTable.flushCommits();

        } catch (InterruptedIOException | RetriesExhaustedWithDetailsException ex) {
            LOGGER.error("Add Users failed", ex);
            return false;
        }

        try {

            //close tables
            usersTable.close();

        } catch (IOException ex) {
            LOGGER.error("Add Users failed", ex);
            return false;
        }

        //return the action status
        return status;

    }

    /**
     * Delete users from HBase storage
     *
     * @param pattern the pattern of the users that i want to delete
     * @param clientName The client username
     * @return The status of this action
     */
    @Override
    public boolean deleteUsers(String pattern, String clientName) {

        boolean status = true;
        HTable usersTable = null;
        HTable clientsTable = null;
        HashMap<String, String> usersForDelete;

        //get all users basic on pattern
        usersForDelete = new HashMap<>(getUsers(pattern, null, clientName));

        if (usersForDelete.isEmpty()) {
            LOGGER.error("#deleteUses | no users to delete");
            return true;
        }

        try {

            // Create an hbase users table object
            usersTable = new HTable(config, table_Users);
            // Create an hbase clients table object
            clientsTable = new HTable(config, table_Clients);

        } catch (IOException ex) {
            LOGGER.error("Can't load usersTable or clientsTable", ex);
            return false;
        }

        // Delete users form the client table
        Delete clientUsersDelete = new Delete(
                Bytes.toBytes(getClientUID(clientName)));

        //For eash user delete them from the table
        for (String cUserName : usersForDelete.keySet()) {
            Delete deleteUser = new Delete(
                    Bytes.toBytes(usersForDelete.get(cUserName)));

            try {

                //delete user record from the table users
                usersTable.delete(deleteUser);

            } catch (IOException ex) {
                LOGGER.error("Can't delete user:" + cUserName + " from userTable", ex);
                return false;
            }

            //add delete column record for the current user
            clientUsersDelete.deleteColumn(family_ClientUsers,
                    Bytes.toBytes(cUserName));
        }

        try {

            // delete user column from the table client
            clientsTable.delete(clientUsersDelete);

            // flush the Commits
            usersTable.flushCommits();
            clientsTable.flushCommits();

        } catch (IOException ex) {
            LOGGER.error("Can't delete client users from clientsTable or usersTable", ex);
            return false;
        }

        try {

            // close the table
            usersTable.close();
            clientsTable.close();

        } catch (IOException ex) {
            LOGGER.error("Error on close tables", ex);
            return false;
        }

        //the action status
        return status;
    }

    /**
     * Get users from HBase Storage
     *
     * @param pattern The username pattern. If pattern is null get all users
     * @param page The page number of the results. Return alla results in page
     * is null
     * @param clientName The client username
     * @return
     */
    @Override
    public Map<String, String> getUsers(String pattern, Integer page, String clientName) {

        //Initialize variables
        HashMap<String, String> users = new HashMap<>();

        HTable table = null;
        try {

            //Create clients table
            table = new HTable(config, table_Clients);

        } catch (IOException ex) {
            LOGGER.error("Can't create clientsTable", ex);
            return null;
        }

        Get get = new Get(Bytes.toBytes(getClientUID(clientName)));
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
//
//            int totalItems = 0;
//
//            try {
//
//                totalItems = table.get(get).size();
//
//            } catch (IOException ex) {
//                LOGGER.error("Error on table get", ex);
//                return null;
//            }
//
//            if ((totalItems % pageSize) != 0) {
//                paging = page + "/" + (totalItems / pageSize + 1);
//            } else {
//                paging = page + "/" + (totalItems / pageSize);
//            }

            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }
        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            LOGGER.error("Error on table get", ex);
            return null;
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
     * @param user A PServer User object
     * @param clientName The client username
     * @return The status of this action
     */
    @Override
    public boolean setUserAttributes(User user, String clientName) {
        String userID = getUserUID(user.getUsername(), clientName);
        if (userID == null) {
            LOGGER.error("User not exists");
            return false;
        }
        List<User> usersList = new ArrayList<>();
        usersList.add(user);
        return updateUsers(usersList, clientName);
//        return addUsers(usersList, clientName);
    }

    /**
     * Get user's key-value pairs of attributes and their values
     *
     * @param username The username that i want the attributes
     * @param pattern The attribute pattern to filter the list If pattern is
     * null then return the whole attribute types.
     * @param page The page number of the data that we want. If page is null
     * then return all the attribute list
     * @param clientName The client username
     * @return A map with key-value pairs (attribute name-value). If user not
     * exist then return null
     */
    @Override
    public Map<String, String> getUserAttributes(
            String username,
            String pattern,
            Integer page,
            String clientName) {

        //Initialize variables
        HashMap<String, String> attMap = new HashMap<>();
        //get User UID from table Clients
        String userUID = getUserUID(username, clientName);

        if (userUID == null) {
            LOGGER.error("User not exists");
            return null;
        }

        HTable table = null;

        try {

            table = new HTable(config, table_Users);

        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Users, ex);
            return null;
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
            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }

        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            LOGGER.error("Error on get from table", ex);
            return null;
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
     * @param user A PServer User object
     * @param clientName The client username
     * @return The status of this action
     */
    @Override
    public boolean setUserFeatures(User user, String clientName) {
        String userID = getUserUID(user.getUsername(), clientName);
        if (userID == null) {
            LOGGER.error("User not exists");
            return false;
        }
        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(user);
        return updateUsers(usersList, clientName);
//        return addUsers(usersList, clientName);
    }

    /**
     * Get user's key-value pairs of feature and their value
     *
     * @param username The username that i want the features
     * @param pattern The feature pattern to filter the list If pattern is null
     * then return the whole feature types.
     * @param page The range of the data that we want. If range is null then all
     * the feature list
     * @param clientName The client username
     * @return A map with key-value pairs (feature name-value). If user not
     * exist return null
     */
    @Override
    public Map<String, String> getUserFeatures(
            String username,
            String pattern,
            Integer page,
            String clientName) {

        //Initialize variables
        HashMap<String, String> ftrMap = new HashMap<>();

        //get User UID from table Clients
        String userUID = getUserUID(username, clientName);

        if (userUID == null) {
            LOGGER.error("User no exists");
            return null;
        }

        HTable table = null;

        try {

            table = new HTable(config, table_Users);

        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Users, ex);
            return null;
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

            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }

        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            LOGGER.error("Error on get from table", ex);
            return null;
        }

        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(family_Features);

        if (result.isEmpty()) {
            LOGGER.error("Result is empty");
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
     * Increase or decrease User features
     *
     * @param user A PServer User object
     * @param clientName The client username
     * @return The status of this action
     */
    @Override
    public boolean modifyUserFeatures(User user, String clientName) {

        //Get user UID
        String userID = getUserUID(user.getUsername(), clientName);
        if (userID == null) {
            LOGGER.error("User not exists");
            return false;
        }

        //Get user features
        HashMap<String, String> userFeatures = new HashMap<>(
                getUserFeatures(user.getUsername(), null, null, clientName));

        HashMap<String, String> featureModify = new HashMap<>(user.getFeatures());

        for (String cFeature : featureModify.keySet()) {
            try {
                int newValue;
                //If it is a new feature then add it with the value of modification
                if (userFeatures.containsKey(cFeature)) {
                    int oldValue = Integer.parseInt(userFeatures.get(cFeature));
                    int inc = Integer.parseInt(featureModify.get(cFeature));
                    newValue = oldValue + inc;

                } else {
                    newValue = Integer.parseInt(featureModify.get(cFeature));
                }
                user.getFeatures().put(cFeature, String.valueOf(newValue));
            } catch (Exception ex) {
                LOGGER.error("No numeric feature:" + cFeature
                        + " value: " + featureModify.get(cFeature));
                return false;
            }
        }

        List<User> usersList = new ArrayList<>();
        usersList.add(user);
        return updateUsers(usersList, clientName);
    }

    /**
     * Get the UUID for the given username If not User exist on Storage return
     * null
     *
     * @param username The username that i want the UUID
     * @return the UUID for the given username
     */
    private String getUserUID(String username, String clientName) {
        String UUID = null;
        String clientUID = getClientUID(clientName);
        try {

            HTable table = new HTable(config, table_Clients);
            Get get = new Get(Bytes.toBytes(clientUID));
            get.setFilter((new ColumnPrefixFilter(Bytes.toBytes(username))));
            Result result = table.get(get);

            UUID = Bytes.toString(
                    result.getValue(family_ClientUsers, Bytes.toBytes(username))
            );

//            if (UUID == null) {
//                //Create new UUID
//                UUID = Util.getUUID(clientUID + "-" + username).toString();
//            }
        } catch (IOException ex) {
            LOGGER.error("IOException", ex);
        }
        return UUID;
    }

    /**
     * Generate new UUID
     *
     * @param username The username
     * @param clientName The clients name
     * @return The UUID
     */
    private String generateUserUID(String username, String clientName) {
        String UUID = null;
        String clientUID = getClientUID(clientName);

        //Create new UUID
        UUID = Utilities.getUUID(clientUID + "-" + username).toString();
        return UUID;
    }

    //=================== Personal Mode =======================================
    //=================== Stereotype Mode =====================================
    /**
     * Get a set with system attributes
     *
     * @param clientName The client name
     * @return A set with attribute names
     */
    @Override
    public Set<String> getSystemAttributes(String clientName) {

        HashSet<String> attributes = new HashSet<>();

        //get all clients users
        HashSet<String> users = new HashSet<>();
        try {
            users.addAll(getUsers(null, null, clientName).keySet());
        } catch (Exception e) {
            LOGGER.error("Failed to get Users", e);
            return null;
        }

        //for each user
        for (String cUser : users) {
            try {
                //Get user attributes and added it on the list
                attributes.addAll(
                        getUserAttributes(cUser, null, null, clientName).keySet());
            } catch (Exception e) {
                LOGGER.error("Failed to get User attributes" + cUser, e);
                return null;
            }
        }

        return attributes;
    }

    /**
     * Add new stereotype on HBase storage
     *
     * @param stereotypeName The stereotypes name
     * @param rule The rule to create the stereotype
     * @param clientName The clients name
     * @return The status of this action
     */
    @Override
    public boolean addStereotype(String stereotypeName, String rule, String clientName) {

        HTable stereotypesTable = null;
        HTable clientsTable = null;

        //Generate stereotype UID
        String stereotypeUID = generateStereotypeUID(stereotypeName, clientName);

        //Get clients UID
        String clientUID = getClientUID(clientName);

        try {

            //create new Stereotypes HTable
            stereotypesTable = new HTable(config, table_Stereotypes);

            //create new Clients HTable
            clientsTable = new HTable(config, table_Clients);

        } catch (IOException ex) {
            LOGGER.error("Problem on load tables", ex);
            return false;
        }

        //Add record on Clients table --> stereotypes CF
        //Create put method with client row key
        Put putClientStereotypes = new Put(Bytes.toBytes(clientUID));
        //put Stereotype name and UID on clients Stereotype PUT
        putClientStereotypes.add(family_Stereotypes,
                Bytes.toBytes(stereotypeName),
                Bytes.toBytes(stereotypeUID));

        //Add record on Stereotypes table
        //Create put method with stereotype row key
        Put putStereotype = new Put(Bytes.toBytes(stereotypeUID));
        putStereotype.add(family_Info,
                qualifier_Client,
                Bytes.toBytes(clientUID));
        putStereotype.add(family_Info,
                qualifier_Rule,
                Bytes.toBytes(rule));

        try {
            //Put on tables
            stereotypesTable.put(putStereotype);
            clientsTable.put(putClientStereotypes);

            //Flush Commits
            stereotypesTable.flushCommits();
            clientsTable.flushCommits();

        } catch (InterruptedIOException | RetriesExhaustedWithDetailsException ex) {
            LOGGER.error("Fail to add stereotype on tables", ex);
            return false;
        }

        try {
            //close tables
            stereotypesTable.close();
            clientsTable.close();
        } catch (IOException ex) {
            LOGGER.error("Fail to close stereotype and client tables", ex);
            return false;
        }

        //Call find stereotype users to add users on the stereotype
        if (findStereotypeUsers(stereotypeName, clientName)) {
            //Call update stereotype features
            if (updateStereotypeFeatures(stereotypeName, clientName)) {
                return true;
            } else {
                LOGGER.error("Fail to update stereotype features."
                        + " Delete Stereotype from storage");
                deleteStereotypes(stereotypeName, clientName);
                return false;
            }
        } else {
            LOGGER.error("Fail to find stereotype users."
                    + " Delete Stereotype from storage");
            deleteStereotypes(stereotypeName, clientName);
            return false;
        }
    }

    /**
     *
     * @param pattern
     * @param clientName
     * @return
     */
    @Override
    public boolean deleteStereotypes(String pattern, String clientName) {

        boolean status = true;
        HTable clientsTable = null;
        HTable usersTable = null;
        HTable stereotypeTable = null;
        HashMap<String, String> stereotypesForDelete;

        try {

            // Create an hbase clients table object
            clientsTable = new HTable(config, table_Clients);
            // Create an hbase users table object
            usersTable = new HTable(config, table_Users);
            // Create an hbase stereotypes table object
            stereotypeTable = new HTable(config, table_Stereotypes);

        } catch (IOException ex) {
            LOGGER.error("Can't load usersTable or clientsTable or stereotypesTable", ex);
            return false;
        }

        //get all stereotypes based on pattern
        stereotypesForDelete = new HashMap<>(
                getStereotypes(pattern, null, clientName));

        // Delete stereotypes form the client table
        Delete clientStereotypesDelete = new Delete(
                Bytes.toBytes(getClientUID(clientName)));

        //For eash stereotype delete them from the table
        for (String cSterName : stereotypesForDelete.keySet()) {

            ArrayList<String> users = new ArrayList<>();
            users.addAll(getStereotypeUsers(cSterName, null, null, clientName));
            Delete stereotypeDelete = new Delete(
                    Bytes.toBytes(stereotypesForDelete.get(cSterName)));

            try {

                //delete stereotype record from the table Stereotypes
                stereotypeTable.delete(stereotypeDelete);

            } catch (IOException ex) {
                LOGGER.error("Can't delete stereotype:" + cSterName + " from stereotypeTable", ex);
                return false;
            }

            //add delete column record for the current stereotype
            clientStereotypesDelete.deleteColumn(family_Stereotypes,
                    Bytes.toBytes(cSterName));

            for (String cUser : users) {

                // Delete stereotypes form the user table
                Delete userStereotypesDelete = new Delete(Bytes.toBytes(getUserUID(cUser, clientName)));
                //add delete column record for the current stereotype
                userStereotypesDelete.deleteColumn(family_Stereotypes, Bytes.toBytes(cSterName));
                try {
                    // delete stereotype column from the table users
                    usersTable.delete(userStereotypesDelete);
                } catch (IOException ex) {
                    LOGGER.error("Can't delete stereotype:" + cSterName + " from usersTable", ex);
                    return false;
                }
            }
        }

        try {

            // delete stereotype column from the table client
            clientsTable.delete(clientStereotypesDelete);

            // flush the Commits
            clientsTable.flushCommits();
            stereotypeTable.flushCommits();
            usersTable.flushCommits();

        } catch (IOException ex) {
            LOGGER.error("Can't delete stereotype from clientsTable or stereotypeTable or usersTable", ex);
            return false;
        }

        try {

            // close the table
            clientsTable.close();
            stereotypeTable.close();
            usersTable.close();

        } catch (IOException ex) {
            LOGGER.error("Error on close tables", ex);
            return false;
        }

        //the action status
        return status;
    }

    /**
     *
     * @param pattern
     * @param page
     * @param clientName
     * @return
     */
    @Override
    public Map<String, String> getStereotypes(String pattern, Integer page, String clientName) {

        //Initialize variables
        HashMap<String, String> stereotypes = new HashMap<>();

        HTable table = null;
        try {

            //Create clients table
            table = new HTable(config, table_Clients);

        } catch (IOException ex) {
            LOGGER.error("Can't create clientsTable", ex);
            return null;
        }

        Get get = new Get(Bytes.toBytes(getClientUID(clientName)));
        get.addFamily(family_Stereotypes);

        ArrayList<Filter> filters = new ArrayList<>();

        //if pattern is not null then add the filter pattern
        if (pattern != null) {
            //set stereotype pattern as filter 
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
            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }

        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            LOGGER.error("Error on table get", ex);
            return null;
        }

        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(family_Stereotypes);

        if (result.isEmpty()) {
            return stereotypes;
        }

        for (byte[] cQ : familyMap.descendingKeySet()) {
            stereotypes.put(Bytes.toString(cQ), Bytes.toString(familyMap.descendingMap().get(cQ)));
        }

        return stereotypes;
    }

    /**
     * Remake the stereotype with the given name
     *
     * @param stereotypeName The stereotypes name
     * @param clientName The client name
     * @return The status of this action
     */
    @Override
    public boolean remakeStereotype(String stereotypeName, String clientName) {

        //Call update stereotype Users to update stereotype users
        if (updateStereotypeUsers(stereotypeName, clientName)) {
            //Call update stereotype Features to update features
            if (updateStereotypeFeatures(stereotypeName, clientName)) {
                return true;
            } else {
                LOGGER.error("Error on update Stereotype Features");
                return false;
            }
        } else {
            LOGGER.error("Error on update Stereotype Users");
            return false;
        }
    }

    /**
     * Update stereotypes features based on users profile
     *
     * @param stereotypeName The stereotype name
     * @param clientName The client name
     * @return The status of this action
     */
    @Override
    public boolean updateStereotypeFeatures(String stereotypeName, String clientName) {

        //Get stereotype UID
        String stereotypeUID = getStereotypeUID(stereotypeName, clientName);
        //Check if stereotype exists
        if (stereotypeUID == null) {
            LOGGER.error("Stereotype name:" + stereotypeName + " not exists");
            return false;
        }

        //Get stereotype users
        ArrayList<String> stereotypeUsers = new ArrayList<>();
        stereotypeUsers.addAll(
                getStereotypeUsers(
                        stereotypeName,
                        null,
                        null,
                        clientName));

        return setStereotypeFeatures(stereotypeName,
                createFeatureCentroid(stereotypeUsers, clientName), clientName);
    }

    /**
     * Update the stereotypes users. Check the users who not satisfies the rule
     * and find the new users who satisfies the rule.
     *
     * @param stereotypeName The stereotype name
     * @param clientName The client name
     * @return The status of this action
     */
    @Override
    public boolean updateStereotypeUsers(String stereotypeName, String clientName) {

        //Call check stereotype users 
        if (checkStereotypeUsers(stereotypeName, clientName)) {
            //Call find stereotype users
            if (findStereotypeUsers(stereotypeName, clientName)) {
                return true;
            } else {
                LOGGER.error("Error on Find Stereotype Users");
                return false;
            }
        } else {
            LOGGER.error("Error on Check Stereotype Users");
            return false;
        }
    }

    /**
     * Find the new users that satisfies the stereotype rule and them on
     * stereotype
     *
     * @param stereotypeName The stereotype name
     * @param clientName The client name
     * @return The status of this action
     */
    @Override
    public boolean findStereotypeUsers(String stereotypeName, String clientName) {

        Boolean status = true;

        //Get stereotypes UID
        String stereotypeUID = getStereotypeUID(stereotypeName, clientName);
        //Check if stereotype exists
        if (stereotypeUID == null) {
            LOGGER.error("Stereotype name:" + stereotypeName + " not exists");
            return false;
        }
        //Get the rule from stereotype
        String rule = getStereoypeRule(stereotypeUID);
        //Parse query to filter
        QueryParser qp = new QueryParser();
        FilterList filter = qp.getParsedQuery(rule);

        //Get all stereotype users
        ArrayList<String> allSystemUsers = new ArrayList<>();
        allSystemUsers.addAll(
                getStereotypeUsers(stereotypeName, null, null, clientName));

        //Get all users how satisfies the rule
        ArrayList<String> findUsers = new ArrayList<>();
        findUsers.addAll(getUsersFromFilter(filter, clientName));

        //For each user in find list
        for (String cUser : findUsers) {
            //Check if is already exist on stereotypes users
            if (!allSystemUsers.contains(cUser)) {
                //If not exist add username to on stereotype
                if (!addUserOnStereotype(cUser, stereotypeName, clientName)) {
                    LOGGER.error("Fail to add user:" + cUser + " on Stereotype" + stereotypeName);
                    status = false;
                }
            }
        }

        return status;
    }

    /**
     * Check the stereotype users and remove the users who not satisfies the
     * stereotype rule
     *
     * @param stereotypeName The stereotype name
     * @param clientName The client name
     * @return The status of this action
     */
    @Override
    public boolean checkStereotypeUsers(String stereotypeName, String clientName) {

        Boolean status = true;

        //Get stereotypes UID
        String stereotypeUID = getStereotypeUID(stereotypeName, clientName);
        //Check if stereotype exists
        if (stereotypeUID == null) {
            LOGGER.error("Stereotype name:" + stereotypeName + " not exists");
            return false;
        }

        //Get the rule from stereotype
        String rule = getStereoypeRule(stereotypeUID);
        //Parse query to filter
        QueryParser qp = new QueryParser();
        FilterList filter = qp.getParsedQuery(rule);

        //Get all stereotype users
        ArrayList<String> allSystemUsers = new ArrayList<>();
        allSystemUsers.addAll(
                getStereotypeUsers(stereotypeName, null, null, clientName));

        //Get all users how satisfies the rule
        ArrayList<String> checkUsers = new ArrayList<>();
        checkUsers.addAll(getUsersFromFilter(filter, clientName));

        //For each user in the stereotype
        for (String cUser : allSystemUsers) {
            //Check if exist on check users list
            if (!checkUsers.contains(cUser)) {
                //If not exist delete username from stereotype
                if (!deleteUserFromStereotype(cUser, stereotypeName, clientName)) {
                    LOGGER.error("Fail to delete user:" + cUser + " on Stereotype" + stereotypeName);
                    status = false;
                }
            }
        }

        return status;
    }

    /**
     * Set Stereotypes features. Add on stereotype if features not exist
     *
     * @param stereotypeName The stereotype name
     * @param features A map with featurs key value pairs. (key: feature name ,
     * value: the feature value)
     * @param clientName The client name
     * @return The status of this action
     */
    @Override
    public boolean setStereotypeFeatures(String stereotypeName,
            Map<String, String> features, String clientName) {

        boolean status = true;
        HTable stereotypesTable = null;

        //Get stereotypes UID
        String stereotypeUID = getStereotypeUID(stereotypeName, clientName);
        if (stereotypeUID == null) {
            LOGGER.error("Stereotype name:" + stereotypeName + " not exists");
            return false;
        }

        try {
            //create new Stereotypes HTable
            stereotypesTable = new HTable(config, table_Stereotypes);
        } catch (IOException ex) {
            LOGGER.error("Problem on load Stereotype table", ex);
            return false;
        }

        //Create put method with row key
        Put put = new Put(Bytes.toBytes(stereotypeUID));

        //add current stereotype features
        for (String cFeature : features.keySet()) {
            put.add(family_Features,
                    Bytes.toBytes(cFeature),
                    Bytes.toBytes(features.get(cFeature)));
        }

        try {
            //add the updated records on stereotypes table
            stereotypesTable.put(put);
            //Flush Commits
            stereotypesTable.flushCommits();
        } catch (RetriesExhaustedWithDetailsException | InterruptedIOException ex) {
            LOGGER.error("Set stereotype features failed", ex);
            return false;
        }

        try {
            //close tables
            stereotypesTable.close();
        } catch (IOException ex) {
            LOGGER.error("Failed to close stereotype table", ex);
            return false;
        }

        //return the action status
        return status;
    }

    /**
     * Modify Stereotypes features (increase/decrease). Add on stereotype if
     * features not exist
     *
     * @param stereotypeName The stereotype name
     * @param modifyFeatures The map with features. Key - value pairs (key:
     * feature name , value: the modification value)
     * @param clientName The client name
     * @return The status of this action
     */
    @Override
    public boolean modifyStereotypeFeatures(String stereotypeName,
            Map<String, String> modifyFeatures, String clientName) {

        //Get stereotype UID
        String stereotypeUID = getStereotypeUID(stereotypeName, clientName);
        //Check if sterotype exists
        if (stereotypeUID == null) {
            LOGGER.error("Stereotype name:" + stereotypeName + " not exists");
            return false;
        }

        //Get stereotype features
        HashMap<String, String> stereotypeFeatures = new HashMap<>();
        stereotypeFeatures.putAll(
                getStereotypeFeatures(stereotypeName,
                        null,
                        null,
                        clientName));

        HashMap<String, String> featuresForSet = new HashMap<>();

        for (String cFeature : modifyFeatures.keySet()) {
            try {
                int newValue;
                //If it is a new feature then add it with the value of modification
                if (stereotypeFeatures.containsKey(cFeature)) {
                    int oldValue = Integer.parseInt(stereotypeFeatures.get(cFeature));
                    int inc = Integer.parseInt(modifyFeatures.get(cFeature));
                    newValue = oldValue + inc;

                } else {
                    newValue = Integer.parseInt(modifyFeatures.get(cFeature));
                }
                featuresForSet.put(cFeature, String.valueOf(newValue));
            } catch (Exception ex) {
                LOGGER.error("No numeric feature:" + cFeature
                        + " value: " + modifyFeatures.get(cFeature));
                return false;
            }
        }

        //Set new features
        return setStereotypeFeatures(stereotypeName, featuresForSet, clientName);
    }

    /**
     * Get all stereotypes features based on stereotype name, pattern and page
     *
     * @param stereotypeName The stereotype name
     * @param pattern The feature pattern. If null return all features
     * @param page The page of the result. If null return all features on single
     * page
     * @param clientName The client name
     * @return A map with feature key - value pairs
     */
    @Override
    public Map<String, String> getStereotypeFeatures(String stereotypeName,
            String pattern, Integer page, String clientName) {

        //Initialize variables
        HashMap<String, String> featuresMap = new HashMap<>();

        //get Stereotype ID from table Clients
        String stereotypeID = getStereotypeUID(stereotypeName, clientName);

        if (stereotypeID == null) {
            LOGGER.error("Stereotype name:" + stereotypeName + " not exists");
            return null;
        }

        HTable table = null;

        try {

            table = new HTable(config, table_Stereotypes);

        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Stereotypes, ex);
            return null;
        }

        Get get = new Get(Bytes.toBytes(stereotypeID));
        get.addFamily(family_Features);

        ArrayList<Filter> filters = new ArrayList<>();

        //if pattern is not null then add the filter pattern
        if (pattern != null) {
            //set features pattern as filter 
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
            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }

        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            LOGGER.error("Error on get from table", ex);
            return null;
        }

        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(family_Features);

        if (result.isEmpty()) {
            return featuresMap;
        }

        //For each Feature on the list
        for (byte[] cQ : familyMap.descendingKeySet()) {
            featuresMap.put(Bytes.toString(cQ),
                    Bytes.toString(familyMap.descendingMap().get(cQ)));
        }

        return featuresMap;
    }

    /**
     *
     * @param stereotypeName
     * @param pattern
     * @param clientName
     * @return
     */
    @Override
    public boolean deleteStereotypeFeatures(String stereotypeName, String pattern, String clientName) {

        //Get stereotype UID
        String stereotypeUID = getStereotypeUID(stereotypeName, clientName);
        //Check if stereotype exists
        if (stereotypeUID == null) {
            LOGGER.error("Stereotype name:" + stereotypeName + " not exists");
            return false;
        }

        boolean status = true;
        HTable stereotypeTable = null;
        HashMap<String, String> featuresForDelete;

        try {

            // Create an hbase stereotypes table object
            stereotypeTable = new HTable(config, table_Stereotypes);

        } catch (IOException ex) {
            LOGGER.error("Can't load stereotypesTable", ex);
            return false;
        }

        //get all features based on pattern
        featuresForDelete = new HashMap<>(getStereotypeFeatures(stereotypeName,
                pattern, null, clientName));

        // Delete stereotypes form the client table
        Delete stereotypeFeaturesDelete = new Delete(
                Bytes.toBytes(getStereotypeUID(stereotypeName, clientName)));

        //For eash feature delete them from the table
        for (String cFtrName : featuresForDelete.keySet()) {
            //add delete column record for the current stereotype
            stereotypeFeaturesDelete.deleteColumn(
                    family_Features, Bytes.toBytes(cFtrName));
        }

        try {

            // delete feature column from the table stereotype
            stereotypeTable.delete(stereotypeFeaturesDelete);

            // flush the Commits
            stereotypeTable.flushCommits();

        } catch (IOException ex) {
            LOGGER.error("Can't delete stereotype features from stereotype Table", ex);
            return false;
        }

        try {

            // close the table
            stereotypeTable.close();

        } catch (IOException ex) {
            LOGGER.error("Error on close tables", ex);
            return false;
        }

        //the action status
        return status;
    }

    /**
     *
     * @param stereotypeName
     * @param pattern
     * @param page
     * @param clientName
     * @return
     */
    @Override
    public List<String> getStereotypeUsers(String stereotypeName, String pattern, Integer page, String clientName) {

        //Initialize variables
        ArrayList<String> usersList = new ArrayList<>();

        //get Stereotype ID from table Clients
        String stereotypeID = getStereotypeUID(stereotypeName, clientName);

        if (stereotypeID == null) {
            LOGGER.error("Stereotype name:" + stereotypeName + " not exists");
            return null;
        }

        HTable table = null;

        try {

            table = new HTable(config, table_Stereotypes);

        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Stereotypes, ex);
            return null;
        }

        Get get = new Get(Bytes.toBytes(stereotypeID));
        get.addFamily(family_Users);

        ArrayList<Filter> filters = new ArrayList<>();

        //if pattern is not null then add the filter pattern
        if (pattern != null) {
            //set usernames pattern as filter 
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
            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }

        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            LOGGER.error("Error on get from table", ex);
            return null;
        }

        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(family_Users);

        if (result.isEmpty()) {
            return usersList;
        }

        //For each User on the list
        for (byte[] cQ : familyMap.descendingKeySet()) {
            usersList.add(Bytes.toString(cQ));
        }

        return usersList;
    }

    /**
     *
     * @param username
     * @param pattern
     * @param page
     * @param clientName
     * @return
     */
    @Override
    public List<String> getUserStereotypes(String username, String pattern, Integer page, String clientName) {

        //Initialize variables
        ArrayList<String> strList = new ArrayList<>();

        //get User UID from table Clients
        String userUID = getUserUID(username, clientName);
        if (userUID == null) {
            LOGGER.error("User: " + username + " not exists");
            return null;
        }

        HTable table = null;

        try {

            table = new HTable(config, table_Users);

        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Users, ex);
            return null;
        }

        Get get = new Get(Bytes.toBytes(userUID));
        get.addFamily(family_Stereotypes);

        ArrayList<Filter> filters = new ArrayList<>();

        //if pattern is not null then add the filter pattern
        if (pattern != null) {
            //set stereotype pattern as filter 
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
            //add page filter of the result
            get.setFilter((new ColumnPaginationFilter(pageSize, pageSize * (page - 1))));
        }

        Result result = null;

        try {

            result = table.get(get);

        } catch (IOException ex) {
            LOGGER.error("Error on get from table", ex);
            return null;
        }

        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(family_Stereotypes);

        if (result.isEmpty()) {
            return strList;
        }

        //For each Stereotype on the list
        for (byte[] cQ : familyMap.descendingKeySet()) {
            strList.add(Bytes.toString(cQ));
        }

        return strList;
    }

    /**
     * Add a user on stereotype manually.
     *
     * @param username The username
     * @param stereotypeName The stereotype name
     * @param clientName The client name
     * @return The status of this action
     */
    @Override
    public boolean addUserOnStereotype(String username, String stereotypeName, String clientName) {

        //Get user UID
        String userUID = getUserUID(username, clientName);
        //Check user if exist
        if (userUID == null) {
            LOGGER.error("User: " + username + " not exists");
            return false;
        }

        //Get stereotype UID
        String stereotypeUID = getStereotypeUID(stereotypeName, clientName);
        //Check stereotype if exist
        if (stereotypeUID == null) {
            LOGGER.error("Stereotype: " + stereotypeName + " not exists");
            return false;
        }

        HTable stereotypesTable = null;
        HTable usersTable = null;

        try {
            //create new Stereotypes HTable
            stereotypesTable = new HTable(config, table_Stereotypes);

            //create new Users HTable
            usersTable = new HTable(config, table_Users);

        } catch (IOException ex) {
            LOGGER.error("Failed to load tables", ex);
            return false;
        }

        //Create Users put
        Put putStereotypeOnUsers = new Put(Bytes.toBytes(userUID));
        //Create Stereotypes put
        Put putUserOnStereotypes = new Put(Bytes.toBytes(stereotypeUID));

        //Put stereotype name and stereotype UID on stereotypes CF at users table
        putStereotypeOnUsers.add(family_Stereotypes,
                Bytes.toBytes(stereotypeName),
                Bytes.toBytes(stereotypeUID));
        //Put user name and user UID on users CF at stereotype table
        putUserOnStereotypes.add(family_Users,
                Bytes.toBytes(username),
                Bytes.toBytes(userUID));

        try {
            //Put on table Stereotypes
            stereotypesTable.put(putUserOnStereotypes);
            //Put on table Users
            usersTable.put(putStereotypeOnUsers);

            //Flush Commits
            stereotypesTable.flushCommits();
            usersTable.flushCommits();

        } catch (InterruptedIOException | RetriesExhaustedWithDetailsException ex) {
            LOGGER.error("Fail to put user on stereotype", ex);
            return false;
        }

        try {
            //Close tables
            stereotypesTable.close();
            usersTable.close();
        } catch (IOException ex) {
            LOGGER.error("Fail to close Stereotypes and Users tables", ex);
            return false;
        }

        return true;
    }

    /**
     * Delete user from stereotype
     *
     * @param username The username
     * @param stereotypeName The stereotype name
     * @param clientName The client name
     * @return The status of this action
     */
    @Override
    public boolean deleteUserFromStereotype(String username, String stereotypeName, String clientName) {

        //Get user UID
        String userUID = getUserUID(username, clientName);
        //Check user if exist
        if (userUID == null) {
            LOGGER.error("User: " + username + " not exists");
            return false;
        }

        //Get stereotype UID
        String stereotypeUID = getStereotypeUID(stereotypeName, clientName);
        //Check stereotype if exist
        if (stereotypeUID == null) {
            LOGGER.error("Stereotype: " + stereotypeName + " not exists");
            return false;
        }

        HTable stereotypesTable = null;
        HTable usersTable = null;

        try {
            //create new Stereotypes HTable
            stereotypesTable = new HTable(config, table_Stereotypes);

            //create new Users HTable
            usersTable = new HTable(config, table_Users);

        } catch (IOException ex) {
            LOGGER.error("Failed to load tables", ex);
            return false;
        }

        //Create Users Delete
        Delete deleteStereotypeFromUsers = new Delete(Bytes.toBytes(userUID));
        //Create Stereotypes Delete
        Delete deleteUserFromStereotypes = new Delete(Bytes.toBytes(stereotypeUID));

        //Delete stereotype name and stereotype UID from users table
        deleteStereotypeFromUsers.deleteColumn(
                family_Stereotypes,
                Bytes.toBytes(stereotypeName));
        //Delete user name and user UID from stereotypes table
        deleteUserFromStereotypes.deleteColumn(
                family_Users,
                Bytes.toBytes(username));

        try {
            //Delete from table Stereotypes
            stereotypesTable.delete(deleteUserFromStereotypes);
            //Delete from table Users
            usersTable.delete(deleteStereotypeFromUsers);

            //Flush Commits
            stereotypesTable.flushCommits();
            usersTable.flushCommits();

        } catch (IOException ex) {
            LOGGER.error("Fail to delete user from stereotype", ex);
            return false;
        }

        try {
            //Close tables
            stereotypesTable.close();
            usersTable.close();
        } catch (IOException ex) {
            LOGGER.error("Fail to close Stereotypes and Users tables", ex);
            return false;
        }

        return true;
    }

    /**
     * Get the stereotype ID for the given stereotype name If not stereotype
     * exist on Storage return null
     *
     * @param username The username that i want the UUID
     * @return the UUID for the given username
     */
    private String getStereotypeUID(String stereotype, String clientName) {
        String sterID = null;
        String clientUID = getClientUID(clientName);
        try {

            HTable table = new HTable(config, table_Clients);
            Get get = new Get(Bytes.toBytes(clientUID));
            get.setFilter((new ColumnPrefixFilter(Bytes.toBytes(stereotype))));
            Result result = table.get(get);

            sterID = Bytes.toString(
                    result.getValue(family_Stereotypes, Bytes.toBytes(stereotype))
            );

        } catch (IOException ex) {
            LOGGER.error("IOException", ex);
        }
        return sterID;
    }

    /**
     * Generate new StereotypeID
     *
     * @param username The Stereotype name
     * @param clientName The client name
     * @return The StereotypeID
     */
    private String generateStereotypeUID(String stereotypeName, String clientName) {
        String UUID = null;
        String clientUID = getClientUID(clientName);

        //Create new StereotypeID
        UUID = Utilities.getUUID(clientUID + "-" + stereotypeName).toString();
        return UUID;
    }

    /**
     * Scan users table and find the users who satisfies the filter
     *
     * @param fl The given filter
     * @return A list with usernames
     */
    private List<String> getUsersFromFilter(FilterList givenFilterList,
            String clientName) {

        ArrayList<String> users = new ArrayList<>();
        //Create users table
        HTable table = null;
        try {
            table = new HTable(config, table_Users);
        } catch (IOException ex) {
            LOGGER.error("Fail to create users table", ex);
            return null;
        }

        Scan scan = new Scan();

        FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        //Add given filter list to scans filter list
        fl.addFilter(givenFilterList);
        //Add filter to get only users from the current client
        fl.addFilter(new SingleColumnValueFilter(
                family_Info,
                qualifier_Client,
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(getClientUID(clientName))
        ));
        scan.setFilter(fl);

        ResultScanner scanner;
        try {
            scanner = table.getScanner(scan);

            for (Result cResult : scanner) {
                String cName = Bytes.toString(
                        cResult.getValue(family_Info,
                                qualifier_Username)
                );

                users.add(cName);
            }
        } catch (IOException ex) {
            LOGGER.error("Can't scan table Users with the given filter.", ex);
            return null;
        }

        return users;
    }

    /**
     * Get the rule of the stereotype
     *
     * @param stereotypeUID The stereotype UID
     * @return The rule
     */
    private String getStereoypeRule(String stereotypeUID) {

        HTable table = null;

        try {
            table = new HTable(config, table_Stereotypes);
        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Stereotypes, ex);
            return null;
        }

        Get get = new Get(Bytes.toBytes(stereotypeUID));
        get.addFamily(family_Info);

        Result result = null;

        try {
            result = table.get(get);
        } catch (IOException ex) {
            LOGGER.error("Error on get from table", ex);
            return null;
        }

        if (result.isEmpty()) {
            return null;
        }

        return Bytes.toString(result.getValue(family_Info, qualifier_Rule));
    }

    //=================== Stereotype Mode =====================================
    //=================== Community Mode ======================================
    //=================== Community Mode ======================================
    //=================== Administration ======================================
    /**
     * Add the given client on HBase. If user exists update client.
     *
     * @param client A PServer Client
     * @return The status of this action
     */
    @Override
    public boolean addClient(Client client) {
        boolean status = true;
        HTable clientsTable = null;

        try {

            //create new Clients HTable
            clientsTable = new HTable(config, table_Clients);

        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Clients, ex);
            //return the status
            return false;
        }

        String clientUIDForAdd = getClientUID(client.getUsername());
        if (clientUIDForAdd == null) {
            LOGGER.error("Client UID is null");
            //return the status
            return false;
        }
        //Create put method with row key
        Put put = new Put(Bytes.toBytes(clientUIDForAdd));

        //add current client info
        for (String cInfo : client.getInfo().keySet()) {
            put.add(family_Info,
                    Bytes.toBytes(cInfo),
                    Bytes.toBytes(client.getInfo().get(cInfo)));
        }

        try {

            //add client record on Clients table
            clientsTable.put(put);
            //Flush Commits
            clientsTable.flushCommits();

        } catch (InterruptedIOException | RetriesExhaustedWithDetailsException ex) {
            LOGGER.error(null, ex);
            //return the status
            return false;
        }

        try {

            //close tables
            clientsTable.close();

        } catch (IOException ex) {
            LOGGER.error("Can't close table", ex);
            //return the status
            return false;
        }

        //return the status
        return status;
    }

    /**
     * Delete a client user form Storage
     *
     * @param clientName The client's username
     * @return The status of this action
     */
    @Override
    public boolean deleteClient(String clientName) {
        boolean status = true;
        HTable clientsTable = null;

        // Delete client users from Users table
        if (!deleteUsers(null, clientName)) {
            LOGGER.error("Fail to remove client users from Users table");
            //return the status
            return false;
        }

        try {
            // Create an hbase clients table
            clientsTable = new HTable(config, table_Clients);

        } catch (IOException ex) {
            LOGGER.error("Can't load table " + table_Clients, ex);
            //return the status
            return false;
        }

        //Add on delete the rowkey
        String clientUIDForDelete = getClientUID(clientName);

        if (clientUIDForDelete == null) {
            LOGGER.error("Client UID is null");
            //return the status
            return false;
        }

        Delete deleteClient = new Delete(Bytes.toBytes(clientUIDForDelete));

        try {

            //delete client user from the table Clients
            clientsTable.delete(deleteClient);

        } catch (IOException ex) {
            LOGGER.error("Can't delete client", ex);
            //return the status
            return false;
        }

        try {

            // flush the Commits
            clientsTable.flushCommits();
            // close the table
            clientsTable.close();

        } catch (IOException ex) {
            LOGGER.error("Can't flush commits or close table", ex);
            //return the status
            return false;
        }

        //return the status
        return status;
    }

    /**
     * Get a set with all clients usernames
     *
     * @return
     */
    @Override
    public Set<String> getClients() {

        //Initialize variables
        HashSet<String> clients = new HashSet<>();

        HTable table = null;
        try {

            //Create clients table
            table = new HTable(config, table_Clients);

            Scan scan = new Scan();

            ResultScanner scanner = table.getScanner(scan);

            for (Result cResult : scanner) {
                String cName = Bytes.toString(
                        cResult.getValue(family_Info, qualifier_Username)
                );
                String cUID = Bytes.toString(
                        cResult.getRow()
                );

                clients.add(cName);

            }

        } catch (IOException ex) {
            LOGGER.error("Error on get clients", ex);
        }

        return clients;
    }

    @Override
    public boolean setClientRoles(String clientName, String role) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getClientRoles(String clientName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * If client exists return the exist UID else generate new UID.
     *
     * @param clientName The client's name
     * @return The UID
     */
    private String getClientUID(String clientName) {
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
                UID = Utilities.getUUID("admin" + "-" + clientName).toString();
            } else {
                UID = Bytes.toString(
                        result.getRow()
                );
            }

        } catch (IOException ex) {
            LOGGER.error("Error on getClientUID", ex);
            return null;
        }
        return UID;
    }

    //=================== Administration ======================================
    //=================== Generic functions ======================================
    private Map<String, String> createFeatureCentroid(
            List<String> users, String clientName) {

        HashMap<String, String> stereotypeFeatures = new HashMap<>();
        HashMap<String, String> numericFeatures = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> alphaFeatures = new HashMap<>();

        //for each user get profile
        for (String cUser : users) {
            Map<String, String> userFeatures = new HashMap<>();
            userFeatures.putAll(getUserFeatures(cUser, null, null, clientName));

            //for each feature 
            for (String cFeature : userFeatures.keySet()) {
                //if feature is numeric
                if (userFeatures.get(cFeature).matches("[0-9]+")) {

                    //if feature contains to numeric stereotype list
                    if (numericFeatures.containsKey(cFeature)) {
                        //update map
                        int cValue = Integer.parseInt(numericFeatures.get(cFeature));
                        int addend = Integer.parseInt(userFeatures.get(cFeature));
                        numericFeatures.put(cFeature,
                                Integer.toString(cValue + addend));
                    } else {
                        //add feature to list
                        numericFeatures.put(cFeature, userFeatures.get(cFeature));
                    }
                } else {
                    //if feature contains to alphastereotype list
                    if (alphaFeatures.containsKey(cFeature)) {
                        HashMap<String, Integer> alphaFeature = new HashMap<>();
                        alphaFeature.putAll(alphaFeatures.get(cFeature));

                        //check if value contains on map
                        if (alphaFeature.containsKey(userFeatures.get(cFeature))) {
                            int oldValue = alphaFeature.get(userFeatures.get(cFeature));
                            alphaFeature.put(userFeatures.get(cFeature), oldValue++);
                        } else {
                            //if not add new value on map
                            alphaFeature.put(userFeatures.get(cFeature), 1);
                        }
                    } else {
                        HashMap<String, Integer> newAlphaFeature = new HashMap<>();
                        newAlphaFeature.put(userFeatures.get(cFeature), 1);
                        //add to list and counter map
                        alphaFeatures.put(cFeature, newAlphaFeature);
                    }
                }
            }
        }

        //for each numeric feature in the map divene the value with users size
        for (String cFeature : numericFeatures.keySet()) {
            //update map
            int cValue = Integer.parseInt(numericFeatures.get(cFeature));
            stereotypeFeatures.put(
                    cFeature, Integer.toString(cValue / users.size()));
        }

        //for each alphafeature add on feature list the alpha value with max value
        for (String cFeature : alphaFeatures.keySet()) {

            HashMap<String, Integer> alphaFeature = new HashMap<>();
            alphaFeature.putAll(alphaFeatures.get(cFeature));
            String value = "";
            int frequency = 0;
            for (String cAlphaFeatureValue : alphaFeature.keySet()) {
                int cFrequency = alphaFeature.get(cAlphaFeatureValue);
                if (frequency < cFrequency) {
                    frequency = cFrequency;
                    value = cAlphaFeatureValue;
                }
            }
            stereotypeFeatures.put(cFeature, value);
        }

        return stereotypeFeatures;
    }

    //=================== Generic functions ======================================
    //=================== Test functions ======================================
}
