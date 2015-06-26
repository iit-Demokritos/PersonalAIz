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
    public String addClient(String clientName, String password,  HashMap<String, String> info) {
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
        return JSon.jsonize(output, Output.class);
    }

    /**
     * Delete client with given UID
     * @param clientUID
     * @return 
     */
    public String deleteClient(String clientName) {
        //Initialize variables
        output = new Output();

        output.setOutputCode(dbAdmin.deleteClient(clientName));
        return JSon.jsonize(output, Output.class);
    }

    
    /**
     * Get all client names with their UID
     * @return A map with key - value pairs (ClinetName:UID)
     */
    public String getClients() {
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
    public String setClientRoles() {
       //TODO: implement
        //Initialize variables
        output = new Output();

        return JSon.jsonize(output, Output.class);
    }

    
    public String getSettings() {
        //Initialize variables
        output = new Output();

        return JSon.jsonize(output, Output.class);
    }

    public String setSettings() {
        //Initialize variables
        output = new Output();

        return JSon.jsonize(output, Output.class);
    }
    
//    public Client getClient(String username, String password) {
//        return new Client(username, password);
//    }
    
    
}
