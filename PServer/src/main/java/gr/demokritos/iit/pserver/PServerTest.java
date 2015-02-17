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

//        Personal ps = new Personal("testUID");
        Personal ps = new Personal("testUID2");
//        System.out.println(ps.addUsers("{\"test1\":{\"attributes\":{\"gender\": \"male\",\"age\": \"18\"},\"features\": {\"ftr1\": \"34\",\"ftr3\": \"3\",\"ftr5\": \"4\"}}}"));
//        System.out.println(ps.addUsers("{\"testko\":{},\"testko2\":{},\"testko3\":{}}"));
//        System.out.println(ps.getUsers(null, null));
//        System.out.println(ps.getUserAttributes("test1",null, null));
//        System.out.println(ps.getUserProfile("test1",null, null));
//        System.out.println(ps.deleteUsers(null));
        System.out.println("===============================================");
    }

}
