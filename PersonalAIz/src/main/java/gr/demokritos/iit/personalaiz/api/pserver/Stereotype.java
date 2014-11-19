/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.personalaiz.api.pserver;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * This class implements the Stereotype mode API
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
/**
 * Root resource (exposed at "pserver/:credentials/stereotype" path)
 */
@Path("{ClientKey}/stereotype")
@Produces(MediaType.APPLICATION_JSON)
public class Stereotype {

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
  

// TODO: Implement Add Stereotypes
// POST /:SteretoypeName | Add new stereotype on the platform
    @Path("/{SteretoypeName}")
    @POST
    public String AddStereotype(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("SteretoypeName") String SteretoypeName,
            @DefaultValue("null") @QueryParam("rule") String rule
    ) {

     return null;
    }
    
// TODO: Implement Get stereotypes
// GET /  | Get a list with all stereotype names in the platform
    @GET
    public String GetStereotypes(
            @PathParam("ClientKey") String ClientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement Delete stereotypes
// DELETE /  | Delete stereotype from the platform. If no pattern remove all the stereotypes
    @DELETE
    public String DeleteStereotypes(
            @PathParam("ClientKey") String ClientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern
    ) {

     return null;
    }


// TODO: Implement Remake stereotype
// PUT /:StereotypeName  | Remake stereotype of the given stereotype Name
    @Path("{StereotypeName}")
    @PUT
    public String RemakeStereotype(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName
    ) {

     return null;
    }


// TODO: Implement Get stereotype users
// GET /:StereotypeName/users  | Get a list with users that belongs to the given stereotype name
    @Path("{StereotypeName}/users")
    @GET
    public String GetStereotypeUsers(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement Get stereotype profile
// GET /:StereotypeName/profile  | Get stereotype profile for the given stereotype name
    @Path("{StereotypeName}/profile")
    @GET
    public String GetStereotypeProfile(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
    

// ===========================================   
// TODO: Implement Add user on stereotype
// POST /users/:Username/:StereotypeNameObject | Add a user on a stereotype with association degree
    @Path("users/{Username}/{StereotypeNameObject}")
    @POST
    public String AddUserStereotype(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("Username") String Username,
            @PathParam("StereotypeNameObject") String StereotypeNameObject
    ) {

     return null;
    }

// TODO: Implement Get user’s stereotypes
// GET /users/:Username | Get a list with stereotypes that user belongs
    @Path("users/{Username}")
    @GET
    public String GetUserStereotypes(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("Username") String Username,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }

// TODO: Implement Set user’s stereotype degrees
// PUT /users/:UserName/:StereotypeNameObject | Set new association degree for a stereotype
    @Path("users/{UserName}/{StereotypeNameObject}")
    @PUT
    public String SetUserStereotypes(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("UserName") String UserName,
            @PathParam("StereotypeNameObject") String StereotypeNameObject
    ) {

     return null;
    }
    
// TODO: Implement Modify user’s stereotype degrees
// PUT /users/:UserName/modify/:StereotypeNameObject | Modify association degree (increase/decrease) for a stereotype
    @Path("users/{UserName}/modify/{StereotypeNameObject}")
    @PUT
    public String ModifyUserStereotypes(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("UserName") String UserName,
            @PathParam("StereotypeNameObject") String StereotypeNameObject
    ) {

     return null;
    }

// TODO: Implement Delete user from stereotype
// DELETE /users/:Username/:StereotypeNameObject | Delete user from stereotypes
    @Path("users/{Username}/{StereotypeNameObject}")
    @DELETE
    public String DeleteUserStereotype(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("Username") String Username,
            @PathParam("StereotypeNameObject") String StereotypeNameObject
    ) {

     return null;
    }
    
// TODO: Implement Update stereotype users
// PUT /users/:StereotypeName/update | Removes all users not matching the stereotypes rule and inserts any new users that do match
    @Path("users/{StereotypeName}/update")
    @PUT
    public String UpdateStereotypeUsers(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName
    ) {

     return null;
    }
    
// TODO: Implement Check stereotype users
// PUT /users/:StereotypeName/check | Removes any current users that do not match the stereotypes rule
    @Path("users/{StereotypeName}/check")
    @PUT
    public String CheckStereotypeUsers(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName
    ) {

     return null;
    }
    
// TODO: Implement Find stereotype users
// PUT /users/:StereotypeName/find | Finds and adds to the stereotype all users matching its rule
    @Path("users/{StereotypeName}/find")
    @PUT
    public String FindStereotypeUsers(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName
    ) {

     return null;
    }
    
    
    
// ===========================================   
// TODO: Implement Add features on stereotype
// POST /features/:StereotypeName/:FeatureNameObject | Add a list of feature names and value on a stereotype
    @Path("features/{StereotypeName}/{FeatureNameObject}")
    @POST
    public String AddFeatureStereotype(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName,
            @PathParam("FeatureNameObject") String FeatureNameObject
    ) {

     return null;
    }
    
// TODO: Implement Delete feature from stereotypes
// DELETE /features/:FeatureName/:StereotypeNameObject | Delete feature from stereotypes
    @Path("features/{FeatureName}/{StereotypeNameObject}")
    @DELETE
    public String DeleteFeatureStereotype(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("FeatureName") String FeatureName,
            @PathParam("StereotypeNameObject") String StereotypeNameObject
    ) {

     return null;
    }
    
// TODO: Implement Set features on stereotype
// PUT /features/:StereotypeName/:FeatureNameObject | set a list of feature names and value on a stereotype
    @Path("features/{StereotypeName}/{FeatureNameObject}")
    @PUT
    public String SetFeatureStereotypes(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName,
            @PathParam("FeatureNameObject") String FeatureNameObject
    ) {

     return null;
    }
    
// TODO: Implement Modify features on stereotype
// PUT /features/:StereotypeName/modify/:FeatureNameObject | Modify a list of feature names and value on a stereotype
    @Path("features/{StereotypeName}/modify/{FeatureNameObject}")
    @PUT
    public String ModifyFeatureStereotypes(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName,
            @PathParam("FeatureNameObject") String FeatureNameObject
    ) {

     return null;
    }
    
// TODO: Implement Update stereotype features
// PUT /features/:StereotypeName/update | Removes all features from the stereotype 
//    and then adds and sets all features as needed based on the stereotypes current users and their degrees
    @Path("features/{StereotypeName}/update")
    @PUT
    public String UpdateStereotypeFeatures(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("StereotypeName") String StereotypeName
    ) {

     return null;
    }



}
