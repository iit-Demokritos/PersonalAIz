/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.storage.interfaces.IStereotypeStorage;
import gr.demokritos.iit.security.SecurityLayer;
import gr.demokritos.iit.security.authorization.Action;
import gr.demokritos.iit.security.authorization.Actions;
import gr.demokritos.iit.utilities.configuration.PServerConfiguration;
import gr.demokritos.iit.utilities.logging.Logging;
import java.util.Date;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This class implement the Stereotype mode of PServer. It supports the
 * stereotype user model. In this mode PS create groups of users with common
 * demographic user information (attributes)
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class Stereotype {

    private final IStereotypeStorage dbStereotype;
    private final PServerConfiguration psConfig;
    private final Client psClient;
    public static final Logger LOGGER = LoggerFactory.getLogger(Stereotype.class);
    private SecurityLayer security = new SecurityLayer();
    private final HashMap<String, Action> actions = new HashMap<>(new Actions().getStereotypeActions());

    public Stereotype(IStereotypeStorage dbStereotype, Client psClient) {
        this.psConfig = new PServerConfiguration();
        this.dbStereotype = dbStereotype;
        this.psClient = psClient;

        //Update logging level 
        Logging.updateLoggerLevel(Personal.class, psConfig.getLogLevel());
    }

    /**
     * Set security control for user authorization
     *
     * @param security
     */
    public void setSecurity(SecurityLayer security) {
        this.security = security;
    }

    //Add stereotype
    //get sterotypes
    //delete sterotypes
    //get stereotype users
    //get stereotype profile
    
    
    
    
    
    
    /**
     * Get the permission for the given action and client
     *
     * @param a The action that we want to check the permission
     * @param sAccessType The access type R (read) - W (write) - X (execute)
     * @return A true or false if the permission granted
     */
    public boolean getPermissionFor(Action a, String sAccessType) {

        return ((security != null)
                && (security.autho.getAccessRights(psClient, a).get(sAccessType))
                && (psClient.authenticatedTimestamp != 0));
    }

}
