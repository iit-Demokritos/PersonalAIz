/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.interfaces;

import gr.demokritos.iit.security.authentication.IAuthentication;
import gr.demokritos.iit.security.authorization.IAuthorization;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public abstract class ISecurity implements IAuthentication, IAuthorization {

    public ISecurity() {
    }
    
    
}
