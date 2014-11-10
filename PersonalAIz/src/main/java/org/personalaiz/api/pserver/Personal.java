/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.personalaiz.api.pserver;

import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * This class implements the Personal mode API
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
/**
 * Root resource (exposed at "pserver/:credentials/personal" path)
 */
@Path("{ClientKey}/personal")
@Produces(MediaType.APPLICATION_JSON)
public class Personal {

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
//    @GET
//    public String getIt(@PathParam("cr") String cr,@Context HttpHeaders hh, 
//            @DefaultValue("*") @QueryParam("c") String c) {
//         MultivaluedMap<String, String> headerParams = hh.getRequestHeaders();
//    Map<String, Cookie> pathParams = hh.getCookies();
//        System.out.println(pathParams);
//        return "{"
//                + "\"credentials\":\"" + cr+"\","
//                + "\"Pattern\":\"" + c+"\""
//                + "}";
//        
//    }
//    @Path("{c}")
//    @GET
//    public String convertCtoFfromInput(@PathParam("c") Double c) {
//        Double fahrenheit;
//        Double celsius = c;
//        fahrenheit = ((celsius * 9) / 5) + 32;
//
//        String result = "@Produces(\"application/xml\") Output: \n\nC to F Converter Output: \n\n" + fahrenheit;
//        return "<ctofservice>" + "<celsius>" + celsius + "</celsius>" + "<ctofoutput>" + result + "</ctofoutput>" + "</ctofservice>";
//    }
    
    
// TODO: Implement add users
//POST users/:JSONUsers | Add new users
    @Path("users/{JSONUsers}")
    @POST
    public String AddUsers() {

        return null;
    }

// TODO: Implement delete users
//DELETE users | Delete users
    @Path("users")
    @DELETE
    public String DeleteUsers() {

        return null;
    }

// TODO: Implement get users
//GET users | Get list with users
    @Path("users")
    @GET
    public String GetUsers() {

        return null;
    }

// TODO: Implement set users Attributes
//PUT users/:User/attributes/:JSONUserAttributes | Set user’s attributes
    @Path("users/{User}/attributes/{JSONUserAttributes}")
    @PUT
    public String SetUserAttributes() {

        return null;
    }

// TODO: Implement set users Features
//PUT users/:User/features/:JSONUserFeatures | Set user’s features
    @Path("users/{User}/features/{JSONUserFeatures}")
    @PUT
    public String SetUserFeatures() {

        return null;
    }

// TODO: Implement Modify users Features
//PUT users/:User/features/modify/:JSONUserFeatures | Modify (increase/decrease) user’s feature
    @Path("users/{User}/features/modify/{JSONUserFeatures}")
    @PUT
    public String ModifyUserFeatures() {

        return null;
    }

// TODO: Implement get user's Profile
//GET users/:User/features | Get user’s profile
    @Path("users/{User}/features")
    @GET
    public String GetUserProfile() {

        return null;
    }

// TODO: Implement get user's Attributes
//GET users/:User/attributes | Get user’s attributes
    @Path("users/{User}/attributes")
    @GET
    public String GetUserAttributes() {

        return null;
    }

// ===========================================   
// TODO: Implement add attributes
//POST attributes/:JSONAttributes | Add New Attributes
    @Path("attributes/{JSONAttributes}")
    @POST
    public String AddAttributes() {

        return null;
    }

// TODO: Implement delete attributes
//DELETE attributes | Delete Attributes from platform
    @Path("attributes")
    @DELETE
    public String DeleteAttributes() {

        return null;
    }

// TODO: Implement get attributes
//GET attributes | Get platform’s Attributes
    @Path("attributes")
    @GET
    public String GetAttributes() {

        return null;
    }

// TODO: Implement update attributes
//PUT attributes/:JSONAttributes | Update Attributes Default value
    @Path("attributes/{JSONAttributes}")
    @PUT
    public String UpdateAttributes() {

        return null;
    }

// ===========================================   
// TODO: Implement add Features
//POST features/:JSONFeatures | Add New Features
    @Path("features/{JSONFeatures}")
    @POST
    public String AddFeatures() {

        return null;
    }

// TODO: Implement delete Features
//DELETE features | Delete Features from platform
    @Path("features")
    @DELETE
    public String DeleteFeatures() {

        return null;
    }

// TODO: Implement get Features
//GET features | Get platform’s Features
    @Path("features")
    @GET
    public String GetFeatures() {

        return null;
    }

// TODO: Implement update Features
//PUT features/:JSONFeatures | Update Features Default value
    @Path("features/{JSONFeatures}")
    @PUT
    public String UpdateFeatures() {

        return null;
    }

}
