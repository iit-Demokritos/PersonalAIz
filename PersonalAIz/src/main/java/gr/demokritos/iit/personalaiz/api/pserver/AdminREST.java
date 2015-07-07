/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.api.pserver;

import gr.demokritos.iit.pserver.api.Admin;
import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import gr.demokritos.iit.pserver.storage.interfaces.IAdminStorage;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.utilities.json.JSon;
import gr.demokritos.iit.utilities.json.Output;
import java.util.HashMap;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
@Path("{userKey}/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminREST.class);
    private final PServerHBase db = new PServerHBase();
    private Admin admin;
    private final SecurityLayer security = new SecurityLayer();
    private Output output;

    /**
     *
     * @param userKey
     * @param clientName
     * @param clientPass
     * @param clientInfo
     * @return
     */
    @Path("client/{clientName}")
    @POST
    public String addClient(
            @PathParam("userKey") String userKey,
            @PathParam("clientName") String clientName,
            @QueryParam("password") String clientPass,
            @QueryParam("info") String clientInfo
    ) {

        // Check the api key Credentials
        if (!security.authe.checkCredentials(userKey)) {
            //TODO: return JSON    
            return null;
        }

        // Create PServer Admin instance
        admin = new Admin(db, new Client(userKey));

        //set security layer on PServer Admin
        admin.setSecurity(security);
        
        
        
        HashMap<String, String> info = new HashMap<String, String>();
        if (clientInfo != null) {
            info.putAll(JSon.unjsonize(clientInfo, HashMap.class));
        }

        admin.addClient(clientName, clientPass, info);

        
        
        return JSon.jsonize(output, Output.class);
    }

    /**
     *
     * @param userKey
     * @param clientName
     * @return
     */
    @Path("client/{clientName}")
    @DELETE
    public String deleteClient(
            @PathParam("userKey") String userKey,
            @DefaultValue("*") @PathParam("clientName") String clientName
    ) {

        if (!security.authe.checkCredentials(userKey)) {
            //TODO: return JSON    
            return null;
        }

        // Create PServer Admin instance
        admin = new Admin(db, new Client(userKey));

        //set security layer on PServer Admin
        admin.setSecurity(security);
        
        
        
        
        return JSon.jsonize(output, Output.class);
    }

    /**
     *
     * @param userKey
     * @param pattern
     * @param page
     * @return
     */
    @Path("clients")
    @GET
    public String getClients(
            @PathParam("userKey") String userKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {
        if (!security.authe.checkCredentials(userKey)) {
            //TODO: return JSON    
            return null;
        }
        // Create PServer Admin instance
        admin = new Admin(db, new Client(userKey));

        //set security layer on PServer Admin
        admin.setSecurity(security);
        
        
        
        
        return JSon.jsonize(output, Output.class);
    }

    /**
     *
     * @param userKey
     * @return
     */
    @Path("settings")
    @POST
    public String setSettings(
            @PathParam("userKey") String userKey
    ) {
        if (!security.authe.checkCredentials(userKey)) {
            //TODO: return JSON    
            return null;
        }
        // Create PServer Admin instance
        admin = new Admin(db, new Client(userKey));

        //set security layer on PServer Admin
        admin.setSecurity(security);
        
        
        
        return JSon.jsonize(output, Output.class);
    }

    /**
     *
     * @param userKey
     * @return
     */
    @Path("settings")
    @GET
    public String getSettings(
            @PathParam("userKey") String userKey
    ) {

        if (!security.authe.checkCredentials(userKey)) {
            //TODO: return JSON    
            return null;
        }
        // Create PServer Admin instance
        admin = new Admin(db, new Client(userKey));

        
        //set security layer on PServer Admin
        admin.setSecurity(security);
        
        
        return JSon.jsonize(output, Output.class);
    }

}