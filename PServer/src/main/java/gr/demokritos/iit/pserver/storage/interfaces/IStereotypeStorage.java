/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.storage.interfaces;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface IStereotypeStorage {
    
    boolean addStereotype(String stereotypeName, String rule, String clientName);
    boolean deleteStereotypes(String pattern, String clientName);
    Map<String,String> getStereotypes(String pattern, Integer page, String clientName);
    
    
    
    boolean remakeStereotype(String stereotypeName, String clientName);
    boolean updateStereotypeFeatures(String stereotypeName, String clientName);
    boolean updateStereotypeUsers(String stereotypeName, String clientName);
    boolean findStereotypeUsers(String stereotypeName, String clientName);
    boolean checkStereotypeUsers(String stereotypeName, String clientName);
    
    
    
    boolean setStereotypeFeatures(String stereotypeName,
            Map<String,String> features, String clientName);
    boolean modifyStereotypeFeatures(String stereotypeName,
            Map<String,String> features, String clientName);
    Map<String,String> getStereotypeFeatures(String stereotypeName,
            String pattern, Integer page, String clientName);
    boolean deleteStereotypeFeatures(String stereotypeName, 
            String pattern, String clientName);
    
    
    
    List<String> getStereotypeUsers(String stereotypeName, String pattern, 
            Integer page, String clientName);
    List<String> getUserStereotypes(String username, String pattern, 
            Integer page, String clientName);
    boolean addUserOnStereotype(String username, String stereotypeName, 
            String clientName);
    boolean DeleteUserFromStereotype(String username, String stereotypeName, 
            String clientName);
    
}
