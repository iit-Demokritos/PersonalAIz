/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.authentication;

/**
 *
 * @author ggianna
 */
public interface IAuthentication {
    public boolean checkCredentials(String username, String password);
}
