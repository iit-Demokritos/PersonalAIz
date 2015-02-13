/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver;

import gr.demokritos.iit.pserver.api.Personal;
import java.io.IOException;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class PServerTest {
    
    public static void main(String[] args) throws IOException {

        System.out.println("===============================================");
        
        Personal ps = new Personal("Zadsds");
        ps.addUsers("[\"testuser1\",\"testuser2\",\"testuser3\",\"testuser4\"]");
        System.out.println(ps.getUsers(null, null));
        System.out.println("===============================================");
    }
    
    
    
    
    
}
