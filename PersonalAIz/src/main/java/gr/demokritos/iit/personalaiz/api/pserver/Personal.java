/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.api.pserver;

import static gr.demokritos.iit.personalaiz.storage.HBase.getRow;
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
@Path("{clientKey}/personal")
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
    public String addUsers(
            @PathParam("clientKey") String clientKey,
            @PathParam("JSONUsers") String JSONUsers
    ) {

        return null;
    }

// TODO: Implement delete users
//DELETE users | Delete users
    @Path("users")
    @DELETE
    public String deleteUsers(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern
    ) {

        return null;
    }

// TODO: Implement get users
//GET users | Get list with users
    @Path("users")
    @GET
    public String getUsers(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {
        getRow();
        return null;
    }

// TODO: Implement set users Attributes
//PUT users/:User/attributes/:JSONUserAttributes | Set user’s attributes
    @Path("users/{User}/attributes/{JSONUserAttributes}")
    @PUT
    public String setUserAttributes(
            @PathParam("clientKey") String clientKey,
            @PathParam("user") String user,
            @PathParam("JSONUserAttributes") String JSONUserAttributes
    ) {

        return null;
    }

// TODO: Implement set users Features
//PUT users/:User/features/:JSONUserFeatures | Set user’s features
    @Path("users/{User}/features/{JSONUserFeatures}")
    @PUT
    public String setUserFeatures(
            @PathParam("clientKey") String clientKey,
            @PathParam("user") String user,
            @PathParam("JSONUserFeatures") String JSONUserFeatures
    ) {

        return null;
    }

// TODO: Implement Modify users Features
//PUT users/:User/features/modify/:JSONUserFeatures | Modify (increase/decrease) user’s feature
    @Path("users/{User}/features/modify/{JSONUserFeatures}")
    @PUT
    public String modifyUserFeatures(
            @PathParam("clientKey") String clientKey,
            @PathParam("user") String user,
            @PathParam("JSONUserFeatures") String JSONUserFeatures
    ) {

        return null;
    }

// TODO: Implement get user's Profile
//GET users/:User/features | Get user’s profile
    @Path("users/{User}/features")
    @GET
    public String getUserProfile(
            @PathParam("clientKey") String clientKey,
            @PathParam("user") String user,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

        return null;
    }

// TODO: Implement get user's Attributes
//GET users/:User/attributes | Get user’s attributes
    @Path("users/{User}/attributes")
    @GET
    public String getUserAttributes(
            @PathParam("clientKey") String clientKey,
            @PathParam("user") String user,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

        return null;
    }

// ===========================================   
// TODO: Implement add attributes
//POST attributes/:JSONAttributes | Add New Attributes
    @Path("attributes/{JSONAttributes}")
    @POST
    public String addAttributes(
            @PathParam("clientKey") String clientKey,
            @PathParam("JSONAttributes") String JSONAttributes
    ) {

        return null;
    }

// TODO: Implement delete attributes
//DELETE attributes | Delete Attributes from platform
    @Path("attributes")
    @DELETE
    public String deleteAttributes(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern
    ) {

        return null;
    }

// TODO: Implement get attributes
//GET attributes | Get platform’s Attributes
    @Path("attributes")
    @GET
    public String getAttributes(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

        return null;
    }

// TODO: Implement update attributes
//PUT attributes/:JSONAttributes | Update Attributes Default value
    @Path("attributes/{JSONAttributes}")
    @PUT
    public String updateAttributes(
            @PathParam("clientKey") String clientKey,
            @PathParam("JSONAttributes") String JSONAttributes
    ) {

        return null;
    }

// ===========================================   
// TODO: Implement add Features
//POST features/:JSONFeatures | Add New Features
    @Path("features/{JSONFeatures}")
    @POST
    public String addFeatures(
            @PathParam("clientKey") String clientKey,
            @PathParam("JSONFeatures") String JSONFeatures
    ) {

        return null;
    }

// TODO: Implement delete Features
//DELETE features | Delete Features from platform
    @Path("features")
    @DELETE
    public String deleteFeatures(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern
    ) {

        return null;
    }

// TODO: Implement get Features
//GET features | Get platform’s Features
    @Path("features")
    @GET
    public String getFeatures(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

        return null;
    }

// TODO: Implement update Features
//PUT features/:JSONFeatures | Update Features Default value
    @Path("features/{JSONFeatures}")
    @PUT
    public String updateFeatures(
            @PathParam("clientKey") String clientKey,
            @PathParam("JSONFeatures") String JSONFeatures
    ) {

        return null;
    }

}
