/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.exceptions.DeserializationException;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class InitializePServer {

    public static void main(String[] args) {

        String table_Clients = "Clients";
        String table_Users = "Users";
        String table_Stereotypes = "Stereotypes";
        String table_APIKeys = "APIKeys";
        
        byte[] family_Info = Bytes.toBytes("Info");
        byte[] family_Attributes = Bytes.toBytes("Attributes");
        byte[] family_Features = Bytes.toBytes("Features");
        byte[] family_ClientUsers = Bytes.toBytes("ClientUsers");
        byte[] family_Stereotypes = Bytes.toBytes("Stereotypes");
        byte[] family_Users = Bytes.toBytes("Users");

        Configuration config = HBaseConfiguration.create();
        HBaseAdmin admin = null;
        try {
            admin = new HBaseAdmin(config);

            //-------------- Clients -------------------------------------------
            if (admin.tableExists(table_Clients)) {
                //Disable table Clients
                admin.disableTable(table_Clients);
                //Drop table Clients
                admin.deleteTable(table_Clients);
            }
            //Crete table Clients
            HTableDescriptor clients = 
                    new HTableDescriptor(
                            HTableDescriptor.parseFrom(
                                    Bytes.toBytes(table_Clients)
                            )
                        );
            //add column families
            clients.addFamily(HColumnDescriptor.parseFrom(family_ClientUsers));
            clients.addFamily(HColumnDescriptor.parseFrom(family_Stereotypes));
            clients.addFamily(HColumnDescriptor.parseFrom(family_Info));
            admin.createTable(clients);
            admin.enableTable(table_Clients);
            
            //-------------- Users ---------------------------------------------
            if (admin.tableExists(table_Users)) {
                //Disable table Users
                admin.disableTable(table_Users);
                //Drop table Users
                admin.deleteTable(table_Users);
            }
            //Crete table Users
            HTableDescriptor users = 
                    new HTableDescriptor(
                            HTableDescriptor.parseFrom(
                                    Bytes.toBytes(table_Users)
                            )
                        );
            //add column families
            users.addFamily(HColumnDescriptor.parseFrom(family_Attributes));
            users.addFamily(HColumnDescriptor.parseFrom(family_Features));
            users.addFamily(HColumnDescriptor.parseFrom(family_Stereotypes));
            users.addFamily(HColumnDescriptor.parseFrom(family_Info));
            admin.createTable(users);
            admin.enableTable(table_Users);

            //-------------- Stereotypes ---------------------------------------
            if (admin.tableExists(table_Stereotypes)) {
                //Disable table Stereotypes
                admin.disableTable(table_Stereotypes);
                //Drop table Stereotypes
                admin.deleteTable(table_Stereotypes);
            }
            //Crete table Stereotypes
            HTableDescriptor stereotypes = 
                    new HTableDescriptor(
                            HTableDescriptor.parseFrom(
                                    Bytes.toBytes(table_Stereotypes)
                            )
                        );
            //add column families
            stereotypes.addFamily(HColumnDescriptor.parseFrom(family_Features));
            stereotypes.addFamily(HColumnDescriptor.parseFrom(family_Users));
            stereotypes.addFamily(HColumnDescriptor.parseFrom(family_Info));
            admin.createTable(stereotypes);
            admin.enableTable(table_Stereotypes);
            
            //-------------- APIKeys -------------------------------------------
            if (admin.tableExists(table_APIKeys)) {
                //Disable table APIKeys
                admin.disableTable(table_APIKeys);
                //Drop table APIKeys
                admin.deleteTable(table_APIKeys);
            }
            //Crete table APIKeys
            HTableDescriptor apikeys = 
                    new HTableDescriptor(
                            HTableDescriptor.parseFrom(
                                    Bytes.toBytes(table_Stereotypes)
                            )
                        );
            //add column families
            apikeys.addFamily(HColumnDescriptor.parseFrom(family_Info));
            admin.createTable(apikeys);
            admin.enableTable(table_APIKeys);
        
        } catch (IOException | DeserializationException ex) {
            Logger.getLogger(InitializePServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
