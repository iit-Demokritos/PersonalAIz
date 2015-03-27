/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver;

import gr.demokritos.iit.pserver.api.Personal;
import java.io.IOException;
import org.apache.hadoop.hbase.exceptions.DeserializationException;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class PServerTest {

    public static void main(String[] args) throws IOException, DeserializationException {

        System.out.println("===============================================");

//        Personal ps = new Personal("testUID");
//        Personal ps = new Personal("testUID2");
        Personal ps = new Personal("PSUID");
//        System.out.println(ps.addUsers("{\"test1\":{\"attributes\":{\"gender\": \"male\",\"age\": \"18\"},\"features\": {\"ftr1\": \"34\",\"ftr33\": \"3\",\"ftr5\": \"4\"}}}"));
//        System.out.println(ps.addUsers("{\"testko\":{},\"testko2\":{},\"testko3\":{}}"));
//        System.out.println(ps.getUsers(null,null));
        System.out.println(ps.getUsers("newsumUI", null));
//        System.out.println(ps.getUserAttributes("user1UIDps",null, null));
//        System.out.println(ps.getUserProfile("user1",null, null));
//        System.out.println(ps.getUserProfile("test1",null, 2));
//        System.out.println(ps.deleteUsers("user1"));
//        System.out.println(ps.setUsersAttributes("{\"user1\":{\"gender\":\"male\"}}"));
//        System.out.println(ps.setUsersFeatures("{\"user1\":{\"category1\":\"top\",\"ftr56\":\"0\"}}"));
//        System.out.println(ps.modifyUsersFeatures("{\"user1\":{\"ftr1\":\"5\"}}"));
        System.out.println("===============================================");
    }

}
