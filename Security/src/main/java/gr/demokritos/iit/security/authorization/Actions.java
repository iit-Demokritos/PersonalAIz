/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.security.authorization;

import java.util.HashMap;
import java.util.Map;

/**
 * Implement the Action system for each functionality
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class Actions {

    private final HashMap<String, Action> adminActions = new HashMap<>();
    private final HashMap<String, Action> personalActions = new HashMap<>();
    private final HashMap<String, Action> stereotypeActions = new HashMap<>();
    private final HashMap<String, Action> communityActions = new HashMap<>();
    private final HashMap<String, Action> recommendationActions = new HashMap<>();

    public Actions() {

        //PServer admin actions
        adminActions.put("aAddClient", new Action("Admin.AddClient"));
        adminActions.put("aDeleteClient", new Action("Admin.DeleteClient"));
        adminActions.put("aGetClients", new Action("Admin.GetClients"));
        adminActions.put("aSetClientRoles", new Action("Admin.setClientRoles"));
        adminActions.put("aGetSettings", new Action("Admin.GetSettings"));
        adminActions.put("aSetSettings", new Action("Admin.SetSettings"));

        //PServer personal actions
        personalActions.put("aAddUser", new Action("Personal.AddUser"));
        personalActions.put("aAddUsers", new Action("Personal.AddUsers"));
        personalActions.put("aDeleteUsers", new Action("Personal.DeleteUsers"));
        personalActions.put("aSetUserAttributes", new Action("Personal.SetUserAttributes"));
        personalActions.put("aSetUserFeatures", new Action("Personal.SetUserFeatures"));
        personalActions.put("aModifyUserFeatures", new Action("Personal.ModifyUserFeatures"));
        personalActions.put("aGetUserAttributes", new Action("Personal.GetUserAttributes"));
        personalActions.put("aGetUserFeatures", new Action("Personal.GetUserFeatures"));
        personalActions.put("aGetUsers", new Action("Personal.GetUsers"));

        //PServer stereotype actions
        stereotypeActions.put("aGetSystemAttributes", new Action("Stereotype.GetSystemAttributes"));
        stereotypeActions.put("aAddStereotype", new Action("Stereotype.AddStereotype"));
        stereotypeActions.put("aDeleteStereotypes", new Action("Stereotype.DeleteStereotypes"));
        stereotypeActions.put("aGetStereotypes", new Action("Stereotype.GetStereotypes"));
        stereotypeActions.put("aRemakeStereotype", new Action("Stereotype.RemakeStereotype"));
        stereotypeActions.put("aUpadateStereotypeFeatures", new Action("Stereotype.UpadateStereotypeFeatures"));
        stereotypeActions.put("aUpadateStereotypeUsers", new Action("Stereotype.UpadateStereotypeUsers"));
        stereotypeActions.put("aFindStereotypeUsers", new Action("Stereotype.FindStereotypeUsers"));
        stereotypeActions.put("aCheckStereotypeUsers", new Action("Stereotype.CheckStereotypeUsers"));
        stereotypeActions.put("aSetStereotypeFeatures", new Action("Stereotype.SetStereotypeFeatures"));
        stereotypeActions.put("aModifyStereotypeFeatures", new Action("Stereotype.ModifyStereotypeFeatures"));
        stereotypeActions.put("aGetStereotypeFeatures", new Action("Stereotype.GetStereotypeFeatures"));
        stereotypeActions.put("aDeleteStereotypeFeatures", new Action("Stereotype.DeleteStereotypeFeatures"));
        stereotypeActions.put("aGetStereotypeUsers", new Action("Stereotype.GetStereotypeUsers"));
        stereotypeActions.put("aGetUserStereotypes", new Action("Stereotype.GetUserStereotypes"));
        stereotypeActions.put("aAddUserOnStereotype", new Action("Stereotype.AddUserOnStereotype"));
        stereotypeActions.put("aDeleteUserFromStereotype", new Action("Stereotype.DeleteUserFromStereotype"));

        //Recommendation actions
        recommendationActions.put("aFeed", new Action("Recommendation.Feed"));
        recommendationActions.put("aGetRecommendations", new Action("Recommendation.GeRecommendations"));
        recommendationActions.put("aAddUser", new Action("Recommendation.AddUser"));
        recommendationActions.put("aDeleteUser", new Action("Recommendation.DeleteUser"));

    }

    /**
     * Get PServer Admin actions
     *
     * @return A map with String:Action pairs
     */
    public Map<String, Action> getAdminActions() {
        return adminActions;
    }

    /**
     * Get PServer Personal actions
     *
     * @return A map with String:Action pairs
     */
    public Map<String, Action> getPersonalActions() {
        return personalActions;
    }

    /**
     * Get PServer Stereotype actions
     *
     * @return A map with String:Action pairs
     */
    public Map<String, Action> getStereotypeActions() {
        return stereotypeActions;
    }

    /**
     * Get PServer Community actions
     *
     * @return A map with String:Action pairs
     */
    public Map<String, Action> getCommunityActions() {
        return communityActions;
    }

    /**
     * Get recommendation actions
     *
     * @return A map with String:Action pairs
     */
    public HashMap<String, Action> getRecommendationActions() {
        return recommendationActions;
    }

    /**
     * Get All PServer actions
     *
     * @return A map with String:Action pairs
     */
    public Map<String, Action> getActions() {
        HashMap<String, Action> actions = new HashMap<>();
        actions.putAll(adminActions);
        actions.putAll(personalActions);
        actions.putAll(stereotypeActions);
        actions.putAll(communityActions);
        actions.putAll(recommendationActions);
        return actions;
    }

}
