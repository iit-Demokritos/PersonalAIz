/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.api.security;

import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.utilities.configuration.PersonalAIzConfiguration;
import gr.demokritos.iit.utilities.json.JSon;
import gr.demokritos.iit.utilities.json.Output;
import gr.demokritos.iit.utilities.logging.Logging;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
@Path("security/{userAuthe}/")
@Produces(MediaType.APPLICATION_JSON)
public class SecurityREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityREST.class);
    //TODO: Change HBase with something global to change storage from settings
    private final SecurityLayer security = new SecurityLayer();
    private final Output output = new Output();
    private final PersonalAIzConfiguration config = new PersonalAIzConfiguration();

    @Path("check/credentials")
    @POST
    public String checkCredentials(
            @PathParam("userAuthe") String userAuthe,
            @FormParam("username") String username,
            @FormParam("password") String password) {

        //Check if user Authentication is with username pass or api key
        if (userAuthe.contains("|")) {
            // Check the username - pass Credentials
            String[] credentials = userAuthe.split("\\|");

            if (!security.authe.checkCredentials(credentials[0], credentials[1])) {
                LOGGER.info("No valid Username: " + credentials[0]
                        + " and pass: " + credentials[1]);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
        } else {
            // Check the api key Credentials
            if (!security.authe.checkCredentials(userAuthe)) {
                LOGGER.info("No valid API Key: " + userAuthe);
                output.setCustomOutputMessage("Security Authentication Failed");
                return JSon.jsonize(output, Output.class);
            }
        }

        //Update logging level 
        Logging.updateLoggerLevel(SecurityREST.class, config.getLogLevel());

        output.setOutput(security.authe.checkCredentials(username, password));

        return JSon.jsonize(output, Output.class);
    }
}
