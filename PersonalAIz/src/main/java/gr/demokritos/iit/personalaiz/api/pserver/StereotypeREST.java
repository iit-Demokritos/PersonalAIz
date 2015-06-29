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
@Path("{clientKey}/stereotype")
@Produces(MediaType.APPLICATION_JSON)
public class StereotypeREST {

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
  

// TODO: Implement Add Stereotypes
// POST /:steretoypeName | Add new stereotype on the platform
    @Path("/{steretoypeName}")
    @POST
    public String addStereotype(
            @PathParam("clientKey") String clientKey,
            @PathParam("steretoypeName") String steretoypeName,
            @DefaultValue("null") @QueryParam("rule") String rule
    ) {

     return null;
    }
    
// TODO: Implement Get stereotypes
// GET /  | Get a list with all stereotype names in the platform
    @GET
    public String getStereotypes(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement Delete stereotypes
// DELETE /  | Delete stereotype from the platform. If no pattern remove all the stereotypes
    @DELETE
    public String deleteStereotypes(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern
    ) {

     return null;
    }


// TODO: Implement Remake stereotype
// PUT /:stereotypeName  | Remake stereotype of the given stereotype Name
    @Path("{stereotypeName}")
    @PUT
    public String remakeStereotype(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName
    ) {

     return null;
    }


// TODO: Implement Get stereotype users
// GET /:stereotypeName/users  | Get a list with users that belongs to the given stereotype name
    @Path("{stereotypeName}/users")
    @GET
    public String getStereotypeUsers(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement Get stereotype profile
// GET /:stereotypeName/profile  | Get stereotype profile for the given stereotype name
    @Path("{stereotypeName}/profile")
    @GET
    public String getStereotypeProfile(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
    

// ===========================================   
// TODO: Implement Add user on stereotype
// POST /users/:username/:stereotypeNameObject | Add a user on a stereotype with association degree
    @Path("users/{username}/{stereotypeNameObject}")
    @POST
    public String addUserStereotype(
            @PathParam("clientKey") String clientKey,
            @PathParam("username") String username,
            @PathParam("stereotypeNameObject") String stereotypeNameObject
    ) {

     return null;
    }

// TODO: Implement Get user’s stereotypes
// GET /users/:username | Get a list with stereotypes that user belongs
    @Path("users/{username}")
    @GET
    public String getUserStereotypes(
            @PathParam("clientKey") String clientKey,
            @PathParam("username") String username,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }

// TODO: Implement Set user’s stereotype degrees
// PUT /users/:userName/:stereotypeNameObject | Set new association degree for a stereotype
    @Path("users/{userName}/{stereotypeNameObject}")
    @PUT
    public String setUserStereotypes(
            @PathParam("clientKey") String clientKey,
            @PathParam("userName") String userName,
            @PathParam("stereotypeNameObject") String stereotypeNameObject
    ) {

     return null;
    }
    
// TODO: Implement Modify user’s stereotype degrees
// PUT /users/:userName/modify/:stereotypeNameObject | Modify association degree (increase/decrease) for a stereotype
    @Path("users/{userName}/modify/{stereotypeNameObject}")
    @PUT
    public String modifyUserStereotypes(
            @PathParam("clientKey") String clientKey,
            @PathParam("userName") String userName,
            @PathParam("stereotypeNameObject") String stereotypeNameObject
    ) {

     return null;
    }

// TODO: Implement Delete user from stereotype
// DELETE /users/:username/:stereotypeNameObject | Delete user from stereotypes
    @Path("users/{username}/{stereotypeNameObject}")
    @DELETE
    public String deleteUserStereotype(
            @PathParam("clientKey") String clientKey,
            @PathParam("username") String username,
            @PathParam("stereotypeNameObject") String stereotypeNameObject
    ) {

     return null;
    }
    
// TODO: Implement Update stereotype users
// PUT /users/:stereotypeName/update | Removes all users not matching the stereotypes rule and inserts any new users that do match
    @Path("users/{stereotypeName}/update")
    @PUT
    public String updateStereotypeUsers(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName
    ) {

     return null;
    }
    
// TODO: Implement Check stereotype users
// PUT /users/:stereotypeName/check | Removes any current users that do not match the stereotypes rule
    @Path("users/{stereotypeName}/check")
    @PUT
    public String checkStereotypeUsers(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName
    ) {

     return null;
    }
    
// TODO: Implement Find stereotype users
// PUT /users/:stereotypeName/find | Finds and adds to the stereotype all users matching its rule
    @Path("users/{stereotypeName}/find")
    @PUT
    public String findStereotypeUsers(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName
    ) {

     return null;
    }
    
    
    
// ===========================================   
// TODO: Implement Add features on stereotype
// POST /features/:stereotypeName/:featureNameObject | Add a list of feature names and value on a stereotype
    @Path("features/{stereotypeName}/{featureNameObject}")
    @POST
    public String addFeatureStereotype(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName,
            @PathParam("featureNameObject") String featureNameObject
    ) {

     return null;
    }
    
// TODO: Implement Delete feature from stereotypes
// DELETE /features/:featureName/:stereotypeNameObject | Delete feature from stereotypes
    @Path("features/{featureName}/{stereotypeNameObject}")
    @DELETE
    public String deleteFeatureStereotype(
            @PathParam("clientKey") String clientKey,
            @PathParam("featureName") String featureName,
            @PathParam("stereotypeNameObject") String stereotypeNameObject
    ) {

     return null;
    }
    
// TODO: Implement Set features on stereotype
// PUT /features/:stereotypeName/:featureNameObject | set a list of feature names and value on a stereotype
    @Path("features/{stereotypeName}/{featureNameObject}")
    @PUT
    public String setFeatureStereotypes(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName,
            @PathParam("featureNameObject") String featureNameObject
    ) {

     return null;
    }
    
// TODO: Implement Modify features on stereotype
// PUT /features/:stereotypeName/modify/:featureNameObject | Modify a list of feature names and value on a stereotype
    @Path("features/{stereotypeName}/modify/{featureNameObject}")
    @PUT
    public String modifyFeatureStereotypes(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName,
            @PathParam("featureNameObject") String featureNameObject
    ) {

     return null;
    }
    
// TODO: Implement Update stereotype features
// PUT /features/:stereotypeName/update | Removes all features from the stereotype 
//    and then adds and sets all features as needed based on the stereotypes current users and their degrees
    @Path("features/{stereotypeName}/update")
    @PUT
    public String updateStereotypeFeatures(
            @PathParam("clientKey") String clientKey,
            @PathParam("stereotypeName") String stereotypeName
    ) {

     return null;
    }



}
