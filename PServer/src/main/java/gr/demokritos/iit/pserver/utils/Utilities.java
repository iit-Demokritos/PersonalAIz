/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver.utils;

import java.util.HashMap;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class Utilities {
    
    private static HashMap<Integer,String> codeMessageMap = new HashMap<Integer,String>();

    public Utilities() {
        
        codeMessageMap.put(100, "Process complete");
        codeMessageMap.put(200, "Error 1");
        codeMessageMap.put(300, "Error 2");
        codeMessageMap.put(400, "Error 3");
        
        
    }
    
    public static String getMessageCode(Integer code){
        return codeMessageMap.get(code);
    }
    
    
}
