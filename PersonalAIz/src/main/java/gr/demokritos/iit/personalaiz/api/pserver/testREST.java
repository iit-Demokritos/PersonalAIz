/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.api.pserver;

import gr.demokritos.iit.pserver.api.Admin;
import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import java.util.Date;
import java.util.HashMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
@Path("pserver/test/")
@Produces(MediaType.APPLICATION_JSON)
public class testREST {

    @Path("initserver")
    @GET
    public String getSystemAttributes() {

        String clientInfo = "{"
                + "\"mail\":\"info@mail.com\","
                + "\"info1\":\"info1value\""
                + "}";

        System.out.println("===============================================");
        Client cl = new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
        Admin admin = new Admin(new PServerHBase(), cl);
        HashMap<String, String> info = new HashMap<>();

        admin.addClient("root", "root", info);
        System.out.println("===============================================");

        return "ok";
    }

}
