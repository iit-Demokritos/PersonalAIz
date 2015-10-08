/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.utilities.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class RecommendationConfiguration extends Configuration {

    public static String RECOMMENDATIon_PROPERTIES = "Recommendation.properties";

    public RecommendationConfiguration(String configurationFileName) {
        super(configurationFileName);
    }
    
    public RecommendationConfiguration(){
        super(RECOMMENDATIon_PROPERTIES);
    }
    
    
    
    /**
     * Get logging level
     *
     * @return Return the default level (Info) or (Debug) if debug mode is ON
     */
    public String getLogLevel() {
        //Default logging level is Info
        //Return the logging level
        return properties.getProperty("LogLevel", "info");
    }
    
    /**
     * Get object weights
     * @return 
     */
    public Map<String,Double> getObjectWeights(){
        
        HashMap<String,Double> weights = new HashMap<>();
        
        weights.put("TextWeight",Double.parseDouble(
                super.properties.getProperty("TextWeight", "1")));
        weights.put("CategoryWeight",Double.parseDouble(
                super.properties.getProperty("CategoryWeight", "1")));
        weights.put("TagWeight",Double.parseDouble(
                super.properties.getProperty("TagWeight", "1")));
        weights.put("BooleanWeight",Double.parseDouble(
                super.properties.getProperty("BooleanWeight", "1")));
        
        return weights;
    }
    
    
    /**
     * Get PServer mode weights
     * @return 
     */
    public Map<String,Double> getPServerModesWeights(){
        
        HashMap<String,Double> weights = new HashMap<>();
        
        weights.put("PersonalWeight",Double.parseDouble(
                super.properties.getProperty("PersonalWeight", "1")));
        weights.put("StereotypeWeight",Double.parseDouble(
                super.properties.getProperty("StereotypeWeight", "1")));
        weights.put("CommunityWeight",Double.parseDouble(
                super.properties.getProperty("CommunityWeight", "1")));
        
        return weights;
    }

}
