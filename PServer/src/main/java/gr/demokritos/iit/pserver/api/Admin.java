/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.storage.interfaces.IAdminStorage;
import gr.demokritos.iit.pserver.utils.JSon;
import gr.demokritos.iit.pserver.utils.Output;

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

    public String addClient() {
        //Initialize variables
        output = new Output();

        return JSon.jsonize(output, Output.class);
    }

    public String deleteClient() {
        //Initialize variables
        output = new Output();

        return JSon.jsonize(output, Output.class);
    }

    public String getClients() {
        //Initialize variables
        output = new Output();

        return JSon.jsonize(output, Output.class);
    }

    public String setClientRoles() {
        //Initialize variables
        output = new Output();

        return JSon.jsonize(output, Output.class);
    }

}
