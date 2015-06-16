/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.interfaces;

import java.util.List;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public interface ISecurity {
    
    
        //check from DB the credentials
        boolean checkClientCredentials(String username, String password);
   
        
        
    
    
    
    
}
