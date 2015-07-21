/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.pserver;

import gr.demokritos.iit.pserver.api.Admin;
import gr.demokritos.iit.pserver.api.Personal;
import gr.demokritos.iit.pserver.ontologies.Client;
import gr.demokritos.iit.pserver.ontologies.User;
import gr.demokritos.iit.pserver.storage.PServerHBase;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import org.apache.hadoop.hbase.exceptions.DeserializationException;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class PServerTest {

    public static void main(String[] args) throws IOException, DeserializationException {

        String clientInfo = "{"
                + "\"mail\":\"info@mail.com\","
                + "\"info1\":\"info1value\""
                + "}";

        System.out.println("===============================================");
        Client cl =new Client("root", "root");
        cl.setAuthenticatedTimestamp(new Date().getTime());
         Admin admin= new Admin(new PServerHBase(),cl );
         HashMap<String, String> info = new HashMap<>();
         
         admin.addClient("root", "root", info);
//        String userJSON ="{\"attributes\":{\"gender\":\"male\",\"age\": \"18\"},\"features\": {\"ftr1\": \"34\",\"ftr3\": \"3\",\"ftr5\": \"4\"}}";
//        User user = new User("panagiotis");
//        user.userCreator(userJSON);
//        System.out.println(user.getFeatures());
//        System.out.println(user.getAttributes());
//        HBase db = new HBase("Admin");
//        PServerHBase db = new PServerHBase("1ec1caa1-3fd9-3afb-8933-79a17f82e7a8");
//        Admin ad =new Admin(db);
//        System.out.println(ad.addClient("pnewDlient", "123", clientInfo));
//        System.out.println(ad.getClients());
//        System.out.println(ad.deleteClient("newClient"));

//        Personal ps = new Personal(db);
//        System.out.println(ps.addUsers("{\"test1\":{\"attributes\":{\"gender\": \"male\",\"age\": \"18\"},\"features\": {\"ftr1\": \"34\",\"ftr33\": \"3\",\"ftr5\": \"4\"}}}"));
//        System.out.println(ps.addUsers("{\"testko\":{},\"testko2\":{}ZZ,\"testko3\":{}}"));
//        System.out.println(ps.getUsers(null,null));
//        System.out.println(ps.getUsers("newsumUI", null));
//        System.out.println(ps.getUserAttributes("user1UIDps",null, null));
//        System.out.println(ps.getUserProfile("user1",null, null));
//        System.out.println(ps.getUserProfile("test1",null, 2));
//        System.out.println(ps.deleteUsers(null));
//        System.out.println(ps.setUsersAttributes("{\"user1\":{\"gender\":\"male\"}}"));
//        System.out.println(ps.setUsersFeatures("{\"user1\":{\"category1\":\"top\",\"ftr56\":\"0\"}}"));
//        System.out.println(ps.modifyUsersFeatures("{\"user1\":{\"ftr1\":\"5\"}}"));
        System.out.println("===============================================");
    }

}
