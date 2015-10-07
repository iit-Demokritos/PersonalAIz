/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.onologies;

import java.util.List;

/**
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class FeedObject {

    private final String id;
    private final String language;
    private final boolean recommended;
    private final long timestamp;
    private List<String> texts;
    private List<String> categories;
    private List<String> tags;

    public FeedObject(String objectID,
            String lang,
            boolean recommended,
            long timestamp) {

        this.id = objectID;
        this.language = lang;
        this.recommended = recommended;
        this.timestamp = timestamp;
    }

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

}
