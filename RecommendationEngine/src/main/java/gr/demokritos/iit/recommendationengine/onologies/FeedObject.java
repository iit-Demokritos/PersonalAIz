/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.onologies;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class FeedObject {

    private final String id;
    private final String language;
    private boolean recommended;
    private long timestamp;
    @SerializedName("text")
    private List<String> texts;
    @SerializedName("category")
    private List<String> categories;
    @SerializedName("tag")
    private List<String> tags;
    @SerializedName("boolean")
    private List<String> booleans;
    @SerializedName("numeric")
    private List<String> numerics;
    @SerializedName("alphanumeric")
    private List<String> alphanumerics;

    public FeedObject(String objectID,
            String lang,
            boolean recommended,
            long timestamp) {

        this.id = objectID;
        this.language = lang;
        this.recommended = recommended;
        this.timestamp = timestamp;
    }

    //-------------------------------------------------------------------------
    public String getId() {
        return id;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLanguage() {
        return language;
    }

    //-------------------------------------------------------------------------
    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getBooleans() {
        return booleans;
    }

    public void setBooleans(List<String> booleans) {
        this.booleans = booleans;
    }

    public List<String> getNumerics() {
        return numerics;
    }

    public void setNumerics(List<String> numerics) {
        this.numerics = numerics;
    }

    public List<String> getAlphanumerics() {
        return alphanumerics;
    }

    public void setAlphanumerics(List<String> alphanumerics) {
        this.alphanumerics = alphanumerics;
    }
    
    

    public boolean isValidObject() {
        return id != null
                && language != null
                && (texts != null
                || categories != null
                || tags != null
                || booleans != null
                || numerics != null
                || alphanumerics != null);

    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public static FeedObject fromJSON(String json) {
        GsonBuilder b = new GsonBuilder().setPrettyPrinting();
        Gson g = b.create();
        return g.fromJson(json, FeedObject.class);
    }

    public static List<FeedObject> fromJSONArray(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json,
                new TypeToken<List<FeedObject>>() {
                }.getType());
    }

    @Override
    public String toString() {
        return "FeedObject{" + "id=" + id 
                + ", language=" + language 
                + ", recommended=" + recommended 
                + ", timestamp=" + timestamp 
                + ", texts=" + texts 
                + ", categories=" + categories 
                + ", tags=" + tags 
                + ", booleans=" + booleans 
                + ", numerics=" + numerics 
                + ", alpharethmetics=" + alphanumerics + '}';
    }

}
