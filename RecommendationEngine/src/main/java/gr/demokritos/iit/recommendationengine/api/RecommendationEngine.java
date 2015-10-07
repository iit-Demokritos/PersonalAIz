/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.api;

import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.security.SecurityLayer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class RecommendationEngine {

    private final Client psClient;
    public static final Logger LOGGER = LoggerFactory.getLogger(RecommendationEngine.class);
    private SecurityLayer security = new SecurityLayer();

    public RecommendationEngine(Client psClient) {

        this.psClient = psClient;

    }

    /**
     * Set security control for user authorization
     *
     * @param security
     */
    public void setSecurity(SecurityLayer security) {
        this.security = security;
    }
    
    
    
    public boolean feed(){
        
        
        return false;
    }
    
    
    public Map<String,Integer> getRecommendation(){
        
        
        return null;
    }
 
    
    private void objectHandler(){
        
        
        
        
        
    }
    
    
    
    

}
