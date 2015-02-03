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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */


/**
 * Root resource (exposed at ":credentials/Community" path)
 */
@Path("{clientKey}/community")
@Produces(MediaType.APPLICATION_JSON)
public class Community {


    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    

    
// TODO: Implement Add new user community
// POST /user/:communityName/:communityUsersObject | Add new user custom community on the platform
    @Path("user/{communityName}/{communityUsersObject}")
    @POST
    public String addUserCommunity(
            @PathParam("clientKey") String clientKey,
            @PathParam("communityName") String communityName,
            @PathParam("communityUsersObject") String communityUsersObject
    ) {

     return null;
    }
    
// TODO: Implement  Add user association
// POST /user/:user1/and/:user2/associate/:weight | Add the weight of user’s association between two users
    @Path("user/{user1}/and/{user2}/associate/{weight}")
    @POST
    public String addUserAssociation(
            @PathParam("clientKey") String clientKey,
            @PathParam("user1") String user1,
            @PathParam("user2") String user2,
            @PathParam("weight") String weight
    ) {

     return null;
    }
    
// TODO: Implement  Calculate User association
// POST /user/:metricAlgorithm/association | Calculates the association distance for all users in the platform
    @Path("user/{metricAlgorithm}/association")
    @POST
    public String calculateUserAssociation(
            @PathParam("clientKey") String clientKey,
            @PathParam("metricAlgorithm") String metricAlgorithm,
            @DefaultValue("*") @QueryParam("properties") String properties
    ) {

     return null;
    }
    
// TODO: Implement  Make user communities
// POST /user/:clusterAlgorithm/with/:associationType | Make clustering to users and find user communities
    @Path("user/{clusterAlgorithm}/with/{associationType}")
    @POST
    public String makeUserCommunities(
            @PathParam("clientKey") String clientKey,
            @PathParam("clusterAlgorithm") String clusterAlgorithm,
            @PathParam("associationType") String associationType,
            @DefaultValue("*") @QueryParam("properties") String properties
    ) {

     return null;
    }
    
// TODO: Implement  Delete user communities
// DELETE /user | Make clustering to users and find user communities
    @Path("user")
    @DELETE
    public String deleteUserCommunities(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern
    ) {

     return null;
    }
    
// TODO: Implement  Get user communities
// GET /user | Get a list with user communities on the platform
    @Path("user")
    @GET
    public String getUserCommunities(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get user community profile
// GET /user/:communityName | Get user’s community profile
    @Path("user/{communityName}")
    @GET
    public String getUserCommunityProfile(
            @PathParam("clientKey") String clientKey,
            @PathParam("communityName") String communityName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get user community users
// GET /user/:communityName/users | Get users that belongs to  given community user
    @Path("user/{communityName}/users")
    @GET
    public String getUserCommunityUsers(
            @PathParam("clientKey") String clientKey,
            @PathParam("communityName") String communityName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get users communities
// GET /user/:username/communities | Get the communities that user contained
    @Path("user/{username}/communities")
    @GET
    public String getUserUserCommunities(
            @PathParam("clientKey") String clientKey,
            @PathParam("username") String username,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
    
    
    
// ===========================================   
// TODO: Implement  Add new feature community
// POST /feature/:communityName/:communityFeatureObject | Add new custom feature community on the platform
    @Path("feature/{communityName}/{communityFeatureObject}")
    @POST
    public String addFeatureCommunity(
            @PathParam("clientKey") String clientKey,
            @PathParam("communityName") String communityName,
            @PathParam("communityFeatureObject") String communityFeatureObject
    ) {

     return null;
    }
    
// TODO: Implement  Calculate Feature association
// POST /feature/:metricAlgorithm/association | Calculates the association distance for all features in the platform
    @Path("feature/{metricAlgorithm}/association")
    @POST
    public String calulateFeatureAssociation(
            @PathParam("clientKey") String clientKey,
            @PathParam("metricAlgorithm") String metricAlgorithm,
            @DefaultValue("*") @QueryParam("properties") String properties
    ) {

     return null;
    }
    
// TODO: Implement  Make feature communities
// POST /feature/:clusterAlgorithm/with/:associationType | Make clustering to features and find feature communities
    @Path("feature/{clusterAlgorithm}/with/{associationType}")
    @POST
    public String makeFeatureCommunities(
            @PathParam("clientKey") String clientKey,
            @PathParam("clusterAlgorithm") String clusterAlgorithm,
            @PathParam("associationType") String associationType,
            @DefaultValue("*") @QueryParam("properties") String properties
    ) {

     return null;
    }
    
// TODO: Implement  Delete feature communities
// DELETE /feature | Delete feature communities from the platform
    @Path("feature")
    @DELETE
    public String deleteFeatureCommunities(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern
    ) {

     return null;
    }
    
// TODO: Implement  Get feature communities
// GET /feature | Get a list with feature communities on the platform
    @Path("feature")
    @GET
    public String getFeatureCommunities(
            @PathParam("clientKey") String clientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get feature community features
// GET /feature/:communityName/features | Get features that belongs to  given feature community
    @Path("feature/{communityName}/features")
    @GET
    public String getFeatureCommunityFeatures(
            @PathParam("clientKey") String clientKey,
            @PathParam("communityName") String communityName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get feature’s  feature communities
// GET /feature/:featurename/communities | Get the communities that feature contained
    @Path("feature/{featurename}/communities")
    @GET
    public String getFeatureFeatureCommunities(
            @PathParam("clientKey") String clientKey,
            @PathParam("featurename") String featurename,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
    
    
    
// ===========================================   
// TODO: Get Metric algorithms
// GET /metrics | Get list with metric algorithms and their parameter description
    @Path("metrics")
    @GET
    public String getMetricAlgorithms(
            @PathParam("clientKey") String clientKey
    ) {

     return null;
    }
    
// TODO: Get Clustering algorithms
// GET /clusters | Get list with cluster algorithms and their parameter description
    @Path("clusters")
    @GET
    public String getClustersAlgorithms(
            @PathParam("clientKey") String clientKey
    ) {

     return null;
    }
    
    
    
}
