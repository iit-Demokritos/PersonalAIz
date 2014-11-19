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
@Path("{ClientKey}/community")
@Produces(MediaType.APPLICATION_JSON)
public class Community {


    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    

    
// TODO: Implement Add new user community
// POST /user/:CommunityName/:CommunityUsersObject | Add new user custom community on the platform
    @Path("user/{CommunityName}/{CommunityUsersObject}")
    @POST
    public String AddUserCommunity(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("CommunityName") String CommunityName,
            @PathParam("CommunityUsersObject") String CommunityUsersObject
    ) {

     return null;
    }
    
// TODO: Implement  Add user association
// POST /user/:User1/and/:User2/associate/:Weight | Add the weight of user’s association between two users
    @Path("user/{User1}/and/{User2}/associate/{Weight}")
    @POST
    public String AddUserAssociation(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("User1") String User1,
            @PathParam("User2") String User2,
            @PathParam("Weight") String Weight
    ) {

     return null;
    }
    
// TODO: Implement  Calculate User association
// POST /user/:MetricAlgorithm/association | Calculates the association distance for all users in the platform
    @Path("user/{MetricAlgorithm}/association")
    @POST
    public String CalculateUserAssociation(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("MetricAlgorithm") String MetricAlgorithm,
            @DefaultValue("*") @QueryParam("properties") String properties
    ) {

     return null;
    }
    
// TODO: Implement  Make user communities
// POST /user/:ClusterAlgorithm/with/:AssociationType | Make clustering to users and find user communities
    @Path("user/{ClusterAlgorithm}/with/{AssociationType}")
    @POST
    public String MakeUserCommunities(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("ClusterAlgorithm") String ClusterAlgorithm,
            @PathParam("AssociationType") String AssociationType,
            @DefaultValue("*") @QueryParam("properties") String properties
    ) {

     return null;
    }
    
// TODO: Implement  Delete user communities
// DELETE /user | Make clustering to users and find user communities
    @Path("user")
    @DELETE
    public String DeleteUserCommunities(
            @PathParam("ClientKey") String ClientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern
    ) {

     return null;
    }
    
// TODO: Implement  Get user communities
// GET /user | Get a list with user communities on the platform
    @Path("user")
    @GET
    public String GetUserCommunities(
            @PathParam("ClientKey") String ClientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get user community profile
// GET /user/:CommunityName | Get user’s community profile
    @Path("user/{CommunityName}")
    @GET
    public String GetUserCommunityProfile(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("CommunityName") String CommunityName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get user community users
// GET /user/:CommunityName/users | Get users that belongs to  given community user
    @Path("user/{CommunityName}/users")
    @GET
    public String GetUserCommunityUsers(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("CommunityName") String CommunityName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get users communities
// GET /user/:Username/communities | Get the communities that user contained
    @Path("user/{Username}/communities")
    @GET
    public String GetUserUserCommunities(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("Username") String Username,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
    
    
    
// ===========================================   
// TODO: Implement  Add new feature community
// POST /feature/:CommunityName/:CommunityFeatureObject | Add new custom feature community on the platform
    @Path("feature/{CommunityName}/{CommunityFeatureObject}")
    @POST
    public String AddFeatureCommunity(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("CommunityName") String CommunityName,
            @PathParam("CommunityFeatureObject") String CommunityFeatureObject
    ) {

     return null;
    }
    
// TODO: Implement  Calculate Feature association
// POST /feature/:MetricAlgorithm/association | Calculates the association distance for all features in the platform
    @Path("feature/{MetricAlgorithm}/association")
    @POST
    public String CalulateFeatureAssociation(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("MetricAlgorithm") String MetricAlgorithm,
            @DefaultValue("*") @QueryParam("properties") String properties
    ) {

     return null;
    }
    
// TODO: Implement  Make feature communities
// POST /feature/:ClusterAlgorithm/with/:AssociationType | Make clustering to features and find feature communities
    @Path("feature/{ClusterAlgorithm}/with/{AssociationType}")
    @POST
    public String MakeFeatureCommunities(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("ClusterAlgorithm") String ClusterAlgorithm,
            @PathParam("AssociationType") String AssociationType,
            @DefaultValue("*") @QueryParam("properties") String properties
    ) {

     return null;
    }
    
// TODO: Implement  Delete feature communities
// DELETE /feature | Delete feature communities from the platform
    @Path("feature")
    @DELETE
    public String DeleteFeatureCommunities(
            @PathParam("ClientKey") String ClientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern
    ) {

     return null;
    }
    
// TODO: Implement  Get feature communities
// GET /feature | Get a list with feature communities on the platform
    @Path("feature")
    @GET
    public String GetFeatureCommunities(
            @PathParam("ClientKey") String ClientKey,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get feature community features
// GET /feature/:CommunityName/features | Get features that belongs to  given feature community
    @Path("feature/{CommunityName}/features")
    @GET
    public String GetFeatureCommunityFeatures(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("CommunityName") String CommunityName,
            @DefaultValue("*") @QueryParam("pattern") String pattern,
            @DefaultValue("*") @QueryParam("page") String page
    ) {

     return null;
    }
    
// TODO: Implement  Get feature’s  feature communities
// GET /feature/:Featurename/communities | Get the communities that feature contained
    @Path("feature/{Featurename}/communities")
    @GET
    public String GetFeatureFeatureCommunities(
            @PathParam("ClientKey") String ClientKey,
            @PathParam("Featurename") String Featurename,
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
    public String GetMetricAlgorithms(
            @PathParam("ClientKey") String ClientKey
    ) {

     return null;
    }
    
// TODO: Get Clustering algorithms
// GET /clusters | Get list with cluster algorithms and their parameter description
    @Path("clusters")
    @GET
    public String GetClustersAlgorithms(
            @PathParam("ClientKey") String ClientKey
    ) {

     return null;
    }
    
    
    
}
