/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.storage;

import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.interfaces.ISecurityStorage;
import gr.demokritos.iit.security.ontologies.SystemUser;
import java.io.IOException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.LoggerFactory;

/**
 * This class implement the Security Apache HBase storage system.
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class SecurityHBase implements ISecurityStorage {

    //=================== HBase tables ========================================
    private final String table_Clients = "Clients";
    private final String table_APIKeys = "APIKeys";
    //=================== HBase Families ======================================
    private final byte[] family_Info = Bytes.toBytes("Info");
    private final byte[] family_Attributes = Bytes.toBytes("Attributes");
    private final byte[] family_Features = Bytes.toBytes("Features");
    private final byte[] family_ClientUsers = Bytes.toBytes("ClientUsers");
    private final byte[] family_Keys = Bytes.toBytes("Keys");

    //=================== HBase Families ======================================
    //=================== HBase Qualifiers ====================================
    private final byte[] qualifier_Client = Bytes.toBytes("Client");
    private final byte[] qualifier_Username = Bytes.toBytes("Username");
    private final byte[] qualifier_Password = Bytes.toBytes("Password");
    //=================== HBase Qualifiers ====================================

    private final Configuration config;
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SecurityHBase.class);

    /**
     * The constructor of Security HBase storage system.
     */
    public SecurityHBase() {
        //Create new HBase configuration
        config = HBaseConfiguration.create();
        //FIXME: enable remote HBase before deployed build
//        config.set("hbase.master", "master:60000");
//        config.set("hbase.zookeeper.quorum", "master"); 
//        config.set("hbase.zookeeper.property.clientPort", "2181");
    }

    //========================== Authentication ================================
    /**
     * Check credentials from HBase via username and password
     *
     * @param username Username
     * @param password Password
     * @return The status of the checking
     */
    @Override
    public boolean checkCredentials(String username, String password) {
        //Initialize variables
        boolean access = false;

        //Initialize variables
        HTable table = null;
        try {

            //Create clients table
            table = new HTable(config, table_Clients);

            Scan scan = new Scan();

            ResultScanner scanner = table.getScanner(scan);

            for (org.apache.hadoop.hbase.client.Result cResult : scanner) {
                String cName = Bytes.toString(
                        cResult.getValue(family_Info, qualifier_Username)
                );

                if (cName.equals(username)) {
                    String cPassword = Bytes.toString(
                            cResult.getValue(family_Info, qualifier_Password)
                    );

                    if (cPassword.equals(DigestUtils.sha1Hex(password))) {
                        access = true;
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error on checkCredentials", ex);
        }
        return access;
    }

    /**
     * Check credentials from HBase via apikey
     *
     * @param apikey The apikey
     * @return The status of the checking
     */
    @Override
    public boolean checkCredentials(String apikey) {

        //Initialize variables
        boolean access = false;

        //Initialize variables
        HTable table = null;
        try {

            //Create clients table
            table = new HTable(config, table_APIKeys);

            Scan scan = new Scan();

            ResultScanner scanner = table.getScanner(scan);

            for (org.apache.hadoop.hbase.client.Result cResult : scanner) {
                String cAPIKey = Bytes.toString(
                        cResult.getRow()
                );

                if (cAPIKey.equals(apikey)) {
                    access = true;
                    break;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error on checkCredentials", ex);
        }

        return access;
    }

    //========================== Authorization =================================
    /**
     * Check the Access permission from HBase
     *
     * @param u The system user
     * @param a The Action for checking
     * @param Access The Access type
     * @return The permission status
     */
    @Override
    public boolean checkAccess(SystemUser u, Action a, String Access) {

        //FIXME: checkAccess Implement read from HBase and return the status
        return true;
    }

}
