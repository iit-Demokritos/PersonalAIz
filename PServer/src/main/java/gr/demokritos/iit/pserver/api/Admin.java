/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.storage.interfaces.IAdminStorage;
import gr.demokritos.iit.pserver.utils.JSon;
import gr.demokritos.iit.pserver.utils.Output;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * This class implement the administration functionality of PServer.
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class Admin {

    private IAdminStorage dbAdmin;
    private Output output;

    public Admin(IAdminStorage dbAdmin) {
        this.dbAdmin = dbAdmin;
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

    public String addClient(String clientName, String password, String JSONClientInfo) {
        //Initialize variables
        output = new Output();
        //convert JSON with clients info as a HashMap
        HashMap<String, String> info = new HashMap<>(
                JSon.unjsonize(JSONClientInfo, HashMap.class));
        //Get Client UID
        String clientUID = dbAdmin.getClientUID(clientName);
        //encrypt password
        String clientPassword = DigestUtils.sha1Hex(password);

        Client client = new Client(clientUID);
        info.put("Username", clientName);
        info.put("Password", clientPassword);

        //Add info on client object 
        client.setInfo(info);

        output.setOutputCode(dbAdmin.addClient(client));
        return JSon.jsonize(output, Output.class);
    }

    public String deleteClient(String clientName) {
        //Initialize variables
        output = new Output();
        //Get Client UID
        String clientUID = dbAdmin.getClientUID(clientName);

        output.setOutputCode(dbAdmin.deleteClient(clientUID));
        return JSon.jsonize(output, Output.class);
    }

    public String getClients() {
        //Initialize variables
        output = new Output();
        ArrayList<String> clients = new ArrayList<>();

        //Call HBase to get Clients
        clients.addAll(dbAdmin.getClients().keySet());
        output.setOutput(clients);
        return JSon.jsonize(output, Output.class);
    }

    public String getClientUID(String clientName) {
        //Initialize variables
        output = new Output();
        //Call HBase to get Clients
        output.setOutput(dbAdmin.getClients().get(clientName));
        return JSon.jsonize(output, Output.class);
    }

    public String setClientRoles() {
        //Initialize variables
        output = new Output();

        return JSon.jsonize(output, Output.class);
    }

}
