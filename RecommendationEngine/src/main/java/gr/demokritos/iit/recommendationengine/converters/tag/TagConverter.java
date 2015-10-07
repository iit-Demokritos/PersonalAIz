/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.demokritos.iit.recommendationengine.converters.tag;

import gr.demokritos.iit.recommendationengine.converters.IConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This converter get a list with tags and create a feature list
 *
 * @author Giotis Panagiotis <giotis.p@gmail.com>
 */
public class TagConverter implements IConverter<String> {

    private final String language;

    /**
     * Tag Converter Constructor
     *
     * @param language The objects language
     */
    public TagConverter(String language) {
        this.language = language;
    }

    /**
     * Convert a list with tags to a PServer feature List
     *
     * @param objects A list with tag names
     * @return A Map with feature name and values
     */
    @Override
    public Map<String, Integer> getFeatures(List<String> objects) {

        HashMap<String, Integer> features = new HashMap<>();

        for (String cCategory : objects) {
            //Create feature name by add 
            //language + the prefix tag + the tag name to Lower Case
            String feature
                    = language
                    + ".tag."
                    + cCategory.toLowerCase().trim();

            //Add feature ton Map with value 1
            features.put(feature, 1);
        }

        return features;
    }

}
