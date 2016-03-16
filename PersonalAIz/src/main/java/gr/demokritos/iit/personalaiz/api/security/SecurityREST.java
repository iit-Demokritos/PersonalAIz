/* 
 * Copyright 2016 IIT , NCSR Demokritos - http://www.iit.demokritos.gr,
 *                      SciFY NPO http://www.scify.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Security REST API
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
@Path("security/")
@Produces(MediaType.APPLICATION_JSON)
public class SecurityREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityREST.class);
    //TODO: Change HBase with something global to change storage from settings
    private final SecurityLayer security = new SecurityLayer();
    private final Output output = new Output();
    private final PersonalAIzConfiguration config = new PersonalAIzConfiguration();

    /**
     * Check if is valid the given credentials
     *
     * @param username The username
     * @param password The password
     * @return
     */
    @Path("check/credentials")
    @POST
    public String checkCredentials(
            @FormParam("username") String username,
            @FormParam("password") String password) {

        //Update logging level 
        Logging.updateLoggerLevel(SecurityREST.class, config.getLogLevel());

        output.setOutput(security.authe.checkCredentials(username, password));

        return JSon.jsonize(output, Output.class);
    }
}
