/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.onologies;

import java.util.Map;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class RecommendationObject extends FeedObject{

    private double score;
    private Map<String, Integer> features;
    
    public RecommendationObject(String objectID, 
            String lang, 
            boolean recommended, 
            long timestamp) {
        super(objectID, lang, recommended, timestamp);
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public Map<String, Integer> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Integer> features) {
        this.features = features;
    }
    
    
    
}
