/* 
 * Copyright 2016 IIT , NCSR Demokritos - http://www.iit.demokritos.gr,
 *                      SciFY NPO http://www.scify.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gr.demokritos.iit.recommendationengine.onologies;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * This is the declaration of what is a FeedObject for the recommendation
 * engine.
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

    /**
     * FeeObject Constructor
     *
     * @param objectID The objectID of the given object
     * @param lang The object language
     * @param recommended The flag if the object is recommended or not
     * @param timestamp The feed timestamp
     */
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
    /**
     * Get the object ID
     *
     * @return A string with objects id
     */
    public String getId() {
        return id;
    }

    /**
     * Check if object is recommended or not
     *
     * @return
     */
    public boolean isRecommended() {
        return recommended;
    }

    /**
     * Get the feed timestamp
     *
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Get the object language
     *
     * @return
     */
    public String getLanguage() {
        return language;
    }

    //-------------------------------------------------------------------------
    /**
     * Get the list with consisted texts for this object
     *
     * @return
     */
    public List<String> getTexts() {
        return texts;
    }

    /**
     * Set list with consisted texts for this object
     *
     * @param texts
     */
    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    /**
     * Get the list with consisted categories for this object
     *
     * @return
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * Set a list with consisted categories for this object
     *
     * @param categories
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    /**
     * Get the list with consisted tags for this object
     *
     * @return
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Set a list with consisted tags for this object
     *
     * @param tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Get the list with consisted booleans for this object
     *
     * @return
     */
    public List<String> getBooleans() {
        return booleans;
    }

    /**
     * Set a list with consisted booleans for this object
     *
     * @param booleans
     */
    public void setBooleans(List<String> booleans) {
        this.booleans = booleans;
    }

    /**
     * Get the list with consisted numerics for this object
     *
     * @return
     */
    public List<String> getNumerics() {
        return numerics;
    }

    /**
     * Set a list with consisted numerics for this object
     *
     * @param numerics
     */
    public void setNumerics(List<String> numerics) {
        this.numerics = numerics;
    }

    /**
     * Get the list with consisted Alphanumerics for this object
     *
     * @return
     */
    public List<String> getAlphanumerics() {
        return alphanumerics;
    }

    /**
     * Set a list with consisted Alphanumerics for this object
     *
     * @param alphanumerics
     */
    public void setAlphanumerics(List<String> alphanumerics) {
        this.alphanumerics = alphanumerics;
    }

    /**
     * Check if the Object is valid or not. Valid is a object when has
     * id,language and at least on of the
     * texts,categories,tags,booleans,numerics,alphanumerics
     *
     * @return
     */
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

    /**
     * Convert the object to JSON
     *
     * @return
     */
    public String toJSON() {
        return new Gson().toJson(this);
    }

    /**
     * Convert a JSON object to feedobject
     *
     * @param json
     * @return
     */
    public static FeedObject fromJSON(String json) {
        GsonBuilder b = new GsonBuilder().setPrettyPrinting();
        Gson g = b.create();
        return g.fromJson(json, FeedObject.class);
    }

    /**
     * Convert a JSON ObjectList to List of feedobjects
     *
     * @param json
     * @return
     */
    public static List<FeedObject> fromJSONArray(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json,
                new TypeToken<List<FeedObject>>() {
        }.getType());
    }

    /**
     * Generate a string with all object properties
     *
     * @return
     */
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
