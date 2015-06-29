/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.storage.interfaces.IAdminStorage;
import gr.demokritos.iit.utilities.configuration.PServerConfiguration;
import gr.demokritos.iit.utilities.json.JSon;
import gr.demokritos.iit.utilities.json.Output;
import gr.demokritos.iit.utilities.logging.Logging;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implement the administration functionality of PServer.
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class Admin {

    private IAdminStorage dbAdmin;
    private Output output;
    public static final Logger LOGGER = LoggerFactory.getLogger(Admin.class);
    private final PServerConfiguration psConfig;
    private final Client adminClient;

    public Admin(IAdminStorage dbAdmin, Client adminClient) {
        this.psConfig = new PServerConfiguration();
        this.dbAdmin = dbAdmin;
        this.adminClient = adminClient;
        //Update logging level 
        Logging.updateLoggerLevel(Admin.class, psConfig.getLogLevel());
    }


    
    /**
     * Add a client in database
     * @param clientName The clients Username
     * @param password The clients password
     * @param info A map with clients information. Key - value pairs (e.g. mail:info@mail.com)
     * @return A JSon if add complete. {SuccessCode"",message:""}
     */
    public boolean addClient(String clientName, String password,  HashMap<String, String> info) {
        //Initialize variables
        output = new Output();
        //Get Client UID
//        String clientUID = dbAdmin.getClientUID(clientName);
        
        //encrypt password
        String clientPassword = DigestUtils.sha1Hex(password);

        Client client = new Client(clientName, password);
        info.put("Username", clientName);
        info.put("Password", clientPassword);

        //Add info on client object 
        client.setInfo(info);

        output.setOutputCode(dbAdmin.addClient(client));
//        return JSon.jsonize(output, Output.class);
        //TODO: change with true return
        return true;
    }

    /**
     * Delete client with given UID
     * @param clientName
     * @return 
     */
    public boolean deleteClient(String clientName) {
        //Initialize variables
        output = new Output();

        output.setOutputCode(dbAdmin.deleteClient(clientName));
        
        
        //TODO: change with true return
        return true;
    }

    
    /**
     * Get all client names with their UID
     * @return A map with key - value pairs (ClinetName:UID)
     */
    public String getClients(){
        //Initialize variables
        output = new Output();
        ArrayList<String> clients = new ArrayList<>();

        //Call HBase to get Clients
        clients.addAll(dbAdmin.getClients().keySet());
        output.setOutput(clients);
        return JSon.jsonize(output, Output.class);
    }


    
    /**
     * 
     * @return 
     */
    public boolean setClientRoles() {
       //TODO: implement
        //Initialize variables
        output = new Output();

        
        //TODO: change with true return
        return true;
    }

    
    public Map<String,String>  getSettings() {
        HashMap<String,String> settings = new HashMap<>();
        //Initialize variables
        output = new Output();

        return settings;
    }

    public boolean setSettings(HashMap<String,String> settings) {
        //Initialize variables
        output = new Output();

        //TODO: change with true return
        return true;
    }
    
//    public Client getClient(String username, String password) {
//        return new Client(username, password);
//    }
    
    
}
