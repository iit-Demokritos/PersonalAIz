/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.authorization;

/**
 *
 * @author ggianna
 */
public class Action {
    private final String name;
    public Action(String sName) {
        this.name = sName;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
    
    
    
}
