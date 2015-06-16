/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.storage;

import gr.demokritos.iit.security.interfaces.ISecurity;
import java.util.List;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class HBase implements ISecurity{

    @Override
    public boolean checkClientCredentials(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
 
    
    
}
