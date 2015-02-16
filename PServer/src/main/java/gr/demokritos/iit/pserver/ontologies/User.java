/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.ontologies;

import java.util.HashMap;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class User {
    
    private String rowKey;
    private HashMap<String,String> info = new HashMap<String,String>();
    private HashMap<String,String> attributes = new HashMap<String,String>();
    private HashMap<String,String> features = new HashMap<String,String>();

    
    
    
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setInfo(HashMap<String, String> info) {
        this.info = info;
    }
    
    public HashMap<String, String> getInfo() {
        return info;
    }
    
    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }
    
    public HashMap<String, String> getAttributes() {
        return attributes;
    }
    
    public void setFeatures(HashMap<String, String> features) {
        this.features = features;
    }

    public HashMap<String, String> getFeatures() {
        return features;
    }
    
    
    
}
